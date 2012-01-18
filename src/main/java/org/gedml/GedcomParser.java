package org.gedml;

import java.util.*;
import java.io.*;
import java.net.URL;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * org.gedml.Gedcom2Json
 * <p/>
 * This class is designed to look like a SAX2-compliant XML parser; however,
 * it takes GEDCOM as its input rather than XML.
 * The events sent to the ContentHandler reflect the GEDCOM input "as is";
 * there is no validation or conversion of tags.
 *
 * @author mhkay@iclway.co.uk
 * @version 22 March 2006 - revised by lmonson.com to support string inlining, a few sax feature settings and additional encodings
 * Also revised in 2007 by Nathan Powell and revised in 2011 by Dallan Quass
 */
public class GedcomParser implements XMLReader, Locator {
   private static final List<String> ACCEPTED_TRUE_SAX_FEATURES = Arrays.asList(
           "http://xml.org/sax/features/namespace-prefixes",
           // OK to support, since non are produced from gedcom
           "http://xml.org/sax/features/external-general-entities",
           // OK to support, since non are produced from gedcom
           "http://xml.org/sax/features/external-parameter-entities",
           // OK to support, since non are produced from gedcom
           "http://xml.org/sax/features/string-interning"
   );
   private static final List<String> EXPECTED_CHAR_ENCODINGS = Arrays.asList(
           "ANSEL","ASCII","Cp850","Cp874","Cp1251","Cp1252","Cp1254","UTF-8","x-MacRoman","UTF-16","UnicodeBigUnmarked"
   );

   private ContentHandler contentHandler;
   private ErrorHandler errorHandler;
   private AttributesImpl emptyAttList = new AttributesImpl();
   private AttributesImpl attList = new AttributesImpl();
   private EntityResolver entityResolver = null;
   private String systemId;
   private int lineNr;

   /**
    * Set the ContentHandler
    *
    * @param handler User-supplied content handler
    */
   public void setContentHandler(ContentHandler handler) {
      contentHandler = handler;
   }

   /**
    * Get the ContentHandler
    */
   public ContentHandler getContentHandler() {
      return contentHandler;
   }

   /**
    * Set the entityResolver.
    * This call has no effect, because entities are not used in GEDCOM files.
    */
   public void setEntityResolver(EntityResolver er) {
      entityResolver = er;
   }

   /**
    * Get the entityResolver
    */
   public EntityResolver getEntityResolver() {
      return entityResolver;
   }

   /**
    * Set the DTDHandler
    * This call has no effect, because DTDs are not used in GEDCOM files.
    */
   public void setDTDHandler(DTDHandler dh) {
   }

   /**
    * Get the DTDHandler
    */
   public DTDHandler getDTDHandler() {
      return null;
   }

   /**
    * Set the error handler
    *
    * @param eh A user-supplied error handler
    */
   public void setErrorHandler(ErrorHandler eh) {
      errorHandler = eh;
   }

   /**
    * Get the error handler
    */
   public ErrorHandler getErrorHandler() {
      return errorHandler;
   }

   public static String readCorrectedCharsetName(InputStream is) throws IOException {
      BufferedReader in = new BufferedReader(new InputStreamReader(is));
      return readCorrectedCharsetName(in);
   }

   private static String readCorrectedCharsetName(BufferedReader in) throws IOException {
      // We will only try to read the first 100 lines of
      // the file attempting to get the char encoding.
      String line;
      String generatorName = null;
      String encoding = null;
      String version = null;
      for (int i = 0; i < 100; i++) {
         line = in.readLine();
         if (line != null) {
            String [] split = line.trim().split("\\s+", 3);
            if (split.length == 3) {
               if (generatorName == null &&
                       split[0].equals("1") &&
                       split[1].equals("SOUR")) {
                  generatorName = split[2];
               }
               else if (split[0].equals("1") &&
                       (split[1].equals("CHAR") || split[1].equals("CHARACTER"))) {
                  // get encoding
                  encoding = split[2].toUpperCase();
                  // look for version
                  line = in.readLine();
                  if (line != null) {
                     split = line.trim().split("\\s+", 3);
                     if (split.length == 3 && split[0].equals("2") && split[1].equals("VERS")) {
                        version = split[2];
                     }
                  }
               }
            }
         }
         if (generatorName != null && encoding != null) {
            break; // got what we need
         }
      }
      in.close();

      return getCorrectedCharsetName(generatorName, encoding, version);
   }

   public static String getCorrectedCharsetName(String generatorName, String encoding, String version) {
      // correct incorrectly-assigned encoding values
      if ("GeneWeb".equals(generatorName) && "ASCII".equals(encoding)) {
         // GeneWeb ASCII -> Cp1252 (ANSI)
         encoding = "Cp1252";
      }
      else if ("Geni.com".equals(generatorName) && "UNICODE".equals(encoding)) {
         // Geni.com UNICODE -> UTF-8
         encoding = "UTF-8";
      }
      else if ("Geni.com".equals(generatorName) && "ANSEL".equals(encoding)) {
         // Geni.com ANSEL -> UTF-8
         encoding = "UTF-8";
      }
      else if ("GENJ".equals(generatorName) && "UNICODE".equals(encoding)) {
         // GENJ UNICODE -> UTF-8
         encoding = "UTF-8";
      }

      // make encoding value java-friendly
      else if ("ASCII".equals(encoding)) { // ASCII followed by VERS MacOS Roman is MACINTOSH
         if ("MacOS Roman".equals(version)) {
            encoding = "x-MacRoman";
         }
      }
      else if ("ATARIST_ASCII".equals(encoding)) {
         encoding = "ASCII";
      }
      else if ("MACROMAN".equals(encoding) || "MACINTOSH".equals(encoding)) {
         encoding = "x-MacRoman";
      }
      else if ("ANSI".equals(encoding) || "IBM WINDOWS".equals(encoding)) {
         encoding = "Cp1252";
      }
      else if ("WINDOWS-874".equals(encoding)) {
         encoding = "Cp874";
      }
      else if ("WINDOWS-1251".equals(encoding)) {
         encoding = "Cp1251";
      }
      else if ("WINDOWS-1254".equals(encoding)) {
         encoding = "Cp1254";
      }
      else if ("IBMPC".equals(encoding) || "IBM DOS".equals(encoding)) {
         encoding = "Cp850";
      }
      else if ("UNICODE".equals(encoding)) {
         encoding = "UTF-16";
      }
      else if ("UTF-16BE".equals(encoding)) {
         encoding = "UnicodeBigUnmarked";
      }
      else if (encoding == null) {
         encoding = ""; // not found
      }

      return encoding;
   }

   private BufferedReader getBufferedReader(String systemId) throws IOException, SAXException {
      InputStream in = (new URL(systemId)).openStream();
      String charEncoding = readCorrectedCharsetName(in);
      in.close();

      if (charEncoding.length() == 0) {
         // Let's try again with a UTF-16 reader.
         BufferedReader br = new BufferedReader(new InputStreamReader((new URL(systemId)).openStream(), "UTF-16"));
         charEncoding = readCorrectedCharsetName(br);
         br.close();
         if (charEncoding.equals("UTF-16")) {
            // skip over junk at the beginning of the file
            InputStreamReader reader = new InputStreamReader((new URL(systemId)).openStream(), "UTF-16");
            int cnt = 0;
            int c;
            while ((c = reader.read()) != '0' && c != -1) {
               cnt++;
            }
            reader.close();
            reader = new InputStreamReader((new URL(systemId)).openStream(), "UTF-16");
            for (int i = 0; i < cnt; i++) {
               reader.read();
            }
            return new BufferedReader(reader);
         }
      }

      if (charEncoding.length() == 0) {
         charEncoding = "ANSEL"; // default
      }

      if (EXPECTED_CHAR_ENCODINGS.contains(charEncoding)) {
         // skip over junk at the beginning of the file
         in = (new URL(systemId)).openStream();
         int cnt = 0;
         int c;
         while ((c = in.read()) != '0' && c != -1) {
            cnt++;
         }
         in.close();
         in = (new URL(systemId)).openStream();
         for (int i = 0; i < cnt; i++) {
            in.read();
         }

         InputStreamReader reader;
         if (charEncoding.equals("ANSEL")) {
            reader = new AnselInputStreamReader(in);
         }
         else {
            reader = new InputStreamReader(in, charEncoding);
         }
         return new BufferedReader(reader);
      }

      throw new SAXException("Unrecognized encoding in gedcom file: " + charEncoding);
   }

   /**
    * Parse input from the supplied InputSource
    */
   public void parse(InputSource source) throws IOException, SAXParseException {
      parse(source.getSystemId());
   }

   /**
    * Parse input from the supplied systemId
    */
   public void parse(String systemId) throws IOException, SAXParseException {
      this.systemId = systemId;
      String line;
      int thisLevel;
      int prevLevel = -1;
      String iden, tag, xref, valu;
      lineNr = 0;
      Stack<String> stack = new Stack<String>();
      stack.push("GED");
      BufferedReader reader = null;
      StringBuilder buf = new StringBuilder();

      try {
         reader = getBufferedReader(systemId);
         contentHandler.setDocumentLocator(this);
         contentHandler.startDocument();
         contentHandler.startElement("", "GED", "GED", emptyAttList);
         boolean goodLine = false; // Indicates whether we have found a good line so far in the file.
         GedcomLineParser lineParser = new GedcomLineParser();
         while ((line = reader.readLine()) != null) {
            lineNr++;

            // remove control chars
            buf.setLength(0);
            for (int j=0; j < line.length(); j++) {
               char c = line.charAt(j);
               if (c >= 32 || c == 9) {
                  buf.append(c);
               }
            }
            line = buf.toString();

            if (line.length() > 0) {
               // parse the GEDCOM line into five fields: level, iden, tag, xref, value
               if (!lineParser.parse(line))
               {
                  if (goodLine) {
                     errorHandler.error(new SAXParseException("Line does not appear to be standard @ " +
                             this.getLineNumber() + " appending content to the last tag started." + line, this));
                     contentHandler.characters(line.toCharArray(), 0, line.length());
                  } // if we haven't found a good line yet, just skip it
                  if (lineNr > 20 && !goodLine) {
                     break;
                  }
               }
               else {
                  thisLevel = Integer.parseInt(lineParser.getLevel());
                  tag = lineParser.getTag();

                  // if level is > prevlevel+1, ignore it until it comes back down
                  if (thisLevel > prevLevel+1) {
                     errorHandler.error(new SAXParseException("Level > prevLevel+1 @ " + this.getLineNumber(), this));
                  }
                  else if (thisLevel < 0) {
                     errorHandler.error(new SAXParseException("Level < 0 @ " + this.getLineNumber(), this));
                  }
                  else if (tag == null || tag.length() == 0) {
                     errorHandler.error(new SAXParseException("Tag not found @ " + this.getLineNumber(), this));
                  }
                  else {
                     iden = lineParser.getID();
                     xref = lineParser.getXRef();
                     valu = lineParser.getValue();

                     // insert any necessary closing tags
                     while (thisLevel <= prevLevel) {
                        String endtag = stack.pop();
                        contentHandler.endElement("", endtag, endtag);
                        prevLevel--;
                     }
                     attList.clear();
                     if (iden != null && iden.length() > 0) attList.addAttribute("", "ID", "ID", "ID", iden);
                     if (xref != null && xref.length() > 0) attList.addAttribute("", "REF", "REF", "IDREF", xref);
                     contentHandler.startElement("", tag, tag, attList);
                     goodLine = true;
                     stack.push(tag);
                     prevLevel = thisLevel;
                     if (valu != null && valu.length() > 0) {
                        contentHandler.characters(valu.toCharArray(), 0, valu.length());
                     }
                  }
               }
            }
         }

         if (!goodLine) {
            throw new SAXParseException("no good lines found in the first 20 lines ", this);
         }
         contentHandler.endElement("", "GED", "GED");
         contentHandler.endDocument();
      } catch (SAXException e) {
         SAXParseException err = new SAXParseException("SAXException: "+e.getMessage(), this);
         try {
            errorHandler.fatalError(err);
         } catch (SAXException e1) {
            // ignore
         }
         throw err;
      } catch (EmptyStackException e) {
         SAXParseException err = new SAXParseException("EmptyStack: "+e.getMessage(), this);
         try {
            errorHandler.fatalError(err);
         } catch (SAXException e1) {
            // ignore
         }
         throw err;
      }
      finally {
         if (reader != null) {
            reader.close();
         }
      }
   }

   /**
    * Set a feature
    */
   public void setFeature(String s, boolean b) throws SAXNotRecognizedException {
      if (!b || !ACCEPTED_TRUE_SAX_FEATURES.contains(s))
         throw new SAXNotRecognizedException("Gedcom Parser does not recognize the feature '" + s + "'");
   }

   /**
    * Get a feature
    */
   public boolean getFeature(String s) throws SAXNotRecognizedException {
      if (s.equals("http://xml.org/sax/features/namespaces")) return true;
      if (s.equals("http://xml.org/sax/features/namespace-prefixes")) return false;
      throw new SAXNotRecognizedException("Gedcom Parser does not recognize any features");
   }

   /**
    * Set a property
    */
   public void setProperty(String s, Object b) throws SAXNotRecognizedException {
      throw new SAXNotRecognizedException("Gedcom Parser does not recognize any properties");
   }

   /**
    * Get a property
    */
   public Object getProperty(String s) throws SAXNotRecognizedException {
      throw new SAXNotRecognizedException("Gedcom Parser does not recognize any properties");
   }

   /**
    * Get the publicId: always null
    */
   public String getPublicId() {
      return null;
   }

   /**
    * Get the system ID
    */
   public String getSystemId() {
      return systemId;
   }

   /**
    * Get the line number
    */
   public int getLineNumber() {
      return lineNr;
   }

   /**
    * Get the column number: always -1
    */
   public int getColumnNumber() {
      return -1;
   }
}

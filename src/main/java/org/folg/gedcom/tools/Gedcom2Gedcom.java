package org.folg.gedcom.tools;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.folg.gedcom.visitors.GedcomWriter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;

import java.io.*;
import java.util.logging.Logger;

/**
 * User: dallan
 * Date: 1/7/12
 */
public class Gedcom2Gedcom {
   private static final Logger logger = Logger.getLogger("org.folg.gedcom.tools");

   @Option(name="-i", required=true, usage="file or directory containing gedcom files to convert")
   private File gedcomIn;

   @Option(name="-o", required=false, usage="target directory")
   private File gedcomOut;

   private ModelParser parser;
   private GedcomWriter writer;
   private File file;

   public Gedcom2Gedcom() {
      parser = new ModelParser();
      writer = new GedcomWriter();
   }

   public void convertGedcom(File gedcomFile) {
      this.file = gedcomFile;
      try {
         Gedcom gedcom = parser.parseGedcom(gedcomFile);
         OutputStream out = (gedcomOut != null ? new FileOutputStream(new File(gedcomOut, gedcomFile.getName())) :
                                                 new ByteArrayOutputStream());
         writer.write(gedcom, out);
         if (gedcomOut != null) {
            out.close();
         }
         else {
            System.out.println(out.toString());
         }
      } catch (SAXParseException e) {
         logger.severe("SaxParseException for file: "+file.getName()+" "+e.getMessage()+" @ "+e.getLineNumber());
      } catch (IOException e) {
         logger.severe("IOException for file: " + file.getName() + " " + e.getMessage());
      } catch (RuntimeException e) {
         e.printStackTrace();
         logger.severe("Exception for file: "+file.getName()+" "+e.getMessage());
      }
   }

   private void doMain() throws FileNotFoundException {
      if (gedcomIn.isDirectory()) {
         for (File file : gedcomIn.listFiles()) {
            convertGedcom(file);
         }
      }
      else if (gedcomIn.isFile()) {
         convertGedcom(gedcomIn);
      }
   }

   public static void main(String[] args) throws FileNotFoundException {
      Gedcom2Gedcom self = new Gedcom2Gedcom();
      CmdLineParser parser = new CmdLineParser(self);
      try {
         parser.parseArgument(args);
         self.doMain();
      }
      catch (CmdLineException e) {
         System.err.println(e.getMessage());
         parser.printUsage(System.err);
      }
   }
}

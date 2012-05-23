package org.folg.gedcom.tools;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.folg.gedcom.visitors.GedcomWriter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: dallan
 * Date: 1/7/12
 *
 * Convert a GEDCOM file or directory to the model and back again
 */
public class Gedcom2Gedcom {
   private static final Logger logger = LoggerFactory.getLogger("org.folg.gedcom.tools");

   @Option(name="-i", required=true, usage="file or directory containing gedcom files to convert")
   private File gedcomIn;

   @Option(name="-o", required=false, usage="target directory")
   private File gedcomOut;

   private ModelParser parser;
   private GedcomWriter writer;

   public Gedcom2Gedcom() {
      parser = new ModelParser();
      writer = new GedcomWriter();
   }

   public void convertGedcom(File file) {
      try {
         Gedcom gedcom = parser.parseGedcom(file);
         OutputStream out = (gedcomOut != null ? new FileOutputStream(new File(gedcomOut, file.getName())) :
                                                 new ByteArrayOutputStream());
         writer.write(gedcom, out);
         if (gedcomOut != null) {
            out.close();
         }
         else {
            System.out.println(out.toString());
         }
      } catch (SAXParseException e) {
         logger.error("SaxParseException for file: "+file.getName()+" "+e.getMessage()+" @ "+e.getLineNumber());
      } catch (IOException e) {
         logger.error("IOException for file: " + file.getName() + " " + e.getMessage());
      } catch (RuntimeException e) {
         e.printStackTrace();
         logger.error("Exception for file: "+file.getName()+" "+e.getMessage());
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

package org.folg.gedcom.tools;

import org.folg.gedcom.model.*;
import org.folg.gedcom.parser.ModelParser;
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
 */
public class PlaceWriter extends Visitor {
   private static final Logger logger = LoggerFactory.getLogger("org.folg.gedcom.tools");

   @Option(name="-i", required=true, usage="file or directory containing gedcom files to process")
   private File gedcomIn;

   @Option(name="-o", required=true, usage="target file for places")
   private File placesOut;

   private ModelParser parser;
   private PrintWriter out;

   public PlaceWriter() {
      parser = new ModelParser();
   }

   @Override
   public boolean visit(EventFact eventFact) {
      String place = eventFact.getPlace();
      if (place != null && place.length() > 0) {
         out.println(place);
      }
      return true;
   }

   public void processGedcom(File file) throws SAXParseException, IOException {
      Gedcom gedcom = parser.parseGedcom(file);
      gedcom.accept(this);
   }

   private void doMain() throws IOException, SAXParseException {
      out = new PrintWriter(new FileWriter(placesOut));
      if (gedcomIn.isDirectory()) {
         for (File file : gedcomIn.listFiles()) {
            processGedcom(file);
         }
      }
      else if (gedcomIn.isFile()) {
         processGedcom(gedcomIn);
      }
      out.close();
   }

   public static void main(String[] args) throws IOException, SAXParseException {
      PlaceWriter self = new PlaceWriter();
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

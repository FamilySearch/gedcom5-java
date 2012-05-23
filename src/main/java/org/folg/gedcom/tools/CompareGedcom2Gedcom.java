package org.folg.gedcom.tools;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.GedcomTag;
import org.folg.gedcom.parser.ErrorHandler;
import org.folg.gedcom.parser.ModelParser;
import org.folg.gedcom.parser.TreeParser;
import org.folg.gedcom.visitors.GedcomWriter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Ryan K.
 * Date: 1/7/12
 * <p/>
 * Convert a GEDCOM file or directory to the model and back again
 */
public class CompareGedcom2Gedcom implements ErrorHandler {
   private static final Logger logger = LoggerFactory.getLogger("org.folg.gedcom.tools");

   @Option(name = "-i", required = true, usage = "file or directory containing gedcom files to convert")
   private File gedcomIn;

   @Option(name = "-o", required = false, usage = "temporary directory to store generated gedcom in")
   private File gedcomOut;


   private int totalGedcoms = 0;
   private int equalsCount = 0;
   private int expectedNotEqualsCount = 0;
   private int unexpectedNotEqualsCount = 0;
   private boolean hasError;
   private boolean logWarningsErrors;
   private String currentFilename = null;

   @Override
   public void warning(String message, int lineNumber) {
      if (logWarningsErrors) {
         logger.warn(message+" @ "+lineNumber);
      }
   }

   @Override
   public void error(String message, int lineNumber) {
      hasError = true;
      if (logWarningsErrors) {
         logger.error(message+" @ "+lineNumber+" => "+currentFilename);
      }
   }

   @Override
   public void fatalError(String message, int lineNumber) {
      // handled below
   }

   public void convertAndCompareGedcom(File file) {

      ModelParser parser = new ModelParser();
      parser.setErrorHandler(this);
      TreeParser treeParser = new TreeParser();
      treeParser.setErrorHandler(this);
      GedcomWriter writer = new GedcomWriter();

      try {
         hasError = false;
         logWarningsErrors = false;
         Gedcom gedcom = parser.parseGedcom(file);

         File tempFile = (gedcomOut != null ? new File(gedcomOut, file.getName()) :
                 File.createTempFile(file.getName(), "tmp"));
         OutputStream out = new FileOutputStream(tempFile);

         writer.write(gedcom, out);
         if (out != null) {
            out.close();
         }

         totalGedcoms++;

         List<GedcomTag> gedcomTags = treeParser.parseGedcom(file);
         logWarningsErrors = true;
         currentFilename = tempFile.getName();
         List<GedcomTag> compareGedcomTags = treeParser.parseGedcom(tempFile);

         if (equals(gedcomTags, compareGedcomTags)) {
            equalsCount++;
         }
         else if (hasError) {
            expectedNotEqualsCount++;
         }
         else {
            logger.warn("Unexpected not equal: " + file.getName());
            unexpectedNotEqualsCount++;
         }

      } catch (SAXParseException e) {
         logger.error("SaxParseException for file: " + file.getName() + " " + e.getMessage() + " @ " + e.getLineNumber());
      } catch (IOException e) {
         logger.error("IOException for file: " + file.getName() + " " + e.getMessage());
      } catch (RuntimeException e) {
         e.printStackTrace();
         logger.error("Exception for file: " + file.getName() + " " + e.getMessage());
      }
   }

   @SuppressWarnings("unchecked")
   private boolean equals(List<GedcomTag> gedcomTags, List<GedcomTag> compareGedcomTags) throws SAXParseException, IOException {
      // merge CONC tags
      mergeContConcTags(gedcomTags);
      mergeContConcTags(compareGedcomTags);

      // remove empty tags
      removeEmptyTags(gedcomTags);
      removeEmptyTags(compareGedcomTags);

      // convert multiple TEXT tags into CONT tags
      convertMultipleTextTags(gedcomTags);

      // sort tags
      Collections.sort(gedcomTags);
      Collections.sort(compareGedcomTags);

      // check whether the lists of tags are equal
      if (gedcomTags.size() != compareGedcomTags.size()) {
         return false;
      }
      for (int indx = 0; indx < gedcomTags.size(); indx++) {
         GedcomTag gedcomTagOne = gedcomTags.get(indx);
         GedcomTag gedcomTagTwo = compareGedcomTags.get(indx);

         if (!gedcomTagOne.equals(gedcomTagTwo)) {
            logger.info("file="+currentFilename+" !tag="+gedcomTagOne.toString()+"<=====>"+gedcomTagTwo.toString());
            return false;
         }
      }

      return true;
   }

   private void mergeContConcTags(List<GedcomTag> gedcomTags) {
      for (GedcomTag tag : gedcomTags) {
         mergeContConcTags(tag);
      }
   }

   private void mergeContConcTags(GedcomTag tag) {
      List<GedcomTag> children = tag.getChildren();
      if (children.size() > 0) {
         for (int i = 0; i < children.size(); i++) {
            GedcomTag child = children.get(i);
            mergeContConcTags(child);
            if ("CONC".equals(child.getTag()) || "CONT".equals(child.getTag())) {
               String concValue = child.getValue() == null ? "" : child.getValue();
               if ("CONT".equals(child.getTag())) {
                  concValue = "\n"+concValue;
               }
               GedcomTag target = (i == 0 ? tag : children.get(i-1));
               String value = target.getValue() == null ? "" : target.getValue();
               target.setValue(value+concValue);
               for (GedcomTag tagToMove : child.getChildren()) {
                  target.addChild(tagToMove);
               }
               children.remove(i);
               i--;
            }
         }
         if (tag.getChildren().size() == 0) {
            tag.setChildren(null);
         }
      }
   }

   private void removeEmptyTags(List<GedcomTag> gedcomTags) {
      Iterator<GedcomTag> iter = gedcomTags.iterator();
      while (iter.hasNext()) {
         GedcomTag tag = iter.next();
         removeEmptyTags(tag.getChildren());
         if (tag.isEmpty()) {
            iter.remove();
         }
      }
   }

   // convert geni's 3 TEXT ... 3 TEXT ... sequence into 3 TEXT ... 4 CONT ... which should be considered equivalent
   private void convertMultipleTextTags(List<GedcomTag> gedcomTags) {
      Iterator<GedcomTag> iter = gedcomTags.iterator();
      GedcomTag prevTag = null;
      while (iter.hasNext()) {
         GedcomTag tag = iter.next();
         if ("TEXT".equals(tag.getTag())) {
            if (prevTag != null && "TEXT".equals(prevTag.getTag()) && prevTag.getChildren().size() == 0) {
               iter.remove();
               tag.setTag("CONT");
               prevTag.addChild(tag);
            }
         }
         prevTag = tag;
         convertMultipleTextTags(tag.getChildren());
      }
   }

   private void doMain() throws FileNotFoundException {
      if (gedcomIn.isDirectory()) {
         int cnt = 0;
         for (File file : gedcomIn.listFiles()) {
            convertAndCompareGedcom(file);
            if (++cnt % 100 == 0) {
               System.out.print(".");
            }
         }
      } else if (gedcomIn.isFile()) {
         convertAndCompareGedcom(gedcomIn);
      }

      System.out.println("Total Number of Gedcoms = " + totalGedcoms);
      System.out.println("Number of Gedcoms that are Equal = " + equalsCount);
      System.out.println("Expected number of Gedcoms that are NOT Equal = " + expectedNotEqualsCount);
      System.out.println("UNEXPECTED number of Gedcoms that are NOT Equal = " + unexpectedNotEqualsCount);
   }

   public static void main(String[] args) throws FileNotFoundException {
      CompareGedcom2Gedcom self = new CompareGedcom2Gedcom();
      CmdLineParser parser = new CmdLineParser(self);
      try {
         parser.parseArgument(args);
         self.doMain();
      } catch (CmdLineException e) {
         System.err.println(e.getMessage());
         parser.printUsage(System.err);
      }
   }
}

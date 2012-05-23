/*
 * Copyright 2011 Foundation for On-Line Genealogy, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.folg.gedcom.tools;

import org.folg.gedcom.parser.ErrorHandler;
import org.folg.gedcom.parser.ModelParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: dallan
 * Date: 1/2/12
 */
public class GedcomAnalyzer implements ErrorHandler {
   private static final Logger logger = LoggerFactory.getLogger("org.folg.gedcom.tools");

   @Option(name="-i", required=true, usage="file or directory containing gedcom files to analyze")
   private File gedcomIn;

   @Option(name="-w", required=false, usage="warnings file out")
   private File warningsOut;

   @Option(name="-e", required=false, usage="errors file out")
   private File errorsOut;

   private ModelParser parser;
   private Set<String> warnings;
   private Set<String> errors;
   private CountsCollector ccWarnings;
   private CountsCollector ccErrors;
   private int cntWarnings;
   private int cntErrors;
   private int cntTotal;
   private File file;

   public GedcomAnalyzer() {
      parser = new ModelParser();
      parser.setErrorHandler(this);
      ccWarnings = new CountsCollector();
      ccErrors = new CountsCollector();
      cntTotal = 0;
      cntWarnings = 0;
      cntErrors = 0;
   }

   public void warning(String message, int lineNumber) {
      logger.info(message+" @ "+lineNumber);
      warnings.add(message);
   }

   public void error(String message, int lineNumber) {
      logger.warn(message+" @ "+lineNumber);
      errors.add(message);
   }

   public void fatalError(String message, int lineNumber) {
      // we'll log this below
   }

   public void analyzeGedcom(File gedcomFile) {
      warnings = new HashSet<String>();
      errors = new HashSet<String>();
      this.file = gedcomFile;

      try {
         parser.parseGedcom(gedcomFile);
         if (warnings.size() > 0 || errors.size() > 0) {
            cntWarnings++;
         }
         if (errors.size() > 0) {
            cntErrors++;
         }
         for (String warning : warnings) {
            ccWarnings.add(warning);
         }
         for (String error : errors) {
            ccErrors.add(error);
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

   public CountsCollector getWarnings() {
      return ccWarnings;
   }

   public CountsCollector getErrors() {
      return ccErrors;
   }

   private void doMain() throws FileNotFoundException {
      if (gedcomIn.isDirectory()) {
         for (File file : gedcomIn.listFiles()) {
            analyzeGedcom(file);
         }
      }
      else if (gedcomIn.isFile()) {
         analyzeGedcom(gedcomIn);
      }

      System.out.println("Total="+cntTotal+" with warnings="+ cntWarnings +" with errors="+ cntErrors);
      getWarnings().writeSorted(false, 1, warningsOut != null ? new PrintWriter(warningsOut) : new PrintWriter(System.out));
      getErrors().writeSorted(false, 1, errorsOut != null ? new PrintWriter(errorsOut) : new PrintWriter(System.out));
   }

   public static void main(String[] args) throws FileNotFoundException {
      GedcomAnalyzer self = new GedcomAnalyzer();
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

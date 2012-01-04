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

import org.folg.gedcom.parser.JsonParser;
import org.folg.gedcom.parser.ModelParser;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.GedcomTag;
import org.folg.gedcom.parser.TreeParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * User: Dallan
 * Date: 1/1/12
 */
public class Gedcom2Json {
   @Option(name="-i", required=true, usage="gedcom file in")
   private File gedcomIn;

   @Option(name="-o", required=false, usage="json file out")
   private File jsonOut;

   @Option(name="-t", required=false, usage="use tree parser (use model parser by default)")
   private boolean useTreeParser = false;

   private void doMain() throws SAXParseException, IOException {
      String json;
      JsonParser jsonParser = new JsonParser();
      if (useTreeParser) {
         TreeParser treeParser = new TreeParser();
         List<GedcomTag> gedcomTags = treeParser.parseGedcom(gedcomIn);
         json = jsonParser.toJson(gedcomTags);
      }
      else {
         ModelParser modelParser = new ModelParser();
         Gedcom gedcom = modelParser.parseGedcom(gedcomIn);
         json = jsonParser.toJson(gedcom);
      }
      if (jsonOut != null) {
         PrintWriter writer = new PrintWriter(jsonOut);
         writer.println(json);
         writer.close();
      }
      else {
         System.out.println(json);
      }
   }

   public static void main(String[] args) throws SAXParseException, IOException {
      Gedcom2Json self = new Gedcom2Json();
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

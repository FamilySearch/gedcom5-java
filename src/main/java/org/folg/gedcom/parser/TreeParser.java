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

package org.folg.gedcom.parser;

import org.folg.gedcom.model.GedcomTag;
import org.gedml.GedcomParser;
import org.xml.sax.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Call parseGedcom to parse a gedcom file into a list of GedcomTag's
 * User: Dallan
 * Date: 12/23/11
 */
public class TreeParser implements ContentHandler, org.xml.sax.ErrorHandler {
   private static final Logger logger = LoggerFactory.getLogger("org.folg.gedcom.parser");

   private Locator locator;
   private GedcomTag tree;
   private Stack<GedcomTag> nodeStack;
   private ErrorHandler errorHandler = null;

   @Override
   public void setDocumentLocator(Locator locator) {
      this.locator = locator;
   }

   @Override
   public void startDocument() throws SAXException {
      tree = null;
      nodeStack = new Stack<GedcomTag>();
   }

   @Override
   public void endDocument() throws SAXException {
      // ignore
   }

   @Override
   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      // ignore
   }

   @Override
   public void endPrefixMapping(String prefix) throws SAXException {
      // ignore
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      GedcomTag node = new GedcomTag(atts.getValue("ID"), localName, atts.getValue("REF"));
      if (tree == null) {
         tree = node;
      }
      else {
         nodeStack.peek().addChild(node);
      }
      nodeStack.push(node);
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      nodeStack.pop();      
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      String s = new String(ch, start, length);
      GedcomTag tos = nodeStack.peek();
      tos.appendValue(s);
   }

   @Override
   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      // ignore
   }

   @Override
   public void processingInstruction(String target, String data) throws SAXException {
      // ignore
   }

   @Override
   public void skippedEntity(String name) throws SAXException {
      // ignore
   }

   @Override
   public void warning(SAXParseException exception) throws SAXException {
      if (errorHandler != null) {
         errorHandler.warning(exception.getMessage(), exception.getLineNumber());
      }
      else {
         logger.info(exception.getMessage() + " @ " + exception.getLineNumber());
      }
   }

   @Override
   public void error(SAXParseException exception) throws SAXException {
      if (errorHandler != null) {
         errorHandler.error(exception.getMessage(), exception.getLineNumber());
      }
      else {
         logger.warn(exception.getMessage() + " @ " + exception.getLineNumber());
      }
   }

   @Override
   public void fatalError(SAXParseException exception) throws SAXException {
      if (errorHandler != null) {
         errorHandler.fatalError(exception.getMessage(), exception.getLineNumber());
      }
      else {
         logger.error(exception.getMessage() + " @ " + exception.getLineNumber());
      }
   }

   public void setErrorHandler(ErrorHandler errorHandler) {
      this.errorHandler = errorHandler;
   }

   public List<GedcomTag> parseGedcom(File gedcomFile) throws SAXParseException, IOException {
      GedcomParser parser = gedcomParser();
      parser.parse(gedcomFile.toURI().toString());
      return tree.getChildren();
   }

   public List<GedcomTag> parseGedcom(InputStream is) throws SAXParseException, IOException {
      GedcomParser parser = gedcomParser();
      parser.parse(is);
      return tree.getChildren();
   }

   public List<GedcomTag> parseGedcom(Reader reader) throws SAXParseException, IOException {
      GedcomParser parser = gedcomParser();
      parser.parse(reader);
      return tree.getChildren();
   }

   private GedcomParser gedcomParser() {
      GedcomParser parser = new GedcomParser();
      parser.setContentHandler(this);
      parser.setErrorHandler(this);
      return parser;
   }

}

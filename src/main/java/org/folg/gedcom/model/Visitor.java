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

package org.folg.gedcom.model;

/**
 * User: Dallan
 * Date: 12/25/11
 * 
 *  If you override these functions, return false if you don't want to visit an object's children
 */
public class Visitor {
   public boolean visit(Address address) {return true;}
   public boolean visit(Association association) {return true;}
   public boolean visit(Change change) {return true;}
   public boolean visit(CharacterSet characterSet) {return true;}
   public boolean visit(ChildRef childRef) {return true;}
   public boolean visit(DateTime dateTime) {return true;}
   public boolean visit(EventFact eventFact) {return true;}
   public boolean visit(String extensionKey, Object extension) {return true;}
   public boolean visit(Family family) {return true;}
   public boolean visit(Gedcom gedcom) {return true;}
   public boolean visit(GedcomVersion gedcomVersion) {return true;}
   public boolean visit(Generator generator) {return true;}
   public boolean visit(GeneratorCorporation generatorCorporation) {return true;}
   public boolean visit(GeneratorData generatorData) {return true;}
   public boolean visit(Header header) {return true;}
   public boolean visit(LdsOrdinance ldsOrdinance) {return true;}
   public boolean visit(Media media) {return true;}
   public boolean visit(MediaRef mediaRef) {return true;}
   public boolean visit(Name name) {return true;}
   public boolean visit(Note note) {return true;}
   public boolean visit(NoteRef noteRef) {return true;}
   public boolean visit(ParentFamilyRef parentFamilyRef) {return true;}
   public boolean visit(ParentRelationship parentRelationship, boolean isFather) {return true;}
   public boolean visit(Person person) {return true;}
   public boolean visit(Repository repository) {return true;}
   public boolean visit(RepositoryRef repositoryRef) {return true;}
   public boolean visit(Source source) {return true;}
   public boolean visit(SourceCitation sourceCitation) {return true;}
   public boolean visit(SpouseRef spouseRef, boolean isHusband) {return true;}
   public boolean visit(SpouseFamilyRef spouseFamilyRef) {return true;}
   public boolean visit(Submission submission) {return true;}
   public boolean visit(Submitter submitter) {return true;}
   public void endVisit(ExtensionContainer obj) {}
}

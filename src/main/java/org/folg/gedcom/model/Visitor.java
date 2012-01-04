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
 */
public class Visitor {
   public void visit(Address address) {}
   public void visit(Change change) {}
   public void visit(CharacterSet characterSet) {}
   public void visit(ChildRef childRef) {}
   public void visit(DateTime dateTime) {}
   public void visit(EventFact eventFact) {}
   public void visit(String extensionKey, Object extension) {}
   public void visit(Family family) {}
   public void visit(SpouseRef spouseRef, boolean isHusband) {}
   public void visit(SpouseFamilyRef spouseFamilyRef) {}
   public void visit(Gedcom gedcom) {}
   public void visit(GedcomVersion gedcomVersion) {}
   public void visit(Generator generator) {}
   public void visit(GeneratorCorporation generatorCorporation) {}
   public void visit(GeneratorData generatorData) {}
   public void visit(Header header) {}
   public void visit(LdsOrdinance ldsOrdinance) {}
   public void visit(Media media) {}
   public void visit(Name name) {}
   public void visit(Note note) {}
   public void visit(NoteRef noteRef) {}
   public void visit(ParentFamilyRef parentFamilyRef) {}
   public void visit(ParentRelationship parentRelationship, boolean isFather) {}
   public void visit(Person person) {}
   public void visit(Repository repository) {}
   public void visit(RepositoryRef repositoryRef) {}
   public void visit(Source source) {}
   public void visit(SourceCitation sourceCitation) {}
   public void visit(Submission submission) {}
   public void visit(Submitter submitter) {}
   public void endVisit(ExtensionContainer obj) {}
}

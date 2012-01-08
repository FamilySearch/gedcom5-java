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

package org.folg.gedcom.visitors;

import org.folg.gedcom.model.*;
import org.folg.gedcom.model.Name;
import org.folg.gedcom.parser.GedcomTypeAdapter;
import org.gedml.GedcomParser;

import java.io.*;
import java.util.List;
import java.util.Stack;

/**
 * User: Dallan
 * Date: 12/25/11
 */
public class GedcomWriter extends Visitor {

   private Writer out = null;
   private String eol = "";
   private Stack<Object> stack;
   private IOException nestedException;

   public void write(Gedcom gedcom, File file) throws IOException {
      OutputStream out = new FileOutputStream(file);
      write(gedcom, out);
      out.close();
   }

   public void write(Gedcom gedcom, OutputStream out) throws IOException {
      stack = new Stack<Object>();
      nestedException = null;
      String charset = getCharsetName(gedcom);
      eol = (charset.equals("x-MacRoman") ? "\r" : "\n");
      this.out = new BufferedWriter(new OutputStreamWriter(out, charset));
      gedcom.accept(this);
      this.out.write("0 TRLR"+eol);
      this.out.flush();
      if (nestedException != null) {
         throw nestedException;
      }
   }

   private String getCharsetName(Gedcom gedcom) {
      Header header = gedcom.getHeader();
      String generator = (header != null && header.getGenerator() != null ? header.getGenerator().getName() : null);
      String encoding = (header != null && header.getCharacterSet() != null ? header.getCharacterSet().getValue() : null);
      String version = (header != null && header.getCharacterSet() != null ? header.getCharacterSet().getVersion() : null);
      String charset = GedcomParser.getJavaCharsetName(generator, encoding, version);
      if (charset.length() == 0) {
         charset = "UTF-8"; // default
      }
      return charset;
   }

   private void write(String tag, String id, String ref, String value) {
      try {
         int level = stack.size();
         out.write(level+" ");
         if (id != null && id.length() > 0) {
            out.write("@"+id+"@ ");
         }
         out.write(tag);
         if (ref != null && ref.length() > 0) {
            out.write(" @"+ref+"@");
         }
         if (value != null && value.length() > 0) {
            out.write(" ");
            String[] lines = value.split("\\n");
            for (int i = 0; i < lines.length; i++) {
               if (i > 0) {
                  out.write(eol+(level+1)+" CONT ");
               }
               while (lines[i].length() > 200) {
                  out.write(lines[i].substring(0,200));
                  lines[i] = lines[i].substring(200);
                  out.write(eol+(level+1)+" CONC ");
               }
               out.write(lines[i]);
            }
         }
         out.write(eol);
      } catch (IOException e) {
         nestedException = e;
      }
   }

   private void write(String tag) {
      write(tag, null, null, null);
   }

   private void write(String tag, String value) {
      write(tag, null, null, value);
   }

   private void writeString(String tag, String value) {
      if (value != null && value.length() > 0) {
         write(tag, value);
      }
   }

   @Override
   public boolean visit(Address address) {
      write("ADDR", address.getValue());
      stack.push(address);
      writeString("ADR1", address.getAddressLine1());
      writeString("ADR2", address.getAddressLine2());
      writeString("CITY", address.getCity());
      writeString("STAE", address.getState());
      writeString("POST", address.getPostalCode());
      writeString("CTRY", address.getCountry());
      writeString("_NAME", address.getName());
      return true;
   }

   @Override
   public boolean visit(Association association) {
      write("ASSO", null, association.getRef(), null);
      stack.push(association);
      writeString("TYPE", association.getType());
      writeString("RELA", association.getRelation());
      return true;
   }

   @Override
   public boolean visit(Change change) {
      write("CHAN");
      stack.push(change);
      return true;
   }

   @Override
   public boolean visit(CharacterSet characterSet) {
      write("CHAR", characterSet.getValue());
      stack.push(characterSet);
      writeString("VERS", characterSet.getVersion());
      return true;
   }

   @Override
   public boolean visit(ChildRef childRef) {
      write("CHIL", null, childRef.getRef(), null);
      stack.push(childRef);
      writeSpouseRefStrings(childRef);
      return true;
   }

   @Override
   public boolean visit(DateTime dateTime) {
      write("DATE", dateTime.getValue());
      stack.push(dateTime);
      writeString("TIME", dateTime.getTime());
      return true;
   }

   private void writeEventFactStrings(EventFact eventFact) {
      writeString("TYPE", eventFact.getType());
      writeString("DATE", eventFact.getDate());
      writeString("PLAC", eventFact.getPlace());
      writeString("RIN", eventFact.getRin());
      writeString(eventFact.getUidTag(), eventFact.getUid());
   }

   @Override
   public boolean visit(EventFact eventFact) {
      Person p = null;
      if (stack.peek() instanceof Person) {
         // look for another event with this tag as the cause-of-death tag
         p = (Person) stack.peek();
         for (EventFact ef : p.getEventsFacts()) {
            // if this event's tag == another event's cause-of-death tag, put this event under the other event
            if (eventFact.getTag().equals(ef.getCauseOfDeathTag())) {
               return false;
            }
         }
      }
      write(eventFact.getTag(), eventFact.getValue());
      stack.push(eventFact);
      writeEventFactStrings(eventFact);
      if (eventFact.getCauseOfDeathTag() != null && p != null) {
         for (EventFact ef : p.getEventsFacts()) {
            if (eventFact.getCauseOfDeathTag().equals(ef.getTag())) {
               ef.accept(this);
            }
         }
      }
      return true;
   }

   private void writeGedcomTag(GedcomTag tag) {
      write(tag.getTag(), tag.getId(), tag.getRef(), tag.getValue());
      stack.push(tag);
      for (GedcomTag child : tag.getChildren()) {
         writeGedcomTag(child);
      }
      stack.pop();
   }

   @Override
   public boolean visit(String extensionKey, Object extension) {
      if (GedcomTypeAdapter.MORE_TAGS_EXTENSION_KEY.equals(extensionKey)) {
         @SuppressWarnings("unchecked")
         List<GedcomTag> moreTags = (List<GedcomTag>)extension;
         for (GedcomTag tag : moreTags) {
            writeGedcomTag(tag);
         }
      }
      return true;
   }

   private void writePersonFamilyCommonContainerStrings(PersonFamilyCommonContainer pf) {
      for (String refn : pf.getReferenceNumbers()) {
         writeString("REFN", refn);
      }
      writeString("RIN", pf.getRin());
      writeString(pf.getUidTag(), pf.getUid());
   }

   @Override
   public boolean visit(Family family) {
      write("FAM", family.getId(), null, null);
      stack.push(family);
      writePersonFamilyCommonContainerStrings(family);
      return true;
   }

   @Override
   public boolean visit(Gedcom gedcom) {
      return true;
   }

   @Override
   public boolean visit(GedcomVersion gedcomVersion) {
      write("GEDC");
      stack.push(gedcomVersion);
      writeString("VERS", gedcomVersion.getVersion());
      writeString("FORM", gedcomVersion.getForm());
      return true;
   }

   @Override
   public boolean visit(Generator generator) {
      write("SOUR", generator.getValue());
      stack.push(generator);
      writeString("NAME", generator.getName());
      writeString("VERS", generator.getVersion());
      return true;
   }

   @Override
   public boolean visit(GeneratorCorporation generatorCorporation) {
      write("CORP", generatorCorporation.getValue());
      stack.push(generatorCorporation);
      writeString("PHON", generatorCorporation.getPhone());
      writeString(generatorCorporation.getWwwTag(), generatorCorporation.getWww());
      return true;
   }

   @Override
   public boolean visit(GeneratorData generatorData) {
      write("DATA", generatorData.getValue());
      stack.push(generatorData);
      writeString("DATE", generatorData.getDate());
      writeString("COPR", generatorData.getCopyright());
      return true;
   }

   @Override
   public boolean visit(Header header) {
      write("HEAD");
      stack.push(header);
      writeString("DEST", header.getDestination());
      writeString("FILE", header.getFile());
      writeString("COPR", header.getCopyright());
      writeString("LANG", header.getLanguage());
      if (header.getSubmitterRef() != null) {
         write("SUBM", null, header.getSubmitterRef(), null);
      }
      if (header.getSubmissionRef() != null) {
         write("SUBN", null, header.getSubmissionRef(), null);
      }
      return true;
   }

   @Override
   public boolean visit(LdsOrdinance ldsOrdinance) {
      write(ldsOrdinance.getTag(), ldsOrdinance.getValue());
      stack.push(ldsOrdinance);
      writeEventFactStrings(ldsOrdinance);
      writeString("STAT", ldsOrdinance.getStatus());
      writeString("TEMP", ldsOrdinance.getTemple());
      return true;
   }

   @Override
   public boolean visit(Media media) {
      write("OBJE", media.getId(), null, null);
      stack.push(media);
      writeString("FORM", media.getFormat());
      writeString("TITL", media.getTitle());
      writeString("BLOB", media.getBlob());
      writeString(media.getFileTag(), media.getFile());
      writeString("_PRIM", media.getPrimary());
      writeString("_TYPE", media.getType());
      writeString("_SCBK", media.getScrapbook());
      writeString("_SSHOW", media.getSlideShow());
      return true;
   }

   @Override
   public boolean visit(MediaRef mediaRef) {
      write("OBJE", null, mediaRef.getRef(), null);
      stack.push(mediaRef);
      return true;
   }

   @Override
   public boolean visit(Name name) {
      // handle ALIA and TITL names later by putting them under the main name
      if (("ALIA".equals(name.getType()) || "TITL".equals(name.getType())) &&
          !(stack.peek() instanceof Name)) {
         return false;
      }
      else {
         write("NAME", name.getValue());
         stack.push(name);
         writeString("GIVN", name.getGiven());
         writeString("SURN", name.getSurname());
         writeString("NPFX", name.getPrefix());
         writeString("NSFX", name.getSuffix());
         writeString("SPFX", name.getSurnamePrefix());
         writeString("NICK", name.getNickname());
         writeString("_TYPE", name.getType());
         writeString(name.getAkaTag(), name.getAka());
         writeString(name.getMarriedNameTag(), name.getMarriedName());
         if (stack.peek() instanceof Person) {
            // put ALIA and TITLE names here
            Person p = (Person)stack.peek();
            for (Name n : p.getNames()) {
               if ("ALIA".equals(name.getType()) || "TITL".equals(name.getType())) {
                  n.accept(this);
               }
            }
         }
         return true;
      }
   }

   @Override
   public boolean visit(Note note) {
      write("NOTE", note.getId(), null, note.getValue());
      stack.push(note);
      if (note.isSourceCitationsUnderValue()) {
         // yuck: handle Reunion broken citations: 0 NOTE 1 CONT ... 2 SOUR
         stack.push(new Object()); // increment level to 2
         for (SourceCitation sc : note.getSourceCitations()) {
            sc.accept(this);
         }
         stack.pop();
         // need to handle the rest of the children here too
         if (note.getChange() != null) {
            note.getChange().accept(this);
         }
      }
      writeString("RIN", note.getRin());
      return !note.isSourceCitationsUnderValue(); // already handled children if source citations are under value
   }

   @Override
   public boolean visit(NoteRef noteRef) {
      write("NOTE", null, noteRef.getRef(), null);
      stack.push(noteRef);
      return true;
   }

   @Override
   public boolean visit(ParentFamilyRef parentFamilyRef) {
      write("FAMC", null, parentFamilyRef.getRef(), null);
      stack.push(parentFamilyRef);
      writeSpouseFamilyRefStrings(parentFamilyRef);
      writeString("PEDI", parentFamilyRef.getRelationshipType());
      writeString("_PRIMARY", parentFamilyRef.getPrimary());
      return true;
   }

   @Override
   public boolean visit(ParentRelationship parentRelationship, boolean isFather) {
      write(isFather ? "_FREL" : "_MREL", parentRelationship.getValue());
      stack.push(parentRelationship);
      return true;
   }

   @Override
   public boolean visit(Person person) {
      write("INDI", person.getId(), null, null);
      stack.push(person);
      if (person.getAncestorInterestSubmitterRef() != null) {
         write("ANCI", null, person.getAncestorInterestSubmitterRef(), null);
      }
      if (person.getDescendantInterestSubmitterRef() != null) {
         write("DESI", null, person.getDescendantInterestSubmitterRef(), null);
      }
      writeString("RFN", person.getRecordFileNumber());
      writeString("PHON", person.getPhone());
      writeString(person.getEmailTag(), person.getEmail());
      writePersonFamilyCommonContainerStrings(person);
      return true;
   }

   @Override
   public boolean visit(Repository repository) {
      write("REPO", repository.getId(), null, repository.getValue());
      stack.push(repository);
      writeString("NAME", repository.getName());
      writeString("PHON", repository.getPhone());
      writeString("RIN", repository.getRin());
      writeString(repository.getEmailTag(), repository.getEmail());
      writeString(repository.getWwwTag(), repository.getWww());
      return true;
   }

   @Override
   public boolean visit(RepositoryRef repositoryRef) {
      write("REPO", null, repositoryRef.getRef(), repositoryRef.getValue());
      stack.push(repositoryRef);
      writeString("CALN", repositoryRef.getCallNumber());
      writeString("MEDI", repositoryRef.getMediaType());
      return true;
   }

   @Override
   public boolean visit(Source source) {
      write("SOUR", source.getId(), null, null);
      stack.push(source);
      writeString("AUTH", source.getAuthor());
      writeString("TITL", source.getTitle());
      writeString("ABBR", source.getAbbreviation());
      writeString("PUBL", source.getPublicationFacts());
      writeString("TEXT", source.getText());
      writeString("REFN", source.getReferenceNumber());
      writeString("RIN", source.getRin());
      writeString("MEDI", source.getMediaType());
      writeString("CALN", source.getCallNumber());
      writeString(source.getTypeTag(), source.getType());
      writeString(source.getUidTag(), source.getUid());
      writeString("_PAREN", source.getParen());
      writeString("_ITALIC", source.getItalic());
      writeString("DATE", source.getDate());
      return true;
   }

   @Override
   public boolean visit(SourceCitation sourceCitation) {
      write("SOUR", null, sourceCitation.getRef(), sourceCitation.getValue());
      stack.push(sourceCitation);
      writeString("PAGE", sourceCitation.getPage());
      writeString("DATE", sourceCitation.getDate());
      writeString("TEXT", sourceCitation.getText());
      writeString("QUAY", sourceCitation.getQuality());
      return true;
   }

   private void writeSpouseRefStrings(SpouseRef spouseRef) {
      writeString("_PREF", spouseRef.getPreferred());
   }

   @Override
   public boolean visit(SpouseRef spouseRef, boolean isHusband) {
      write(isHusband ? "HUSB" : "WIFE", null, spouseRef.getRef(), null);
      stack.push(spouseRef);
      writeSpouseRefStrings(spouseRef);
      return true;
   }

   private void writeSpouseFamilyRefStrings(SpouseFamilyRef spouseFamilyRef) {
      // nothing to write
   }

   @Override
   public boolean visit(SpouseFamilyRef spouseFamilyRef) {
      write("FAMS", null, spouseFamilyRef.getRef(), null);
      stack.push(spouseFamilyRef);
      writeSpouseFamilyRefStrings(spouseFamilyRef);
      return true;
   }

   @Override
   public boolean visit(Submission submission) {
      write("SUBN", submission.getId(), null, null);
      stack.push(submission);
      writeString("DESC", submission.getDescription());
      writeString("ORDI", submission.getOrdinanceFlag());
      return true;
   }

   @Override
   public boolean visit(Submitter submitter) {
      write("SUBM", submitter.getId(), null, submitter.getValue());
      stack.push(submitter);
      writeString("PHON", submitter.getPhone());
      writeString("NAME", submitter.getName());
      writeString("RIN", submitter.getRin());
      writeString("LANG", submitter.getLanguage());
      writeString(submitter.getWwwTag(), submitter.getWww());
      writeString(submitter.getEmailTag(), submitter.getEmail());
      return true;
   }

   @Override
   public void endVisit(ExtensionContainer obj) {
      if (!(obj instanceof Gedcom)) {
         stack.pop();
      }
   }
}

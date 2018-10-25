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
import org.folg.gedcom.parser.ModelParser;
import org.gedml.AnselOutputStreamWriter;
import org.gedml.GedcomParser;

import java.io.*;
import java.util.List;
import java.util.Stack;

/**
 * User: Dallan
 * Date: 12/25/11
 *
 * Export a model as GEDCOM
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
      OutputStreamWriter writer;
      if ("ANSEL".equals(charset)) {
         writer = new AnselOutputStreamWriter(out);
      }
      else {
         writer = new OutputStreamWriter(out, charset);
      }
      this.out = new BufferedWriter(writer);
      gedcom.accept(this);
      this.out.write("0 TRLR"+eol);
      this.out.flush();
      if (nestedException != null) {
         throw nestedException;
      }
   }

   private String getCharsetName(Gedcom gedcom) {
      Header header = gedcom.getHeader();
      String generator = (header != null && header.getGenerator() != null ? header.getGenerator().getValue() : null);
      String charset = (header != null && header.getCharacterSet() != null ? header.getCharacterSet().getValue() : null);
      String version = (header != null && header.getCharacterSet() != null ? header.getCharacterSet().getVersion() : null);
      charset = GedcomParser.getCorrectedCharsetName(generator, charset, version);
      if (charset.length() == 0) {
         charset = "UTF-8"; // default
      }
      return charset;
   }

   private void write(String tag, String id, String ref, String value, boolean forceValueOnSeparateLine) {
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
            if (forceValueOnSeparateLine && !value.startsWith("\n")) {
               out.write(eol+(level+1)+" CONC ");
            }
            else {
               out.write(" ");
            }
            StringBuilder buf = new StringBuilder(value);
            int cnt = 0;
            boolean lastLine = false;
            while (!lastLine) {
               int nlPos = buf.indexOf("\n");
               String line;
               if (nlPos >= 0) {
                  line = buf.substring(0, nlPos);
                  buf.delete(0,nlPos+1);
               }
               else {
                  line = buf.toString();
                  lastLine = true;
               }
               if (cnt > 0) {
                  out.write(eol+(level+1)+" CONT ");
               }
               while (line.length() > 200) {
                  out.write(line.substring(0,200));
                  line = line.substring(200);
                  out.write(eol+(level+1)+" CONC ");
               }
               out.write(line);
               cnt++;
            }
         }
         out.write(eol);
      } catch (IOException e) {
         nestedException = e;
      }
   }

   private void write(String tag, String id, String ref, String value) {
      write(tag, id, ref, value, false);
   }

   private void write(String tag) {
      write(tag, null, null, null);
   }

   private void write(String tag, String value) {
      write(tag, null, null, value);
   }

   private void writeFieldExtensions(String fieldName, ExtensionContainer ec) {
      @SuppressWarnings("unchecked")
      List<GedcomTag> moreTags = (List<GedcomTag>)ec.getExtension(ModelParser.MORE_TAGS_EXTENSION_KEY);
      if (moreTags != null) {
         for (GedcomTag tag : moreTags) {
            if (fieldName.equals(tag.getParentTagName())) {
               stack.push(new Object()); // placeholder
               writeGedcomTag(tag);
               stack.pop();
            }
         }
      }
   }

   private void writeString(String tag, ExtensionContainer ec, String value) {
      if (value != null && value.length() > 0) {
         write(tag, value);
			writeFieldExtensions(tag, ec);
      }
   }
   private void writeRef(String tag, ExtensionContainer ec, String ref) {
      if (ref != null && ref.length() > 0) {
         write(tag, null, ref, null);
         writeFieldExtensions(tag, ec);
      }
   }

   @Override
   public boolean visit(Address address) {
      write("ADDR", address.getValue());
      stack.push(address);
      writeString("ADR1", address, address.getAddressLine1());
      writeString("ADR2", address, address.getAddressLine2());
      writeString("CITY", address, address.getCity());
      writeString("STAE", address, address.getState());
      writeString("POST", address, address.getPostalCode());
      writeString("CTRY", address, address.getCountry());
      writeString("_NAME", address, address.getName());
      return true;
   }

   @Override
   public boolean visit(Association association) {
      write("ASSO", null, association.getRef(), null);
      stack.push(association);
      writeString("TYPE", association, association.getType());
      writeString("RELA", association, association.getRelation());
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
      writeString("VERS", characterSet, characterSet.getVersion());
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
      writeString("TIME", dateTime, dateTime.getTime());
      return true;
   }

   private void writeEventFactStrings(EventFact eventFact) {
      writeString("TYPE", eventFact, eventFact.getType());
      writeString("DATE", eventFact, eventFact.getDate());
      writeString("PLAC", eventFact, eventFact.getPlace());
      writeString("CAUS", eventFact, eventFact.getCause());
      writeString("RIN", eventFact, eventFact.getRin());
      writeString(eventFact.getUidTag(), eventFact, eventFact.getUid());
   }

   @Override
   public boolean visit(EventFact eventFact) {
      write(eventFact.getTag(), eventFact.getValue());
      stack.push(eventFact);
      writeEventFactStrings(eventFact);
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
      if (ModelParser.MORE_TAGS_EXTENSION_KEY.equals(extensionKey)) {
         @SuppressWarnings("unchecked")
         List<GedcomTag> moreTags = (List<GedcomTag>)extension;
         for (GedcomTag tag : moreTags) {
            if (tag.getParentTagName() == null) { // if field name is not null, the extension should have been written already
               writeGedcomTag(tag);
            }
         }
      }
      return true;
   }

   private void writePersonFamilyCommonContainerStrings(PersonFamilyCommonContainer pf) {
      for (String refn : pf.getReferenceNumbers()) {
         // it's a problem if there's multiple refns with sub-tags
         writeString("REFN", pf, refn);
      }
      writeString("RIN", pf, pf.getRin());
      writeString(pf.getUidTag(), pf, pf.getUid());
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
      writeString("VERS", gedcomVersion, gedcomVersion.getVersion());
      writeString("FORM", gedcomVersion, gedcomVersion.getForm());
      return true;
   }

   @Override
   public boolean visit(Generator generator) {
      write("SOUR", generator.getValue());
      stack.push(generator);
      writeString("NAME", generator, generator.getName());
      writeString("VERS", generator, generator.getVersion());
      return true;
   }

   @Override
   public boolean visit(GeneratorCorporation generatorCorporation) {
      write("CORP", generatorCorporation.getValue());
      stack.push(generatorCorporation);
      writeString("PHON", generatorCorporation, generatorCorporation.getPhone());
      writeString(generatorCorporation.getWwwTag(), generatorCorporation, generatorCorporation.getWww());
      return true;
   }

   @Override
   public boolean visit(GeneratorData generatorData) {
      write("DATA", generatorData.getValue());
      stack.push(generatorData);
      writeString("DATE", generatorData, generatorData.getDate());
      writeString("COPR", generatorData, generatorData.getCopyright());
      return true;
   }

   @Override
   public boolean visit(Header header) {
      write("HEAD");
      stack.push(header);
      writeString("DEST", header, header.getDestination());
      writeString("FILE", header, header.getFile());
      writeString("COPR", header, header.getCopyright());
      writeString("LANG", header, header.getLanguage());
      writeRef("SUBM", header, header.getSubmitterRef());
      writeRef("SUBN", header, header.getSubmissionRef());
      return true;
   }

   @Override
   public boolean visit(LdsOrdinance ldsOrdinance) {
      write(ldsOrdinance.getTag(), ldsOrdinance.getValue());
      stack.push(ldsOrdinance);
      writeEventFactStrings(ldsOrdinance);
      writeString("STAT", ldsOrdinance, ldsOrdinance.getStatus());
      writeString("TEMP", ldsOrdinance, ldsOrdinance.getTemple());
      return true;
   }

   @Override
   public boolean visit(Media media) {
      write("OBJE", media.getId(), null, null);
      stack.push(media);
      writeString("FORM", media, media.getFormat());
      writeString("TITL", media, media.getTitle());
      writeString("BLOB", media, media.getBlob());
      writeString(media.getFileTag(), media, media.getFile());
      writeString("_PRIM", media, media.getPrimary());
      writeString("_TYPE", media, media.getType());
      writeString("_SCBK", media, media.getScrapbook());
      writeString("_SSHOW", media, media.getSlideShow());
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
      // handle ALIA name by recording it with that tag
      String tag;
      String type = name.getType();
      if ("ALIA".equals(type)) {
         tag = type;
         type = null;
      }
      else {
         tag = "NAME";
      }
      write(tag, name.getValue());
      stack.push(name);
      writeString("GIVN", name, name.getGiven());
      writeString("SURN", name, name.getSurname());
      writeString("NPFX", name, name.getPrefix());
      writeString("NSFX", name, name.getSuffix());
      writeString("SPFX", name, name.getSurnamePrefix());
      writeString("NICK", name, name.getNickname());
      writeString(name.getTypeTag(), name, type);
      writeString(name.getAkaTag(), name, name.getAka());
      writeString(name.getMarriedNameTag(), name, name.getMarriedName());
      return true;
   }

   @Override
   public boolean visit(Note note) {
      boolean visitChildren = false;
      if (note.isSourceCitationsUnderValue() && note.getSourceCitations().size() > 0 &&
          note.getValue() != null && note.getValue().length() > 0) {
         // yuck: handle Reunion broken citations: 0 NOTE 1 CONT ... 2 SOUR; also Easytree: 0 NOTE 1 CONC ... 2 SOUR
         write("NOTE", note.getId(), null, note.getValue(), true);
         stack.push(note);
         stack.push(new Object()); // increment level to 2
         for (SourceCitation sc : note.getSourceCitations()) {
            sc.accept(this);
         }
         stack.pop();
         visitChildren = true;
      }
      else {
         write("NOTE", note.getId(), null, note.getValue());
         stack.push(note);
      }

      // write note strings
      writeString("RIN", note, note.getRin());

      if (visitChildren) {
         // if we return false below we need to visit the rest of the children and pop the stack here
         note.visitContainedObjects(this, false);
         stack.pop();
      }
      return !visitChildren;
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
      writeString("PEDI", parentFamilyRef, parentFamilyRef.getRelationshipType());
      writeString("_PRIMARY", parentFamilyRef, parentFamilyRef.getPrimary());
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
      writeRef("ANCI", person, person.getAncestorInterestSubmitterRef());
      writeRef("DESI", person, person.getDescendantInterestSubmitterRef());
      writeString("RFN", person, person.getRecordFileNumber());
      writeString("PHON", person, person.getPhone());
      writeString(person.getEmailTag(), person, person.getEmail());
      writePersonFamilyCommonContainerStrings(person);
      return true;
   }

   @Override
   public boolean visit(Repository repository) {
      write("REPO", repository.getId(), null, repository.getValue());
      stack.push(repository);
      writeString("NAME", repository, repository.getName());
      writeString("PHON", repository, repository.getPhone());
      writeString("RIN", repository, repository.getRin());
      writeString(repository.getEmailTag(), repository, repository.getEmail());
      writeString(repository.getWwwTag(), repository, repository.getWww());
      return true;
   }

   @Override
   public boolean visit(RepositoryRef repositoryRef) {
      write("REPO", null, repositoryRef.getRef(), repositoryRef.getValue());
      stack.push(repositoryRef);
      if (repositoryRef.isMediUnderCalnTag() ||
              (repositoryRef.getCallNumber() != null && repositoryRef.getCallNumber().length() > 0)) {
         write("CALN", repositoryRef.getCallNumber());
      }
      if (repositoryRef.isMediUnderCalnTag()) {
         stack.push(new Object()); // placeholder
      }
      writeString("MEDI", repositoryRef, repositoryRef.getMediaType());
      if (repositoryRef.isMediUnderCalnTag()) {
         stack.pop();
      }

      return true;
   }

   @Override
   public boolean visit(Source source) {
      write("SOUR", source.getId(), null, null);
      stack.push(source);
      writeString("AUTH", source, source.getAuthor());
      writeString("TITL", source, source.getTitle());
      writeString("ABBR", source, source.getAbbreviation());
      writeString("PUBL", source, source.getPublicationFacts());
      writeString("TEXT", source, source.getText());
      writeString("REFN", source, source.getReferenceNumber());
      writeString("RIN", source, source.getRin());
      writeString("MEDI", source, source.getMediaType());
      writeString("CALN", source, source.getCallNumber());
      writeString(source.getTypeTag(), source, source.getType());
      writeString(source.getUidTag(), source, source.getUid());
      writeString("_PAREN", source, source.getParen());
      writeString("_ITALIC", source, source.getItalic());
      writeString("DATE", source, source.getDate());
      return true;
   }

   private void writeUnderData(String tag, SourceCitation sourceCitation, String value) {
      if (value != null && value.length() > 0) {
         write("DATA");
         stack.push(new Object()); // placeholder
         writeString(tag, sourceCitation, value);
         stack.pop();
      }
   }
   @Override
   public boolean visit(SourceCitation sourceCitation) {
      write("SOUR", null, sourceCitation.getRef(), sourceCitation.getValue());
      stack.push(sourceCitation);
      writeString("PAGE", sourceCitation, sourceCitation.getPage());
      writeString("QUAY", sourceCitation, sourceCitation.getQuality());
      if (sourceCitation.getDataTagContents() == SourceCitation.DataTagContents.COMBINED &&
          (sourceCitation.getDate() != null && sourceCitation.getDate().length() > 0 ||
           sourceCitation.getText() != null && sourceCitation.getText().length() > 0)) {
         write("DATA");
         stack.push(new Object()); // placeholder
         writeString("DATE", sourceCitation, sourceCitation.getDate());
         writeString("TEXT", sourceCitation, sourceCitation.getText());
         stack.pop();
      }
      else if (sourceCitation.getDataTagContents() == SourceCitation.DataTagContents.DATE) {
         writeUnderData("DATE", sourceCitation, sourceCitation.getDate());
         writeString("TEXT", sourceCitation, sourceCitation.getText());
      }
      else if (sourceCitation.getDataTagContents() == SourceCitation.DataTagContents.TEXT) {
         writeUnderData("TEXT", sourceCitation, sourceCitation.getText());
         writeString("DATE", sourceCitation, sourceCitation.getDate());
      }
      else if (sourceCitation.getDataTagContents() == SourceCitation.DataTagContents.SEPARATE) {
         writeUnderData("DATE", sourceCitation, sourceCitation.getDate());
         writeUnderData("TEXT", sourceCitation, sourceCitation.getText());
      }
      else if (sourceCitation.getDataTagContents() == null) {
         writeString("DATE", sourceCitation, sourceCitation.getDate());
         writeString("TEXT", sourceCitation, sourceCitation.getText());
      }
      return true;
   }

   private void writeSpouseRefStrings(SpouseRef spouseRef) {
      writeString("_PREF", spouseRef, spouseRef.getPreferred());
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
      writeString("DESC", submission, submission.getDescription());
      writeString("ORDI", submission, submission.getOrdinanceFlag());
      return true;
   }

   @Override
   public boolean visit(Submitter submitter) {
      write("SUBM", submitter.getId(), null, submitter.getValue());
      stack.push(submitter);
      writeString("PHON", submitter, submitter.getPhone());
      writeString("NAME", submitter, submitter.getName());
      writeString("RIN", submitter, submitter.getRin());
      writeString("LANG", submitter, submitter.getLanguage());
      writeString(submitter.getWwwTag(), submitter, submitter.getWww());
      writeString(submitter.getEmailTag(), submitter, submitter.getEmail());
      return true;
   }

   @Override
   public void endVisit(ExtensionContainer obj) {
      if (!(obj instanceof Gedcom)) {
         stack.pop();
      }
   }
}

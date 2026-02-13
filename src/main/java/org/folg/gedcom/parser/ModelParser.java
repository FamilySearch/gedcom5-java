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

import org.folg.gedcom.model.*;
import org.gedml.GedcomParser;
import org.xml.sax.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Dallan
 * Date: 12/25/11
 */
public class ModelParser implements ContentHandler, org.xml.sax.ErrorHandler {
   private static final Logger logger = LoggerFactory.getLogger("org.folg.gedcom.parser");
   public static final String MORE_TAGS_EXTENSION_KEY = "folg.more_tags";
   private Locator locator;

   private Stack<String> tagStack;
   private Stack<Object> objectStack;
   private Gedcom gedcom;
   private ErrorHandler errorHandler = null;

   private String joinTagStack() {
      StringBuilder buf = new StringBuilder();
      for (int i = 1; i < tagStack.size(); i++) {
         if (i > 1) {
            buf.append(' ');
         }
         buf.append(tagStack.get(i));
      }
      return buf.toString();
   }

   @Override
   public void setDocumentLocator(Locator locator) {
      this.locator = locator;
   }

   @Override
   public void startDocument() throws SAXException {
      gedcom = null;
      tagStack = new Stack<String>();
      objectStack = new Stack<Object>();
      
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
   
   public static enum Tag {
      ABBR, ADDR, ADR1, ADR2, ADR3, AGE, _AKA, ALIA, ANCI, ASSO, AUTH,
      BLOB,
      CALN, CAUS, CHAN, CHAR, CHIL, CITY, CONC, CONT, COPR, CORP, CTRY,
      DATA, DATE, DESC, DESI, DEST,
      EMAIL, _EMAIL, _EML,
      FAM, FAMC, FAMS, FAX, _FILE, FILE, FONE, FORM, _FREL,
      GED, GEDC, GIVN,
      HEAD, HUSB,
      INDI, _ITALIC,
      LANG,
      _MARRNM, _MAR, _MARNM, MEDI, _MREL,
      NAME, _NAME, NICK, NOTE, NPFX, NSFX,
      OBJE, ORDI,
      PAGE, _PAREN, PEDI, PHON, POST, PLAC, _PREF, _PRIM, _PRIMARY, PUBL,
      QUAY,
      REFN, RELA, REPO, RFN, RIN, ROMN,
      _SCBK, SOUR, SPFX, _SSHOW, STAE, STAT, SUBM, SUBN, SURN,
      TEMP, TEXT, TIME, TITL, TRLR, TYPE, _TYPE,
      UID, _UID, _URL,
      VERS,
      WIFE, _WEB, WWW, _WWW,
      
      // personal LDS ordinances
      BAPL, CONL, WAC, ENDL, SLGC,
      // family LDS ordinances
      SLGS
   }

   public void startElement(String uri, String tagName, String qName, Attributes atts) throws SAXException {
      String tagNameUpper = tagName.toUpperCase();
      String id = atts.getValue("ID");
      String ref = atts.getValue("REF");
      Object tos = objectStack.size() > 0 ? objectStack.peek() : null;
      Object obj = null;
      
      try { 
         Tag tag = Tag.valueOf(tagNameUpper);
         switch (tag) {
            case ABBR:
               obj = handleAbbr(tos);
               break;
            case ADDR:
               obj = handleAddr(tos);
               break;
            case ADR1:
               obj = handleAdr1(tos);
               break;
            case ADR2:
               obj = handleAdr2(tos);
               break;
            case ADR3:
               obj = handleAdr3(tos);
               break;
            case AGE:
               obj = handleAge(tos);
               break;
           case _AKA:
               obj = handleAka(tos, tagName);
               break;
            case ALIA:
               obj = handleAlia(tos, ref);
               break;
            case ANCI:
               obj = handleAnci(tos, ref);
               break;
            case ASSO:
               obj = handleAsso(tos, ref);
               break;
            case AUTH:
               obj = handleAuth(tos);
               break;
            case BLOB:
               obj = handleBlob(tos);
               break;
            case CALN:
               obj = handleCaln(tos);
               break;
            case CAUS:
                obj = handleCaus(tos);
                break;
            case CHAN:
               obj = handleChan(tos);
               break;
            case CHAR:
               obj = handleChar(tos);
               break;
            case CHIL:
               obj = handleChil(tos, ref);
               break;
            case CITY:
               obj = handleCity(tos);
               break;
            case CONC:
               obj = handleCont(tos, false);
               break;
            case CONT:
               obj = handleCont(tos, true);
               break;
            case COPR:
               obj = handleCopr(tos);
               break;
            case CORP:
               obj = handleCorp(tos);
               break;
            case CTRY:
               obj = handleCtry(tos);
               break;
            case DATA:
               obj = handleData(tos);
               break;
            case DATE:
               obj = handleDate(tos);
               break;
            case DESC:
               obj = handleDesc(tos);
               break;
            case DESI:
               obj = handleDesi(tos, ref);
               break;
            case DEST:
               obj = handleDest(tos);
               break;
            case EMAIL:
            case _EMAIL:
            case _EML:
               obj = handleEmail(tos, tagName);
               break;
            case FAM:
               obj = handleFam(tos, id);
               break;
            case FAMC:
               obj = handleFamc(tos, ref);
               break;
            case FAMS:
               obj = handleFams(tos, ref);
               break;
            case FAX:
               obj = handleFax(tos);
               break;
            case _FILE:
            case FILE:
               obj = handleFile(tos, tagName);
               break;
           case FONE:
             obj = handleFone(tos, tagName);
             break;
           case FORM:
               obj = handleForm(tos);
               break;
            case _FREL:
               obj = handleFRel(tos);
               break;
            case GED:
               obj = handleGed();
               break;
            case GEDC:
               obj = handleGedc(tos);
               break;
            case GIVN:
               obj = handleGivn(tos);
               break;
            case HEAD:
               obj = handleHead(tos);
               break;
            case HUSB:
               obj = handleHusb(tos, ref);
               break;
            case INDI:
               obj = handleIndi(tos, id);
               break;
            case _ITALIC:
               obj = handleItalic(tos);
               break;
            case LANG:
               obj = handleLang(tos);
               break;
            case _MARRNM:
            case _MARNM:
            case _MAR:
               obj = handleMarrnm(tos, tagName);
               break;
            case MEDI:
               obj = handleMedi(tos);
               break;
            case _MREL:
               obj = handleMRel(tos);
               break;
            case NAME:
            case _NAME:
               obj = handleName(tos);
               break;
            case NICK:
               obj = handleNick(tos);
               break;
            case NPFX:
               obj = handleNpfx(tos);
               break;
            case NSFX:
               obj = handleNsfx(tos);
               break;
            case NOTE:
               obj = handleNote(tos, id, ref);
               break;
            case OBJE:
               obj = handleObje(tos, id, ref);
               break;
            case ORDI:
               obj = handleOrdi(tos);
               break;
            case PAGE:
               obj = handlePage(tos);
               break;
            case _PAREN:
               obj = handleParen(tos);
               break;
            case PEDI:
               obj = handlePedi(tos);
               break;
            case PHON:
               obj = handlePhon(tos);
               break;
            case PLAC:
               obj = handlePlac(tos);
               break;
            case POST:
               obj = handlePost(tos);
               break;
            case _PREF:
               obj = handlePref(tos);
               break;
            case _PRIM:
            case _PRIMARY:
               obj = handlePrim(tos);
               break;
            case PUBL:
               obj = handlePubl(tos);
               break;
            case QUAY:
               obj = handleQuay(tos);
               break;
            case REFN:
               obj = handleRefn(tos);
               break;
            case RELA:
               obj = handleRela(tos);
               break;
            case REPO:
               obj = handleRepo(tos, id, ref);
               break;
            case RFN:
               obj = handleRfn(tos);
               break;
            case RIN:
               obj = handleRin(tos);
               break;
           case ROMN:
             obj = handleRomn(tos, tagName);
             break;
           case _SCBK:
               obj = handleScbk(tos);
               break;
            case SOUR:
               obj = handleSour(tos, id, ref);
               break;
            case SPFX:
               obj = handleSpfx(tos);
               break;
            case _SSHOW:
               obj = handleSshow(tos);
               break;
            case STAE:
               obj = handleStae(tos);
               break;
            case STAT:
               obj = handleStat(tos);
               break;
            case SUBM:
               obj = handleSubm(tos, id, ref);
               break;
            case SUBN:
               obj = handleSubn(tos, id, ref);
               break;
            case SURN:
               obj = handleSurn(tos);
               break;
            case TEMP:
               obj = handleTemp(tos);
               break;
            case TEXT:
               obj = handleText(tos);
               break;
            case TIME:
               obj = handleTime(tos);
               break;
            case TITL:
               obj = handleTitl(tos);
               break;
            case TRLR:
               obj = handleTrlr(tos);
               break;
            case TYPE:
            case _TYPE:
               obj = handleType(tos, tagName);
               break;
            case VERS:
               obj = handleVers(tos);
               break;
            case _UID:
            case UID:
               obj = handleUid(tos, tagName);
               break;
            case WIFE:
               obj = handleWife(tos, ref);
               break;
            case WWW:
            case _WWW:
            case _WEB:
            case _URL:
               obj = handleWww(tos, tagName);
               break;
            // lds ordinance tags
            case BAPL:
            case CONL:
            case WAC:
            case ENDL:
            case SLGC:
               obj = handleLdsOrdinance(tos, true, tagNameUpper);
               break;
            case SLGS:
               obj = handleLdsOrdinance(tos, false, tagNameUpper);
               break;
            
            default:
               throw new SAXParseException("handler not found for tag: "+tag.name(), locator);
         }
      } catch (IllegalArgumentException e) {
         // handle events/facts below
      }
      if (obj == null) {
         obj = handleEventFact(tos, tagName, tagNameUpper);
      }

      if (obj == null) {
         // unexpected tag
         obj = new GedcomTag(id, tagName, ref);
         if (tos instanceof ExtensionContainer) {
            addGedcomTag((ExtensionContainer) tos, (GedcomTag) obj);
         }
         else if (tos instanceof GedcomTag) {
            ((GedcomTag)tos).addChild((GedcomTag)obj);
         }
         else if (tos instanceof FieldRef && ((FieldRef)tos).getTarget() instanceof ExtensionContainer) {
            ((GedcomTag)obj).setParentTagName(tagStack.peek());
            ExtensionContainer ec = (ExtensionContainer)((FieldRef)tos).getTarget();
            addGedcomTag(ec, (GedcomTag)obj);
         }
         else {
            error(new SAXParseException("Dropped tag: "+joinTagStack()+" "+tagName, locator));
         }
      }

      tagStack.push(tagName);
      objectStack.push(obj);
   }

   private void addGedcomTag(ExtensionContainer ec, GedcomTag tag) throws SAXException {
      @SuppressWarnings("unchecked")
      List<GedcomTag> moreTags = (List<GedcomTag>)ec.getExtension(MORE_TAGS_EXTENSION_KEY);
      if (moreTags == null) {
         moreTags = new ArrayList<GedcomTag>();
         ec.putExtension(MORE_TAGS_EXTENSION_KEY, moreTags);
      }
      moreTags.add(tag);
      warning(new SAXParseException("Tag added as extension: "+joinTagStack()+" "+tag.getTag(), locator));
   }

   private Object handleAbbr(Object tos) {
      if (tos instanceof Source && ((Source)tos).getAbbreviation() == null) {
         return new FieldRef(tos, "Abbreviation");
      }
      return null;
   }

   private Object handleAddr(Object tos) {
      if ((tos instanceof GeneratorCorporation && ((GeneratorCorporation)tos).getAddress() == null) ||
          (tos instanceof EventFact && ((EventFact)tos).getAddress() == null) ||
          (tos instanceof Person && ((Person)tos).getAddress() == null) ||
          (tos instanceof Repository && ((Repository)tos).getAddress() == null) ||
          (tos instanceof Submitter && ((Submitter)tos).getAddress() == null)) {
         Address address = new Address();
         if (tos instanceof GeneratorCorporation) {
            ((GeneratorCorporation)tos).setAddress(address);
         }
         else if (tos instanceof EventFact) {
            ((EventFact)tos).setAddress(address);
         }
         else if (tos instanceof Person) {
            ((Person)tos).setAddress(address);
         }
         else if (tos instanceof Repository) {
            ((Repository)tos).setAddress(address);
         }
         else {
            ((Submitter)tos).setAddress(address);
         }
         return address;
      }
      return null;
   }

   private Object handleAdr1(Object tos) {
      if (tos instanceof Address && ((Address)tos).getAddressLine1() == null) {
         return new FieldRef(tos, "AddressLine1");
      }
      return null;
   }

   private Object handleAdr2(Object tos) {
      if (tos instanceof Address && ((Address)tos).getAddressLine2() == null) {
         return new FieldRef(tos, "AddressLine2");
      }
      return null;
   }

   private Object handleAdr3(Object tos) {
      if (tos instanceof Address && ((Address)tos).getAddressLine3() == null) {
         return new FieldRef(tos, "AddressLine3");
      }
      return null;
   }

   private Object handleAge(Object tos) {
      if (tos instanceof EventFact && ((EventFact)tos).getAge() == null ||
          tos instanceof Spouse && ((Spouse)tos).getAge() == null) {
         return new FieldRef(tos, "Age");
      }
      return null;
   }

   private Object handleAka(Object tos, String tagName) {
      if (tos instanceof Name && ((Name)tos).getAka() == null) {
         ((Name)tos).setAkaTag(tagName);
         return new FieldRef(tos, "Aka");
      }
      return null;
   }

  private Object handleAlia(Object tos, String ref) {
      // we don't handle the standard ALIA @ref@ form, because nobody writes it that way
      // make it a name so it can have source citations
      if (tos instanceof Person && ref == null) {
         Name name = new Name();
         name.setType("ALIA");
         ((Person)tos).addName(name);
         return name;
      }
      return null;
   }

   private Object handleAnci(Object tos, String ref) {
      if (tos instanceof Person && ((Person)tos).getAncestorInterestSubmitterRef() == null && ref != null) {
         ((Person)tos).setAncestorInterestSubmitterRef(ref);
         return new FieldRef(tos, "AncestorInterestSubmitterRef");
      }
      return null;
   }

   private Object handleAsso(Object tos, String ref) {
      if (tos instanceof Person) {
         Association association = new Association();
         if (ref != null) {
            association.setRef(ref);
         }
         ((Person)tos).addAssociation(association);
         return association;
      }
      return null;
   }

   private Object handleAuth(Object tos) {
      if (tos instanceof Source && ((Source)tos).getAuthor() == null) {
         return new FieldRef(tos, "Author");
      }
      return null;
   }

   private Object handleBlob(Object tos) {
      if (tos instanceof Media && ((Media)tos).getBlob() == null) {
         return new FieldRef(tos, "Blob");
      }
      return null;
   }

   private Object handleCaln(Object tos) {
      if ((tos instanceof RepositoryRef && ((RepositoryRef)tos).getCallNumber() == null) ||
          (tos instanceof Source && ((Source)tos).getCallNumber() == null)) {
         return new FieldRef(tos, "CallNumber");
      }
      return null;
   }

   private Object handleCaus(Object tos) {
      if (tos instanceof EventFact && ((EventFact)tos).getCause() == null) {
         return new FieldRef(tos, "Cause");
      }
      return null;
   }

   private Object handleChan(Object tos) {
      if ((tos instanceof PersonFamilyCommonContainer && ((PersonFamilyCommonContainer)tos).getChange() == null) ||
          (tos instanceof Media && ((Media)tos).getChange() == null) ||
          (tos instanceof Note && ((Note)tos).getChange() == null) ||
          (tos instanceof Source && ((Source)tos).getChange() == null) ||
          (tos instanceof Repository && ((Repository)tos).getChange() == null) ||
          (tos instanceof Submitter && ((Submitter)tos).getChange() == null)) {
         Change change = new Change();
         if (tos instanceof PersonFamilyCommonContainer) {
            ((PersonFamilyCommonContainer)tos).setChange(change);
         }
         else if (tos instanceof Media) {
            ((Media)tos).setChange(change);
         }
         else if (tos instanceof Note) {
            ((Note)tos).setChange(change);
         }
         else if (tos instanceof Source) {
            ((Source)tos).setChange(change);
         }
         else if (tos instanceof Repository) {
            ((Repository)tos).setChange(change);
         }
         else {
            ((Submitter)tos).setChange(change);
         }
         return change;
      }
      return null;
   }

   private Object handleChar(Object tos) {
      if (tos instanceof Header && ((Header)tos).getCharacterSet() == null) {
         CharacterSet charset = new CharacterSet();
         ((Header)tos).setCharacterSet(charset);
         return charset;
      }
      return null;
   }
   
   private Object handleChil(Object tos, String ref) {
      if (tos instanceof Family) {
         ChildRef childRef = new ChildRef();
         childRef.setRef(ref);
         ((Family)tos).addChild(childRef);
         return childRef;
      }
      return null;
   }

   private Object handleCity(Object tos) {
      if (tos instanceof Address && ((Address)tos).getCity() == null) {
         return new FieldRef(tos, "City");
      }
      return null;
   }

   private Object handleCont(Object tos, boolean insertNewLine) throws SAXException {
      FieldRef fieldRef;
      if (tos instanceof FieldRef) {
         fieldRef = (FieldRef)tos;
      }
      else {
         fieldRef = new FieldRef(tos, "Value");
      }

      if (insertNewLine) {
         try {
            fieldRef.appendValue("\n");
         } catch (NoSuchMethodException e) {
            SAXParseException exception = new SAXParseException("value not stored for: "+joinTagStack(), locator);
            error(exception);
         }
      }
      return fieldRef;
   }

   private Object handleCopr(Object tos) {
      if ((tos instanceof Header && ((Header)tos).getCopyright() == null) ||
          (tos instanceof GeneratorData && ((GeneratorData)tos).getCopyright() == null)) {
         return new FieldRef(tos, "Copyright");
      }
      return null;
   }

   private Object handleCorp(Object tos) {
      if (tos instanceof Generator && ((Generator)tos).getGeneratorCorporation() == null) {
         GeneratorCorporation generatorCorporation = new GeneratorCorporation();
         ((Generator)tos).setGeneratorCorporation(generatorCorporation);
         return generatorCorporation;
      }
      return null;
   }

   private Object handleCtry(Object tos) {
      if (tos instanceof Address && ((Address)tos).getCountry() == null) {
         return new FieldRef(tos, "Country");
      }
      return null;
   }

   private Object handleData(Object tos) {
      if (tos instanceof Generator && ((Generator)tos).getGeneratorData() == null) {
         GeneratorData generatorData = new GeneratorData();
         ((Generator)tos).setGeneratorData(generatorData);
         return generatorData;
      }
      else if (tos instanceof SourceCitation) {
         // what a hack - if we come across a second data tag, set contents to separate
         if (((SourceCitation)tos).getDataTagContents() == SourceCitation.DataTagContents.DATE ||
             ((SourceCitation)tos).getDataTagContents() == SourceCitation.DataTagContents.TEXT) {
            ((SourceCitation)tos).setDataTagContents(SourceCitation.DataTagContents.SEPARATE);
         }
         return tos; // move data attributes directly onto source citation
      }
      return null;
   }

   private void setDataTagContents(Object tos, boolean addDate) {
      // what a hack - everyone uses the DATA tag differently; some people put only DATE under it, others only TEXT, others both
      // and still others put DATE and TEXT under two separate DATA tags
      // if the second-from-top-of-stack is also a source citation, then we're skipping a DATA tag and we need to set DataTagContents
      if (objectStack.size() > 1 && objectStack.get(objectStack.size()-2) instanceof SourceCitation) {
         SourceCitation.DataTagContents dataTagContents = ((SourceCitation)tos).getDataTagContents();
         if (dataTagContents == null && addDate) {
            dataTagContents = SourceCitation.DataTagContents.DATE;
         }
         else if (dataTagContents == null && !addDate) {
            dataTagContents = SourceCitation.DataTagContents.TEXT;
         }
         else if (dataTagContents == SourceCitation.DataTagContents.TEXT && addDate ||
                  dataTagContents == SourceCitation.DataTagContents.DATE && !addDate) {
            dataTagContents = SourceCitation.DataTagContents.COMBINED;
         }
         ((SourceCitation)tos).setDataTagContents(dataTagContents);
      }
   }

   private Object handleDate(Object tos) {
      if ((tos instanceof GeneratorData && ((GeneratorData)tos).getDate() == null) ||
          (tos instanceof Source && ((Source)tos).getDate() == null) ||
          (tos instanceof EventFact && ((EventFact)tos).getDate() == null)) {
         return new FieldRef(tos, "Date");
      }
      else if (tos instanceof SourceCitation && ((SourceCitation)tos).getDate() == null) {
         setDataTagContents(tos, true);
         return new FieldRef(tos, "Date");
      }
      else if ((tos instanceof Header && ((Header)tos).getDateTime() == null) ||
               (tos instanceof Change && ((Change)tos).getDateTime() == null)) {
         DateTime dateTime = new DateTime();
         if (tos instanceof Header) {
            ((Header)tos).setDateTime(dateTime);
         }
         else {
            ((Change)tos).setDateTime(dateTime);
         }
         return dateTime;
      }
      return null;
   }

   private Object handleDesc(Object tos) {
      if (tos instanceof Submission && ((Submission)tos).getDescription() == null) {
         return new FieldRef(tos, "Description");
      }
      return null;
   }

   private Object handleDesi(Object tos, String ref) {
      if (tos instanceof Person && ((Person)tos).getDescendantInterestSubmitterRef() == null && ref != null) {
         ((Person)tos).setDescendantInterestSubmitterRef(ref);
         return new FieldRef(tos, "DescendantInterestSubmitterRef");
      }
      return null;
   }

   private Object handleDest(Object tos) {
      if (tos instanceof Header && ((Header)tos).getDestination() == null) {
         return new FieldRef(tos, "Destination");
      }
      return null;
   }

   private Object handleEmail(Object tos, String tagName) {
      if ((tos instanceof Submitter && ((Submitter)tos).getEmail() == null) ||
          (tos instanceof GeneratorCorporation && ((GeneratorCorporation)tos).getEmail() == null) ||
          (tos instanceof EventFact && ((EventFact)tos).getEmail() == null) ||
          (tos instanceof Person && ((Person)tos).getEmail() == null) ||
          (tos instanceof Repository && ((Repository)tos).getEmail() == null)) {
         if (tos instanceof Submitter) {
            ((Submitter)tos).setEmailTag(tagName);
         }
         else if (tos instanceof GeneratorCorporation) {
            ((GeneratorCorporation)tos).setEmailTag(tagName);
         }
         else if (tos instanceof EventFact) {
           ((EventFact)tos).setEmailTag(tagName);
         }
         else if (tos instanceof Person) {
            ((Person)tos).setEmailTag(tagName);
         }
         else {
            ((Repository)tos).setEmailTag(tagName);
         }
         return new FieldRef(tos, "Email");
      }
      return null;
   }

   private Object handleEventFact(Object tos, String tagName, String tagNameUpper) {
      if ((tos instanceof Person && EventFact.PERSONAL_EVENT_FACT_TAGS.contains(tagNameUpper)) ||
          (tos instanceof Family && EventFact.FAMILY_EVENT_FACT_TAGS.contains(tagNameUpper))) {
         EventFact eventFact = new EventFact();
         eventFact.setTag(tagName);
         ((PersonFamilyCommonContainer)tos).addEventFact(eventFact);
         return eventFact;
      }
      return null;
   }

   private Object handleFam(Object tos, String id) {
      if (tos instanceof Gedcom) {
         Family family = new Family();
         family.setId(id);
         ((Gedcom)tos).addFamily(family);
         return family;
      }
      return null;
   }

   private Object handleFamc(Object tos, String ref) {
      if (tos instanceof Person) {
         ParentFamilyRef parentFamilyRef = new ParentFamilyRef();
         parentFamilyRef.setRef(ref);
         ((Person)tos).addParentFamilyRef(parentFamilyRef);
         return parentFamilyRef;
      }
      return null;
   }

   private Object handleFams(Object tos, String ref) {
      if (tos instanceof Person) {
         SpouseFamilyRef spouseFamilyRef = new SpouseFamilyRef();
         spouseFamilyRef.setRef(ref);
         ((Person)tos).addSpouseFamilyRef(spouseFamilyRef);
         return spouseFamilyRef;
      }
      return null;
   }

   private Object handleFax(Object tos) {
      if ((tos instanceof GeneratorCorporation && ((GeneratorCorporation)tos).getFax() == null) ||
          (tos instanceof Repository && ((Repository)tos).getFax() == null) ||
          (tos instanceof EventFact && ((EventFact)tos).getFax() == null) ||
          (tos instanceof Person && ((Person)tos).getFax() == null) ||
          (tos instanceof Submitter && ((Submitter)tos).getFax() == null)) {
         return new FieldRef(tos, "Fax");
      }
      return null;
   }

   private Object handleFile(Object tos, String tagName) {
      if (tos instanceof Header && ((Header)tos).getFile() == null) {
         return new FieldRef(tos, "File");
      }
      else if (tos instanceof Media && ((Media)tos).getFile() == null) {
         ((Media)tos).setFileTag(tagName);
         return new FieldRef(tos, "File");
      }
      return null;
   }

  private Object handleFone(Object tos, String tagName) {
    if (tos instanceof Name && ((Name)tos).getFone() == null) {
      ((Name)tos).setFoneTag(tagName);
      return new FieldRef(tos, "Fone");
    }
    return null;
  }

  private Object handleForm(Object tos) {
      if (tos instanceof GedcomVersion && ((GedcomVersion)tos).getForm() == null) {
         return new FieldRef(tos, "Form");
      }
      else if (tos instanceof Media && ((Media)tos).getFormat() == null) {
         return new FieldRef(tos, "Format");
      }
      return null;
   }

   private Object handleFRel(Object tos) {
      if (tos instanceof ChildRef && ((ChildRef)tos).getFatherRelationship() == null) {
         ParentRelationship parentRelationship = new ParentRelationship();
         ((ChildRef)tos).setFatherRelationship(parentRelationship);
         return parentRelationship;
      }
      return null;
   }
   
   private Object handleGed() {
      gedcom = new Gedcom();
      return gedcom;
   }

   private Object handleGedc(Object tos) {
      if (tos instanceof Header && ((Header)tos).getGedcomVersion() == null) {
         GedcomVersion gedcomVersion = new GedcomVersion();
         ((Header)tos).setGedcomVersion(gedcomVersion);
         return gedcomVersion;
      }
      return null;
   }

   private Object handleGivn(Object tos) {
      if (tos instanceof Name && ((Name)tos).getGiven() == null) {
         return new FieldRef(tos, "Given");
      }
      return null;
   }

   private Object handleHead(Object tos) {
      if (tos instanceof Gedcom && ((Gedcom)tos).getHeader() == null) {
         Header header = new Header();
         ((Gedcom) tos).setHeader(header);
         return header;
      }
      return null;
   }

   private Object handleHusb(Object tos, String ref) {
      if (tos instanceof Family) {
         SpouseRef spouseRef = new SpouseRef();
         spouseRef.setRef(ref);
         ((Family)tos).addHusband(spouseRef);
         return spouseRef;
      }
      else if (tos instanceof EventFact) {
         Spouse spouse = new Spouse();
         ((EventFact)tos).setHusband(spouse);
         return spouse;
      }
      return null;
   }

   private Object handleIndi(Object tos, String id) {
      if (tos instanceof Gedcom) {
         Person person = new Person();
         person.setId(id);
         ((Gedcom)tos).addPerson(person);
         return person;
      }
      return null;
   }

   private Object handleItalic(Object tos) {
      if (tos instanceof Source && ((Source)tos).getItalic() == null) {
         return new FieldRef(tos, "Italic");
      }
      return null;
   }
   
   private Object handleLang(Object tos) {
      if ((tos instanceof Submitter && ((Submitter)tos).getLanguage() == null) ||
          (tos instanceof Header && ((Header)tos).getLanguage() == null)) {
         return new FieldRef(tos, "Language");
      }
      return null;
   }

   private Object handleLdsOrdinance(Object tos, boolean isPersonalOrdinance, String tagName) {
      if ((tos instanceof Person && isPersonalOrdinance) ||
          (tos instanceof Family && !isPersonalOrdinance)) {
         LdsOrdinance ldsOrdinance = new LdsOrdinance();
         ((PersonFamilyCommonContainer)tos).addLdsOrdinance(ldsOrdinance);
         ldsOrdinance.setTag(tagName);
         return ldsOrdinance;
      }
      return null;
   }

   private Object handleMarrnm(Object tos, String tagName) {
      if (tos instanceof Name && ((Name)tos).getMarriedName() == null) {
         ((Name)tos).setMarriedNameTag(tagName);
         return new FieldRef(tos, "MarriedName");
      }
      return null;
   }

   private Object handleMedi(Object tos) {
      if (tos instanceof Source && ((Source)tos).getMediaType() == null) {
         return new FieldRef(tos, "MediaType");
      }
      // move medi tag out from under caln in a repository ref
      else if (tos instanceof FieldRef && ((FieldRef)tos).getTarget() instanceof RepositoryRef &&
               ((FieldRef)tos).getFieldName().equals("CallNumber") &&
               ((RepositoryRef)((FieldRef)tos).getTarget()).getMediaType() == null) {
         ((RepositoryRef)((FieldRef)tos).getTarget()).setMediUnderCalnTag(true);
         return new FieldRef(((FieldRef)tos).getTarget(), "MediaType");
      }
      return null;
   }

   private Object handleMRel(Object tos) {
      if (tos instanceof ChildRef && ((ChildRef)tos).getMotherRelationship() == null) {
         ParentRelationship parentRelationship = new ParentRelationship();
         ((ChildRef)tos).setMotherRelationship(parentRelationship);
         return parentRelationship;
      }
      return null;
   }

   private Object handleName(Object tos) {
      if ((tos instanceof Generator && ((Generator)tos).getName() == null) ||
          (tos instanceof Repository && ((Repository)tos).getName() == null) ||
          (tos instanceof Address && ((Address)tos).getName() == null) ||
          (tos instanceof Submitter && ((Submitter)tos).getName() == null)) {
         return new FieldRef(tos, "Name");
      }
      else if (tos instanceof Person) {
         Name name = new Name();
         ((Person)tos).addName(name);
         return name;
      }
      return null;
   }

   private Object handleNick(Object tos) {
      if (tos instanceof Name && ((Name)tos).getNickname() == null) {
         return new FieldRef(tos, "Nickname");
      }
      return null;
   }

   private Object handleNpfx(Object tos) {
      if (tos instanceof Name && ((Name)tos).getPrefix() == null) {
         return new FieldRef(tos, "Prefix");
      }
      return null;
   }

   private Object handleNsfx(Object tos) {
      if (tos instanceof Name && ((Name)tos).getSuffix() == null) {
         return new FieldRef(tos, "Suffix");
      }
      return null;
   }

   private Object handleNote(Object tos, String id, String ref) {
      if (tos instanceof NoteContainer) {
         if (ref == null) {
            Note note = new Note();
            ((NoteContainer)tos).addNote(note);
            return note;
         }
         else {
            NoteRef noteRef = new NoteRef();
            noteRef.setRef(ref);
            ((NoteContainer)tos).addNoteRef(noteRef);
            return noteRef;
         }
      }
      else if (tos instanceof Gedcom) {
         Note note = new Note();
         if (id != null) {
            note.setId(id);
         }
         if (ref != null) {
            // ref is invalid here, so store it as value - another geni-ism
            note.setValue("@"+ref+"@");
         }
         ((Gedcom)tos).addNote(note);
         return note;
      }
      return null;
   }
   
   private Object handleObje(Object tos, String id, String ref) {
      if (tos instanceof MediaContainer) {
         if (ref == null) {
            Media media = new Media();
            ((MediaContainer)tos).addMedia(media);
            return media;
         }
         else {
            MediaRef mediaRef = new MediaRef();
            mediaRef.setRef(ref);
            ((MediaContainer)tos).addMediaRef(mediaRef);
            return mediaRef;
         }
      }
      else if (tos instanceof Gedcom) {
         Media media = new Media();
         if (id != null) {
            media.setId(id);
         }
         ((Gedcom)tos).addMedia(media);
         return media;
      }
      return null;
   }

   private Object handleOrdi(Object tos) {
      if (tos instanceof Submission && ((Submission)tos).getOrdinanceFlag() == null) {
         return new FieldRef(tos, "OrdinanceFlag");
      }
      return null;
   }

   private Object handlePage(Object tos) {
      if (tos instanceof SourceCitation && ((SourceCitation)tos).getPage() == null) {
         return new FieldRef(tos, "Page");
      }
      return null;
   }

   private Object handleParen(Object tos) {
      if (tos instanceof Source && ((Source)tos).getParen() == null) {
         return new FieldRef(tos, "Paren");
      }
      return null;
   }

   private Object handlePedi(Object tos) {
      if (tos instanceof ParentFamilyRef && ((ParentFamilyRef)tos).getRelationshipType() == null) {
         return new FieldRef(tos, "RelationshipType");
      }
      return null;
   }

   private Object handlePhon(Object tos) {
      if ((tos instanceof GeneratorCorporation && ((GeneratorCorporation)tos).getPhone() == null) ||
          (tos instanceof Repository && ((Repository)tos).getPhone() == null) ||
          (tos instanceof EventFact && ((EventFact)tos).getPhone() == null) ||
          (tos instanceof Person && ((Person)tos).getPhone() == null) ||
          (tos instanceof Submitter && ((Submitter)tos).getPhone() == null)) {
         return new FieldRef(tos, "Phone");
      }
      return null;
   }

   private Object handlePlac(Object tos) {
      if (tos instanceof EventFact && ((EventFact)tos).getPlace() == null) {
         return new FieldRef(tos, "Place");
      }
      return null;
   }

   private Object handlePost(Object tos) {
      if (tos instanceof Address && ((Address)tos).getPostalCode() == null) {
         return new FieldRef(tos, "PostalCode");
      }
      return null;
   }
   
   private Object handlePref(Object tos) {
      if (tos instanceof SpouseRef && ((SpouseRef)tos).getPreferred() == null) {
         return new FieldRef(tos, "Preferred");
      }
      return null;
   }

   private Object handlePrim(Object tos) {
      if ((tos instanceof Media && ((Media)tos).getPrimary() == null) ||
          (tos instanceof ParentFamilyRef && ((ParentFamilyRef)tos).getPrimary() == null)) {
         return new FieldRef(tos, "Primary");
      }
      return null;
   }

   private Object handlePubl(Object tos) {
      if (tos instanceof Source && ((Source)tos).getPublicationFacts() == null) {
         return new FieldRef(tos, "PublicationFacts");
      }
      return null;
   }

   private Object handleQuay(Object tos) {
      if (tos instanceof SourceCitation && ((SourceCitation)tos).getQuality() == null) {
         return new FieldRef(tos, "Quality");
      }
      return null;
   }

   private Object handleRefn(Object tos) {
      if (tos instanceof PersonFamilyCommonContainer) {
         return new FieldRef(tos, "ReferenceNumber");
      }
      else if (tos instanceof Source && ((Source)tos).getReferenceNumber() == null) {
         return new FieldRef(tos, "ReferenceNumber");
      }
      return null;
   }

   private Object handleRela(Object tos) {
      if (tos instanceof Association && ((Association)tos).getRelation() == null) {
         return new FieldRef(tos, "Relation");
      }
      return null;
   }

   private Object handleRepo(Object tos, String id, String ref) {
      if (tos instanceof Source && ((Source)tos).getRepositoryRef() == null) {
         RepositoryRef repositoryRef = new RepositoryRef();
         if (ref != null) {
            repositoryRef.setRef(ref);
         }
         ((Source)tos).setRepositoryRef(repositoryRef);
         return repositoryRef;
      }
      else if (tos instanceof Gedcom) {
         Repository repository = new Repository();
         if (id != null) {
            repository.setId(id);
         }
         ((Gedcom)tos).addRepository(repository);
         return repository;
      }
      return null;
   }

   private Object handleRfn(Object tos) {
      if (tos instanceof Person && ((Person)tos).getRecordFileNumber() == null) {
         return new FieldRef(tos, "RecordFileNumber");
      }
      return null;
   }

   private Object handleRin(Object tos) {
      if ((tos instanceof Submitter && ((Submitter)tos).getRin() == null) ||
          (tos instanceof Note && ((Note)tos).getRin() == null) ||
          (tos instanceof Repository && ((Repository)tos).getRin() == null) ||
          (tos instanceof EventFact && ((EventFact)tos).getRin() == null) ||
          (tos instanceof Source && ((Source)tos).getRin() == null) ||
          (tos instanceof PersonFamilyCommonContainer && ((PersonFamilyCommonContainer)tos).getRin() == null)) {
         return new FieldRef(tos, "Rin");
      }
      return null;
   }

  private Object handleRomn(Object tos, String tagName) {
    if (tos instanceof Name && ((Name)tos).getRomn() == null) {
      ((Name)tos).setRomnTag(tagName);
      return new FieldRef(tos, "Romn");
    }
    return null;
  }

  private Object handleScbk(Object tos) {
      if (tos instanceof Media && ((Media)tos).getScrapbook() == null) {
         return new FieldRef(tos, "Scrapbook");
      }
      return null;
   }

   private Object handleSour(Object tos, String id, String ref) {
      if (tos instanceof Header && ((Header)tos).getGenerator() == null) {
         Generator generator = new Generator();
         ((Header)tos).setGenerator(generator);
         return generator;
      }
      else if (tos instanceof SourceCitationContainer ||
               tos instanceof Note ||
               tos instanceof NoteRef ||
               (tos instanceof FieldRef &&
                ((FieldRef)tos).getTarget() instanceof Note &&
                ((FieldRef)tos).getFieldName().equals("Value"))) {
         SourceCitation sourceCitation = new SourceCitation();
         if (ref != null) {
            sourceCitation.setRef(ref);
         }
         if (tos instanceof SourceCitationContainer) {
            ((SourceCitationContainer)tos).addSourceCitation(sourceCitation);
         }
         else if (tos instanceof Note) {
            ((Note)tos).addSourceCitation(sourceCitation);
         }
         else if (tos instanceof NoteRef) {
            ((NoteRef)tos).addSourceCitation(sourceCitation);
         }
         else {
            // Reunion puts source citations under value: 0 NOTE 1 CONT ... 2 SOUR
            Note note = (Note)((FieldRef)tos).getTarget();
            note.addSourceCitation(sourceCitation);
            note.setSourceCitationsUnderValue(true);
         }
         return sourceCitation;
      }
      else if (tos instanceof Gedcom) {
         Source source = new Source();
         if (id != null) {
            source.setId(id);
         }
         ((Gedcom)tos).addSource(source);
         return source;
      }
      return null;
   }

   private Object handleSpfx(Object tos) {
      if (tos instanceof Name && ((Name)tos).getSurnamePrefix() == null) {
         return new FieldRef(tos, "SurnamePrefix");
      }
      return null;
   }

   private Object handleSshow(Object tos) {
      if (tos instanceof Media && ((Media)tos).getSlideShow() == null) {
         return new FieldRef(tos, "SlideShow");
      }
      return null;
   }

   private Object handleStae(Object tos) {
      if (tos instanceof Address && ((Address)tos).getState() == null) {
         return new FieldRef(tos, "State");
      }
      return null;
   }

   private Object handleStat(Object tos) {
      if (tos instanceof LdsOrdinance && ((LdsOrdinance)tos).getStatus() == null) {
         return new FieldRef(tos, "Status");
      }
      return null;
   }

   private Object handleSubm(Object tos, String id, String ref) {
      if (tos instanceof Header && ref != null && ((Header)tos).getSubmitterRef() == null) {
         ((Header)tos).setSubmitterRef(ref);
         return new Object(); // placeholder
      }
      else if (tos instanceof Gedcom) {
         Submitter submitter = new Submitter();
         if (id != null) {
            submitter.setId(id);
         }
         ((Gedcom)tos).addSubmitter(submitter);
         return submitter;
      }
      return null;
   }

   private Object handleSubn(Object tos, String id, String ref) {
      if (tos instanceof Header && ref != null && ((Header)tos).getSubmissionRef() == null) {
         ((Header)tos).setSubmissionRef(ref);
         return new Object(); // placeholder
      }
      else if (tos instanceof Header && ref == null && ((Header)tos).getSubmission() == null) {
         Submission submission = new Submission();
         ((Header)tos).setSubmission(submission);
         return submission;
      }
      else if (tos instanceof Gedcom && ((Gedcom)tos).getSubmission() == null) {
         Submission submission = new Submission();
         if (id != null) {
            submission.setId(id);
         }
         ((Gedcom)tos).setSubmission(submission);
         return submission;
      }
      return null;
   }

   private Object handleSurn(Object tos) {
      if (tos instanceof Name && ((Name)tos).getSurname() == null) {
         return new FieldRef(tos, "Surname");
      }
      return null;
   }

   private Object handleTemp(Object tos) {
      if (tos instanceof LdsOrdinance && ((LdsOrdinance)tos).getTemple() == null) {
         return new FieldRef(tos, "Temple");
      }
      return null;
   }

   private Object handleText(Object tos) {
      if (tos instanceof SourceCitation) {
         setDataTagContents(tos, false);
         // standard says you should just have 1 TEXT tag, but geni uses multiple text tags in place of CONT tags here
         String text = ((SourceCitation)tos).getText();
         if (text != null) {
            ((SourceCitation)tos).setText(text+"\n");
         }
         return new FieldRef(tos, "Text");
      }
      else if (tos instanceof Source) {
         String text = ((Source)tos).getText();
         if (text != null) {
            ((Source)tos).setText(text+"\n");
         }
         return new FieldRef(tos, "Text");
      }
      return null;
   }

   private Object handleTime(Object tos) {
      if (tos instanceof DateTime && ((DateTime)tos).getTime() == null) {
         return new FieldRef(tos, "Time");
      }
      return null;
   }

   private Object handleTitl(Object tos) {
      if ((tos instanceof Media && ((Media)tos).getTitle() == null) ||
          (tos instanceof Source && ((Source)tos).getTitle() == null)) {
         return new FieldRef(tos, "Title");
      }
      return null;
   }

   private Object handleTrlr(Object tos) {
      if (tos instanceof Gedcom) {
         // don't attach trailer to gedcom
         return new Trailer();
      }
      return null;
   }

   private Object handleType(Object tos, String tagName) {
      if ((tos instanceof Name && ((Name)tos).getType() == null) ||
          (tos instanceof Media && ((Media)tos).getType() == null) ||
          (tos instanceof EventFact && ((EventFact)tos).getType() == null) ||
          (tos instanceof Association && ((Association)tos).getType() == null) ||
          (tos instanceof Source && ((Source)tos).getType() == null)) {
         if (tos instanceof Source) {
            ((Source)tos).setTypeTag(tagName);
         }
         else if (tos instanceof Name) {
            ((Name)tos).setTypeTag(tagName);
         }
         return new FieldRef(tos, "Type");
      }
      return null;
   }

   private Object handleUid(Object tos, String tagName) {
      if ((tos instanceof PersonFamilyCommonContainer && ((PersonFamilyCommonContainer)tos).getUid() == null) ||
          (tos instanceof EventFact && ((EventFact)tos).getUid() == null) ||
          (tos instanceof Source && ((Source)tos).getUid() == null)) {
         if (tos instanceof PersonFamilyCommonContainer) {
            ((PersonFamilyCommonContainer)tos).setUidTag(tagName);
         }
         else if (tos instanceof EventFact) {
            ((EventFact)tos).setUidTag(tagName);
         }
         else {
            ((Source)tos).setUidTag(tagName);
         }
         return new FieldRef(tos, "Uid");
      }
      return null;
   }
   
   private Object handleVers(Object tos) {
      if ((tos instanceof Generator && ((Generator)tos).getVersion() == null) ||
          (tos instanceof GedcomVersion && ((GedcomVersion)tos).getVersion() == null) ||
          (tos instanceof CharacterSet && ((CharacterSet)tos).getVersion() == null)) {
         return new FieldRef(tos, "Version");
      }
      return null;
   }

   private Object handleWife(Object tos, String ref) {
      if (tos instanceof Family) {
         SpouseRef spouseRef = new SpouseRef();
         spouseRef.setRef(ref);
         ((Family)tos).addWife(spouseRef);
         return spouseRef;
      }
      else if (tos instanceof EventFact) {
         Spouse spouse = new Spouse();
         ((EventFact)tos).setWife(spouse);
         return spouse;
      }
      return null;
   }

   private Object handleWww(Object tos, String tagName) {
      if ((tos instanceof GeneratorCorporation && ((GeneratorCorporation)tos).getWww() == null) ||
          (tos instanceof Repository && ((Repository)tos).getWww() == null) ||
          (tos instanceof EventFact && ((EventFact)tos).getWww() == null) ||
          (tos instanceof Person && ((Person)tos).getWww() == null) ||
          (tos instanceof Submitter && ((Submitter)tos).getWww() == null)) {
         if (tos instanceof GeneratorCorporation) {
            ((GeneratorCorporation)tos).setWwwTag(tagName);
         }
         else if (tos instanceof Repository) {
            ((Repository)tos).setWwwTag(tagName);
         }
         else if (tos instanceof EventFact) {
           ((EventFact)tos).setWwwTag(tagName);
         }
         else if (tos instanceof Person) {
           ((Person)tos).setWwwTag(tagName);
         }
         else {
            ((Submitter)tos).setWwwTag(tagName);
         }
         return new FieldRef(tos, "Www");
      }
      return null;
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      objectStack.pop();
      tagStack.pop();
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      String value = new String(ch, start, length);
      Object tos = objectStack.size() > 0 ? objectStack.peek() : null;
      FieldRef fieldRef = null;
      try {
         if (tos instanceof GedcomTag) {
            ((GedcomTag)tos).appendValue(value);
         }
         else if (tos instanceof FieldRef) {
            fieldRef = (FieldRef)tos;
            fieldRef.appendValue(value);
         }
         else {
            fieldRef = new FieldRef(tos, "Value");
            fieldRef.setValue(value);
         }
      }
      catch (NoSuchMethodException e) {
         if (fieldRef.getFieldName().equals("Value")) {
            // this object doesn't have a value field, so we're going to drop these characters on the floor
            SAXParseException exception = new SAXParseException("value not stored for: "+joinTagStack(), locator);
            error(exception);
         }
         else {
            // if we don't find the method, it's programmer error
            SAXParseException exception = new SAXParseException("get method not found for: "+fieldRef.getClassFieldName(), locator);
            fatalError(exception);
            throw exception;
         }
      }
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
         logger.trace(exception.getMessage() + " @ " + exception.getLineNumber());
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

   public Gedcom parseGedcom(File gedcomFile) throws SAXParseException, IOException {
      GedcomParser parser = gedcomParser();
      parser.parse(gedcomFile.toURI().toString());
      return gedcom;
   }

   public Gedcom parseGedcom(InputStream is) throws SAXParseException, IOException {
      GedcomParser parser = gedcomParser();
      parser.parse(is);
      return gedcom;
   }

   public Gedcom parseGedcom(Reader reader) throws SAXParseException, IOException {
      GedcomParser parser = gedcomParser();
      parser.parse(reader);
      return gedcom;
   }

   private GedcomParser gedcomParser() {
      GedcomParser parser = new GedcomParser();
      parser.setContentHandler(this);
      parser.setErrorHandler(this);
      return parser;
   }

}

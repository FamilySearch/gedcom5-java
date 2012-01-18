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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Dallan
 * Date: 12/25/11
 *
 * omit: Submitter, Reference type, Restriction, >1 anci, >1 desi
 * add: Uid, Address, phone, email, title (is a Name)
 * change: alia from xref to Name
 */
public class Person extends PersonFamilyCommonContainer {
   private String id = null;
   private List<Name> names = null;
   private List<ParentFamilyRef> famc = null;
   private List<SpouseFamilyRef> fams = null;
   private List<Association> assos = null;
   private String anci = null;
   private String desi = null;
   private String rfn = null;
   private Address addr = null;
   private String phon = null;
   private String email = null;
   private String emailTag = null;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public List<Name> getNames() {
      return names != null ? names : Collections.<Name>emptyList();
   }

   public void setNames(List<Name> names) {
      this.names = names;
   }

   public void addName(Name name) {
      if (names == null) {
         names = new ArrayList<Name>();
      }
      names.add(name);
   }
   
   private List<Family> getFamilies(Gedcom gedcom, List<? extends SpouseFamilyRef> familyRefs) {
      List<Family> families = new ArrayList<Family>();
      for (SpouseFamilyRef familyRef : familyRefs) {
         Family family = familyRef.getFamily(gedcom);
         if (family != null) {
            families.add(family);
         }
      }
      return families;
   }

   /**
    * Convenience function to dereference parent family refs
    * @param gedcom Gedcom
    * @return list of parent families
    */
   public List<Family> getParentFamilies(Gedcom gedcom) {
      return getFamilies(gedcom, getParentFamilyRefs());
   }

   public List<ParentFamilyRef> getParentFamilyRefs() {
      return famc != null ? famc : Collections.<ParentFamilyRef>emptyList();
   }

   public void setParentFamilyRefs(List<ParentFamilyRef> famc) {
      this.famc = famc;
   }
   
   public void addParentFamilyRef(ParentFamilyRef parentFamilyRef) {
      if (famc == null) {
         famc = new ArrayList<ParentFamilyRef>();
      }
      famc.add(parentFamilyRef);
   }

   /**
    * Convenience function to dereference spouse family refs
    * @param gedcom Gedcom
    * @return list of spouse families
    */
   public List<Family> getSpouseFamilies(Gedcom gedcom) {
      return getFamilies(gedcom, getSpouseFamilyRefs());
   }

   public List<SpouseFamilyRef> getSpouseFamilyRefs() {
      return fams != null ? fams : Collections.<SpouseFamilyRef>emptyList();
   }

   public void setSpouseFamilyRefs(List<SpouseFamilyRef> fams) {
      this.fams = fams;
   }

   public void addSpouseFamilyRef(SpouseFamilyRef spouseFamilyRef) {
      if (fams == null) {
         fams = new ArrayList<SpouseFamilyRef>();
      }
      fams.add(spouseFamilyRef);
   }

   public List<Association> getAssociations() {
      return assos != null ? assos : Collections.<Association>emptyList();
   }

   public void setAssociations(List<Association> assos) {
      this.assos = assos;
   }

   public void addAssociation(Association asso) {
      if (assos == null) {
         assos = new ArrayList<Association>();
      }
      assos.add(asso);
   }

   public String getAncestorInterestSubmitterRef() {
      return anci;
   }

   public void setAncestorInterestSubmitterRef(String anci) {
      this.anci = anci;
   }

   public String getDescendantInterestSubmitterRef() {
      return desi;
   }

   public void setDescendantInterestSubmitterRef(String desi) {
      this.desi = desi;
   }

   public String getRecordFileNumber() {
      return rfn;
   }

   public void setRecordFileNumber(String rfn) {
      this.rfn = rfn;
   }

   public Address getAddress() {
      return addr;
   }

   public void setAddress(Address addr) {
      this.addr = addr;
   }

   public String getPhone() {
      return phon;
   }

   public void setPhone(String phon) {
      this.phon = phon;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getEmailTag() {
      return emailTag;
   }

   public void setEmailTag(String emailTag) {
      this.emailTag = emailTag;
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         for (Name name : getNames()) {
            name.accept(visitor);
         }
         for (ParentFamilyRef parentFamilyRef : getParentFamilyRefs()) {
            parentFamilyRef.accept(visitor);
         }
         for (SpouseFamilyRef spouseFamilyRef : getSpouseFamilyRefs()) {
            spouseFamilyRef.accept(visitor);
         }
         for (Association association : getAssociations()) {
            association.accept(visitor);
         }
         if (addr != null) {
            addr.accept(visitor);
         }
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

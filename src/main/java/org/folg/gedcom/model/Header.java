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
 * omit: Submitter, Submission, Place
 */
public class Header extends NoteContainer {
   private Generator sour = null;
   private String dest = null;
   private DateTime date = null;
   private String submRef = null;
   private String subnRef = null;
   private Submission subn = null;
   private String file = null;
   private String copr = null;
   private GedcomVersion gedc = null;
   private CharacterSet charset = null;
   private String lang = null;

   public Generator getGenerator() {
      return sour;
   }

   public void setGenerator(Generator sour) {
      this.sour = sour;
   }

   public String getDestination() {
      return dest;
   }

   public void setDestination(String dest) {
      this.dest = dest;
   }

   public DateTime getDateTime() {
      return date;
   }

   public void setDateTime(DateTime date) {
      this.date = date;
   }

   /**
    * Use Gedcom.getSubmitter in place of this function
    * @return submitter reference
    */
   public String getSubmitterRef() {
      return submRef;
   }

   public void setSubmitterRef(String submRef) {
      this.submRef = submRef;
   }

   /**
    * Use Gedcom.getSubmitter in place of this function
    * @return submitter
    */
   public Submitter getSubmitter(Gedcom gedcom) {
      return gedcom.getSubmitter(submRef);
   }

   /**
    * Use Gedcom.getSubmission in place of this function
    * @return submission reference
    */
   public String getSubmissionRef() {
      return subnRef;
   }

   public void setSubmissionRef(String subnRef) {
      this.subnRef = subnRef;
   }

   /**
    * Use Gedcom.getSubmission in place of this function
    * @return submission
    */
   public Submission getSubmission() {
      return subn;
   }

   public void setSubmission(Submission subn) {
      this.subn = subn;
   }

   public String getFile() {
      return file;
   }

   public void setFile(String file) {
      this.file = file;
   }

   public String getCopyright() {
      return copr;
   }
   
   public void setCopyright(String copr) {
      this.copr = copr;
   }
   
   public GedcomVersion getGedcomVersion() {
      return gedc;
   }

   public void setGedcomVersion(GedcomVersion gedc) {
      this.gedc = gedc;
   }

   public CharacterSet getCharacterSet() {
      return charset;
   }

   public void setCharacterSet(CharacterSet charset) {
      this.charset = charset;
   }

   public String getLanguage() {
      return lang;
   }

   public void setLanguage(String lang) {
      this.lang = lang;
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         if (sour != null) {
            sour.accept(visitor);
         }
         if (date != null) {
            date.accept(visitor);
         }
         if (subn != null) {
            subn.accept(visitor);
         }
         if (gedc != null) {
            gedc.accept(visitor);
         }
         if (charset != null) {
            charset.accept(visitor);
         }
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

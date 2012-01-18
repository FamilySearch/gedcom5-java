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
 * Date: 12/27/11
 * 
 * omit: event, data
 * add: text and date from data
 */
public class SourceCitation extends MediaContainer {
   public static enum DataTagContents {DATE, TEXT, COMBINED, SEPARATE};

   private String ref = null;
   private String value = null;
   private String page = null;
   private String date = null;
   private String text = null;
   private String quay = null;
   // yuck - some gedcom's don't use the data tag, some include write both text and date under the same tag, others use two data tags
   private DataTagContents dataTagContents = null; // set to null in default case (no data tag) so it isn't saved to json

   public String getRef() {
      return ref;
   }

   public void setRef(String ref) {
      this.ref = ref;
   }

   public Source getSource(Gedcom gedcom) {
      return gedcom.getSource(ref);
   }

   /**
    * Use this function to get text from value or text field
    * @return text, or value if text is empty
    */
   public String getTextOrValue() {
      return text != null ? text : value;
   }
   
   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getPage() {
      return page;
   }

   public void setPage(String page) {
      this.page = page;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getQuality() {
      return quay;
   }

   public void setQuality(String quay) {
      this.quay = quay;
   }

   public DataTagContents getDataTagContents() {
      return this.dataTagContents;
   }

   public void setDataTagContents(DataTagContents dataTagContents) {
      this.dataTagContents = dataTagContents;
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

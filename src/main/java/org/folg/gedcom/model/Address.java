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
 * Date: 12/26/11
 *
 * add: name
 * note: addressLine1+2 are rarely used; addresses are generally stored in Value
 */
public class Address extends ExtensionContainer {
   private String value = null;
   private String adr1 = null;
   private String adr2 = null;
   private String adr3 = null;
   private String city = null;
   private String stae = null;
   private String post = null;
   private String ctry = null;
   private String _name = null;

   private void appendValue(StringBuilder buf, String value) {
      if (value != null) {
         if (buf.length() > 0) {
            buf.append("\n");
         }
         buf.append(value);
      }
   }

   public String getDisplayValue() {
      StringBuilder buf = new StringBuilder();
      appendValue(buf, value);
      appendValue(buf, adr1);
      appendValue(buf, adr2);
      appendValue(buf, adr3);
      appendValue(buf, (city != null ? city : "")+(city != null && stae != null ? ", " : "")+(stae != null ? stae : "")+
                       ((city != null || stae != null) && post != null ? " " : "")+(post != null ? post : ""));
      appendValue(buf, ctry);
      return buf.toString();
   }
   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getAddressLine1() {
      return adr1;
   }

   public void setAddressLine1(String adr1) {
      this.adr1 = adr1;
   }

   public String getAddressLine2() {
      return adr2;
   }

   public void setAddressLine2(String adr2) {
      this.adr2 = adr2;
   }

   public String getAddressLine3() {
      return adr3;
   }

   public void setAddressLine3(String adr3) {
      this.adr3 = adr3;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getState() {
      return stae;
   }

   public void setState(String stae) {
      this.stae = stae;
   }

   public String getPostalCode() {
      return post;
   }

   public void setPostalCode(String post) {
      this.post = post;
   }

   public String getCountry() {
      return ctry;
   }

   public void setCountry(String ctry) {
      this.ctry = ctry;
   }

   public String getName() {
      return _name;
   }

   public void setName(String name) {
      this._name = name;
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

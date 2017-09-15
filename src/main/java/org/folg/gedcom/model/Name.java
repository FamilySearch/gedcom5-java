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
 * add: type, aka, married name, media
 * (Media objects are very rare in gedcoms in the wild)
 * note: _aka and _marrnm should be their own Name, but this would break round-tripping, so do it as a separate step
 */
public class Name extends SourceCitationContainer {
   private String value = null;
   private String givn = null;
   private String surn = null;
   private String npfx = null;
   private String nsfx = null;
   private String spfx = null;
   private String nick = null;
   private String fone = null;
   private String romn = null;
   private String _type = null;
   private String typeTag = null;
   private String _aka = null;
   private String akaTag = null;
   private String foneTag = null;
   private String romnTag = null;
   private String _marrnm = null;
   private String marrnmTag = null;

   private void appendValue(StringBuilder buf, String value) {
      if (value != null) {
         if (buf.length() > 0) {
            buf.append(' ');
         }
         buf.append(value);
      }
   }

   public String getDisplayValue() {
      if (value != null) {
         return value;
      }
      else {
         StringBuilder buf = new StringBuilder();
         appendValue(buf, npfx);
         appendValue(buf, givn);
         appendValue(buf, spfx);
         appendValue(buf, surn);
         appendValue(buf, nsfx);
         return buf.toString();
      }
   }
   
   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getGiven() {
      return givn;
   }

   public void setGiven(String givn) {
      this.givn = givn;
   }

   public String getSurname() {
      return surn;
   }

   public void setSurname(String surn) {
      this.surn = surn;
   }

   public String getPrefix() {
      return npfx;
   }

   public void setPrefix(String npfx) {
      this.npfx = npfx;
   }

   public String getSuffix() {
      return nsfx;
   }

   public void setSuffix(String nsfx) {
      this.nsfx = nsfx;
   }

   /**
    * Rarely used
    * @return surname prefix
    */
   public String getSurnamePrefix() {
      return spfx;
   }

   public void setSurnamePrefix(String spfx) {
      this.spfx = spfx;
   }

   public String getNickname() {
      return nick;
   }

   public void setNickname(String nick) {
      this.nick = nick;
   }

   /**
    * Name has a type of ALIA or TITL when the GEDCOM had a ALIA or TITL sub-tag of INDI
    * @return The type.
    */
   public String getType() {
      return _type;
   }

   public void setType(String _type) {
      this._type = _type;
   }

   public String getTypeTag() {
      return typeTag;
   }

   public void setTypeTag(String typeTag) {
      this.typeTag = typeTag;
   }

   public String getAka() {
      return _aka;
   }

   public void setAka(String _aka) {
      this._aka = _aka;
   }

   public String getAkaTag() {
      return akaTag;
   }

   public void setAkaTag(String akaTag) {
      this.akaTag = akaTag;
   }

   public String getRomn() {
      return romn;
   }

   public void setRomn(String romn) {
      this.romn = romn;
   }

   public String getRomnTag() {
      return romnTag;
   }

   public void setRomnTag(String romnTag) {
      this.romnTag = romnTag;
   }

   public String getFone() {
      return fone;
   }

   public void setFone(String fone) {
      this.fone = fone;
   }

   public String getFoneTag() {
      return foneTag;
   }

   public void setFoneTag(String foneTag) {
      this.foneTag = foneTag;
   }


   public String getMarriedName() {
      return _marrnm;
   }

   public void setMarriedName(String _marrnm) {
      this._marrnm = _marrnm;
   }

   public String getMarriedNameTag() {
      return marrnmTag;
   }

   public void setMarriedNameTag(String marrnmTag) {
      this.marrnmTag = marrnmTag;
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

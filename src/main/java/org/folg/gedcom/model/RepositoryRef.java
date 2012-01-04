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
 * Date: 12/30/11
 *
 * omit: multiple call numbers
 * add: value (a few sources inline the repo name as a value)
 * move: media type from under call number to directly under repository ref
 */
public class RepositoryRef extends NoteContainer {
   private String ref = null;
   private String value = null;
   private String caln = null;
   private String medi = null;

   public String getRef() {
      return ref;
   }

   public void setRef(String ref) {
      this.ref = ref;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public Repository getRepository(Gedcom gedcom) {
      return gedcom.getRepository(ref);
   }

   public String getCallNumber() {
      return caln;
   }

   public void setCallNumber(String caln) {
      this.caln = caln;
   }

   public String getMediaType() {
      return medi;
   }

   public void setMediaType(String medi) {
      this.medi = medi;
   }

   public void accept(Visitor visitor) {
      visitor.visit(this);
      super.visitContainedObjects(visitor);
      visitor.endVisit(this);
   }
}

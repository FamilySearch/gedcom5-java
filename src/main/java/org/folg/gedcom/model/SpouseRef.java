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
 */
public class SpouseRef extends ExtensionContainer {
   private String ref = null;
   private String _pref = null;

   public String getRef() {
      return ref;
   }

   public void setRef(String ref) {
      this.ref = ref;
   }

   /**
    * Convenience function to dereference person
    * @param gedcom Gedcom
    * @return referenced person
    */
   public Person getPerson(Gedcom gedcom) {
      return gedcom.getPerson(ref);
   }

   public String getPreferred() {
      return _pref;
   }

   public void setPreferred(String pref) {
      this._pref = pref;
   }
   
   public void accept(Visitor visitor) {
      throw new RuntimeException("Not implemented - pass isHusband");
   }

   /**
    * Handle the visitor  
    * @param visitor Visitor
    * @param isHusband false for wife; ChildRef overrides this method
    */
   public void accept(Visitor visitor, boolean isHusband) {
      if (visitor.visit(this, isHusband)) {
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

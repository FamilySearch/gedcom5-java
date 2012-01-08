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
 * Date: 12/28/11
 *
 * add: primary
 */
public class ParentFamilyRef extends SpouseFamilyRef {
   private String pedi;
   private String _primary;

   public String getRelationshipType() {
      return pedi;
   }

   public void setRelationshipType(String pedi) {
      this.pedi = pedi;
   }

   public String getPrimary() {
      return _primary;
   }

   public void setPrimary(String primary) {
      this._primary = primary;
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

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
 * User: dallan
 * Date: 1/3/12
 */
public class Association extends ExtensionContainer {
   private String ref = null;
   private String type = null;
   private String rela = null;

   public String getRef() {
      return ref;
   }

   public void setRef(String ref) {
      this.ref = ref;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getRelation() {
      return rela;
   }

   public void setRelation(String rela) {
      this.rela = rela;
   }

   public void accept(Visitor visitor) {
      visitor.visit(this);
      super.visitContainedObjects(visitor);
      visitor.endVisit(this);
   }
}

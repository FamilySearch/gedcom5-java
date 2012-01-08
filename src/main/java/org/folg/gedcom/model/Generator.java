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
 */
public class Generator extends ExtensionContainer {
   private String value = null;
   private String name = null;
   private String vers = null;
   private GeneratorCorporation corp = null;
   private GeneratorData data = null;

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getVersion() {
      return vers;
   }

   public void setVersion(String vers) {
      this.vers = vers;
   }

   public GeneratorCorporation getGeneratorCorporation() {
      return corp;
   }

   public void setGeneratorCorporation(GeneratorCorporation corp) {
      this.corp = corp;
   }

   public GeneratorData getGeneratorData() {
      return data;
   }

   public void setGeneratorData(GeneratorData data) {
      this.data = data;
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         if (corp != null) {
            corp.accept(visitor);
         }
         if (data != null) {
            data.accept(visitor);
         }
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

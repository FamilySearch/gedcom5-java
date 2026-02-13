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

public class Spouse extends ExtensionContainer {
   private String age = null;

   public String getAge() {
      return age;
   }

   public void setAge(String age) {
      this.age = age;
   }

   public void accept(Visitor visitor) {
      throw new RuntimeException("Not implemented - pass isHusband");
   }

   public void accept(Visitor visitor, boolean isHusband) {
      if (visitor.visit(this, isHusband)) {
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

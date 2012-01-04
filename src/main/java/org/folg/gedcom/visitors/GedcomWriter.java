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

package org.folg.gedcom.visitors;

import org.folg.gedcom.model.Address;
import org.folg.gedcom.model.ExtensionContainer;
import org.folg.gedcom.model.Visitor;

/**
 * User: Dallan
 * Date: 12/25/11
 */
public class GedcomWriter extends Visitor {
   // TODO - extend visitor, accept PrintWriter to write to

   @Override
   public void visit(Address address) {
      // add 1 to level
      // output tag
      // output address attrs
   }

   @Override
   public void endVisit(ExtensionContainer obj) {
      // subtract 1 from level
   }
}

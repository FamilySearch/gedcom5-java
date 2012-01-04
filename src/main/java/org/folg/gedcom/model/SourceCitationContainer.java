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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Dallan
 * Date: 12/29/11
 */
public abstract class SourceCitationContainer extends MediaContainer {
   private List<SourceCitation> sourceCitations = null;

   public List<SourceCitation> getSourceCitations() {
      return sourceCitations != null ? sourceCitations : Collections.<SourceCitation>emptyList();
   }

   public void setSourceCitations(List<SourceCitation> sourceCitations) {
      this.sourceCitations = sourceCitations;
   }

   public void addSourceCitation(SourceCitation sourceCitation) {
      if (sourceCitations == null) {
         sourceCitations = new ArrayList<SourceCitation>();
      }
      sourceCitations.add(sourceCitation);
   }

   public void visitContainedObjects(Visitor visitor) {
      for (SourceCitation sourceCitation : getSourceCitations()) {
         sourceCitation.accept(visitor);
      }
      super.visitContainedObjects(visitor);
   }
}

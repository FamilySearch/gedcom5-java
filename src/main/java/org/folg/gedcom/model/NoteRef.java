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
 * User: dallan
 * Date: 1/3/12
 * don't make this a SourceCitationContainer, because that would allow notes within note refs
 */
public class NoteRef extends ExtensionContainer {
   private String ref = null;
   private List<SourceCitation> sourceCitations = null;

   public String getRef() {
      return ref;
   }

   public void setRef(String ref) {
      this.ref = ref;
   }

   /**
    * Convenience function to dereference note
    * @param gedcom Gedcom
    * @return referenced note
    */
   public Note getNote(Gedcom gedcom) {
      return gedcom.getNote(ref);
   }

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

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         for (SourceCitation sourceCitation : getSourceCitations()) {
            sourceCitation.accept(visitor);
         }
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

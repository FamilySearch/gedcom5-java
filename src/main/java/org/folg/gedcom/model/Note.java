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
 * Date: 12/26/11
 * 
 * omit: reference number
 * don't make this a SourceCitationContainer, because that would allow nested notes
 */
public class Note extends ExtensionContainer {
   private String id = null;
   private String value = null;
   private String rin = null;
   private Change chan = null;
   private List<SourceCitation> sourceCitations = null;
   private boolean sourceCitationsUnderValue = false; // yuck: Reunion does this: 0 NOTE 1 CONT ... 2 SOUR; remember for round-trip

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getRin() {
      return rin;
   }

   public void setRin(String rin) {
      this.rin = rin;
   }

   public Change getChange() {
      return chan;
   }

   public void setChange(Change chan) {
      this.chan = chan;
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

   public boolean isSourceCitationsUnderValue() {
      return sourceCitationsUnderValue;
   }

   public void setSourceCitationsUnderValue(boolean sourceRefsUnderValue) {
      this.sourceCitationsUnderValue = sourceRefsUnderValue;
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         if (chan != null) {
            chan.accept(visitor);
         }
         for (SourceCitation sourceCitation : getSourceCitations()) {
            sourceCitation.accept(visitor);
         }
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

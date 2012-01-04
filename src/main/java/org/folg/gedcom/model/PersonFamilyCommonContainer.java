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
 * Date: 12/28/11
 */
public abstract class PersonFamilyCommonContainer extends SourceCitationContainer {
   private String id = null;
   private List<EventFact> eventsFacts = null;
   private List<LdsOrdinance> ldsOrdinances = null;
   private String refn = null;
   private String rin = null;
   private Change chan = null;
   private String _uid = null;
   private String uidTag = null;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public List<EventFact> getEventsFacts() {
      return eventsFacts != null ? eventsFacts : Collections.<EventFact>emptyList();
   }

   public void setEventsFacts(List<EventFact> eventsFacts) {
      this.eventsFacts = eventsFacts;
   }

   public void addEventFact(EventFact eventFact) {
      if (eventsFacts == null) {
         eventsFacts = new ArrayList<EventFact>();
      }
      eventsFacts.add(eventFact);
   }

   public List<LdsOrdinance> getLdsOrdinances() {
      return ldsOrdinances != null ? ldsOrdinances : Collections.<LdsOrdinance>emptyList();
   }

   public void setLdsOrdinances(List<LdsOrdinance> ldsOrdinances) {
      this.ldsOrdinances = ldsOrdinances;
   }

   public void addLdsOrdinance(LdsOrdinance ldsOrdinance) {
      if (ldsOrdinances == null) {
         ldsOrdinances = new ArrayList<LdsOrdinance>();
      }
      ldsOrdinances.add(ldsOrdinance);
   }

   public String getReferenceNumber() {
      return refn;
   }

   public void setReferenceNumber(String refn) {
      this.refn = refn;
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

   public String getUid() {
      return _uid;
   }

   public void setUid(String uid) {
      this._uid = uid;
   }

   public String getUidTag() {
      return uidTag;
   }

   public void setUidTag(String uidTag) {
      this.uidTag = uidTag;
   }

   public void visitContainedObjects(Visitor visitor) {
      for (EventFact eventFact : getEventsFacts()) {
         eventFact.accept(visitor);
      }
      for (LdsOrdinance ldsOrdinance : getLdsOrdinances()) {
         ldsOrdinance.accept(visitor);
      }
      if (chan != null) {
         chan.accept(visitor);
      }
      super.visitContainedObjects(visitor);
   }
}

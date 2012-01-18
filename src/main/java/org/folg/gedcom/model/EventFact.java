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

import java.util.*;

/**
 * User: Dallan
 * Date: 12/25/11
 * 
 * omit: Phone, Age, Agency, Cause (except for death events)
 * add: Rin, Uid
 * move: move caus tag for death events to its own event so it can have source citations; set EventFact.causeTag=tag of other event
 */
public class EventFact extends SourceCitationContainer {
   public static final Set<String> PERSONAL_EVENT_FACT_TAGS = new HashSet<String>(Arrays.asList(
           "ADOP", "AFN", "ARRI", "ARVL", "_ATTR",
           "BAP", "BAPM", "BAPT", "BARM", "BIRT", "BLES", "BURI",
           "CAST", "CAUS", "CENS", "CHR", "_COLOR", "CONF", "CREM",
           "_DCAUSE", "DEAT", "DEED", "_DEG", "DPRT", "DSCR",
           "EDUC", "_ELEC", "EMAIL", "EMIG", "EMPL", "_EMPLOY", "ENLIST", "EVEN", "EVENT", "EYES",
           "FACT", "FCOM", "_FNRL", "_FUN",
           "_FA1", "_FA2", "_FA3", "_FA4", "_FA5", "_FA6", "_FA7", "_FA8", "_FA9", "_FA10", "_FA11", "_FA12", "_FA13",
           "GRAD",
           "HAIR", "HEIG", "_HEIG",
           "ILL", "IMMI",
           "MARR", "_MDCL", "MIL", "MILI", "_MIL", "_MILI", "_MILT", "_MILTID", "MISE",
           "NATI", "NATU",
           "OCCU", "ORDI", "ORDN",
           "PHON", "PROB", "PROP",
           "RELI", "RESI", "RETI",
           "SEX", "SSN",
           "_WEIG", "WILL"
   ));
   public static final Set<String> FAMILY_EVENT_FACT_TAGS = new HashSet<String>(Arrays.asList(
      "ANUL",
      "CENS", "CLAW",
      "_DEATH_OF_SPOUSE", "DIV", "DIVF", "DIVORCE", "_DIV",
      "EMIG", "ENGA", "EVEN", "EVENT",
      "IMMI",
      "MARB", "MARC", "MARL", "MARR", "MARRIAGE", "MARS", "_MBON",
      "NCHI",
      "RESI",
      "SEPA", "_SEPR"
   ));

   public static final Map<String,String> DISPLAY_TYPE;
   // note: some of these tags aren't in the personal/family_event_fact_tags sets because they appear only in the type field
   // others need to be added to the appropriate tag sets
   static {
      Map<String, String> m = new HashMap<String, String>();
      m.put("ADOP", "Adoption");
      m.put("ADOPTION", "Adoption");
      m.put("AFN", "Ancestral file number");
      m.put("ANUL","Annulment");
      m.put("ANNULMENT", "Annulment");
      m.put("ARRIVAL", "Arrival");
      m.put("ARRI", "Arrival");
      m.put("ARVL", "Arrival");
      m.put("_ATTR","Attribute");
      m.put("BAP","Baptism");
      m.put("BAPM", "Baptism");
      m.put("BAPT", "Baptism");
      m.put("BAPTISM", "Baptism");
      m.put("BARM", "Bar mitzvah");
      m.put("BATM", "Bat mitzvah");
      m.put("BAR_MITZVAH", "Bar mitzvah");
      m.put("BIRT","Birth");
      m.put("BIRTH","Birth");
      m.put("BLES", "Blessing");
      m.put("BURI", "Burial");
      m.put("BURIAL", "Burial");
      m.put("CAST", "Caste");
      m.put("CAUS", "Cause of death");
      m.put("CAUSE", "Cause of death");
      m.put("CENS","Census");
      m.put("CHR", "Christening");
      m.put("CHRISTENING","Christening");
      m.put("CLAW","Common law marriage");
      m.put("_COLOR","Color");
      m.put("CONF", "Confirmation");
      m.put("CREM","Cremation");
      m.put("_DCAUSE","Cause of death");
      m.put("DEAT","Death");
      m.put("DEATH", "Death");
      m.put("_DEATH_OF_SPOUSE","Death of spouse");
      m.put("DEED","Deed");
      m.put("_DEG","Degree");
      m.put("_DEGREE","Degree");
      m.put("DEPA","Departure");
      m.put("DPRT","Departure");
      m.put("DIV","Divorce");
      m.put("DIVF","Divorce filing");
      m.put("DIVORCE","Divorce");
      m.put("_DIV","Divorce");
      m.put("DSCR","Physical description");
      m.put("EDUC","Education");
      m.put("EDUCATION","Education");
      m.put("_ELEC","Elected");
      m.put("EMAIL","Email");
      m.put("EMIG","Emigration");
      m.put("EMIGRATION", "Emigration");
      m.put("EMPL","Employment");
      m.put("_EMPLOY","Employment");
      m.put("ENGA","Engagement");
      m.put("ENLIST","Military");
      m.put("EVEN","Other");
      m.put("EVENT","Other");
      m.put("EYES","Eyes");
      m.put("_EXCM","Excommunication");
      m.put("FCOM","First communion");
      m.put("_FNRL","Funeral");
      m.put("_FUN","Funeral");
      m.put("GRAD","Graduation");
      m.put("GRADUATION","Graduation");
      m.put("HAIR","Hair");
      m.put("HEIG","Height");
      m.put("_HEIG","Height");
      m.put("_HEIGHT","Height");
      m.put("ILL","Illness");
      m.put("IMMI","Immigration");
      m.put("IMMIGRATION","Immigration");
      m.put("MARB","Marriage banns");
      m.put("MARC","Marriage contract");
      m.put("MARL","Marriage license");
      m.put("MARR","Marriage");
      m.put("MARRIAGE","Marriage");
      m.put("MARS","Marriage settlement");
      m.put("_MBON","Marriage banns");
      m.put("_MDCL","Medical");
      m.put("_MEDICAL","Medical");
      m.put("MIL","Military");
      m.put("_MIL","Military");
      m.put("MILI","Military");
      m.put("_MILI","Military");
      m.put("_MILT","Military");
      m.put("_MILTID","Military");
      m.put("_MILITARY_SERVICE","Military");
      m.put("MISE","Military");
      m.put("_MISN","Mission");
      m.put("_NAMS","Namesake");
      m.put("NATI","Nationality");
      m.put("NATU","Naturalization");
      m.put("NATURALIZATION","Naturalization");
      m.put("NCHI","Number of children");
      m.put("OCCU","Occupation");
      m.put("OCCUPATION","Occupation");
      m.put("ORDI","Ordination");
      m.put("ORDN","Ordination");
      m.put("PHON","Phone");
      m.put("PROB","Probate");
      m.put("PROP","Property");
      m.put("RELI","Religion");
      m.put("RELIGION","Religion");
      m.put("RESI","Residence");
      m.put("RESIDENCE","Residence");
      m.put("RETI","Retirement");
      m.put("SEPA","Separated");
      m.put("_SEPARATED","Separated");
      m.put("_SEPR","Separated");
      m.put("SEX","Sex");
      m.put("SSN","Social security number");
      m.put("SOC_","Social security number");
      m.put("SOC_SEC_NUMBER","Social security number");
      m.put("_WEIG","Weight");
      m.put("_WEIGHT","Weight");
      m.put("WILL","Will");
      DISPLAY_TYPE = Collections.unmodifiableMap(m);
   }
   public static final String OTHER_TYPE = "Other";

   private String value = null;
   private String tag = null;
   private String type = null;
   private String date = null;
   private String place = null;
   private Address addr = null;
   private String rin = null;
   private EventFact caus = null;
   private String _uid = null;
   private String uidTag = null;

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   /**
    * Return human-friendly event type
    * @return human-friendly event type
    */
   public String getDisplayType() {
      String key = type != null && type.length() > 0 ? type : tag;
      if (key != null) {
         String displayType = DISPLAY_TYPE.get(key.toUpperCase());
         if (displayType != null) {
            return displayType;
         }
      }
      return OTHER_TYPE;
   }

   public String getTag() {
      return tag;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public String getType() {
      return type;
   }
   
   public void setType(String type) {
      this.type = type;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getPlace() {
      return place;
   }

   public void setPlace(String place) {
      this.place = place;
   }

   public Address getAddress() {
      return addr;
   }

   public void setAddress(Address addr) {
      this.addr = addr;
   }

   public EventFact getCause() {
      return caus;
   }

   public void setCause(EventFact caus) {
      this.caus = caus;
   }

   public String getRin() {
      return rin;
   }

   public void setRin(String rin) {
      this.rin = rin;
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

   @Override
   public void visitContainedObjects(Visitor visitor) {
      if (addr != null) {
         addr.accept(visitor);
      }
      if (caus != null) {
         caus.accept(visitor);
      }
      super.visitContainedObjects(visitor);
   }

   @Override
   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         this.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}

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
 * omit: Phone, Agency, Cause (except for death events)
 * add: Rin, Uid
 */
public class EventFact extends SourceCitationContainer {
   public static final Set<String> PERSONAL_EVENT_FACT_TAGS = new HashSet<String>(Arrays.asList(
           "ADOP", "ADOPTION", "ADULT_CHRISTNG", "AFN", "ARRI", "ARVL", "ARRIVAL", "_ATTR",
           "BAP", "BAPM", "BAPT", "BAPTISM", "BARM", "BAR_MITZVAH", "BASM", "BAS_MITZVAH", "BATM", "BAT_MITZVAH", "BIRT", "BIRTH", "BLES", "BLESS", "BLESSING", "BLSL", "BURI", "BURIAL",
           "CAST", "CASTE", "CAUS", "CENS", "CENSUS", "CHILDREN_COUNT", "CHR", "CHRA", "CHRISTENING", "CIRC", "CITN", "_COLOR",  "CONF", "CONFIRMATION", "CREM", "CREMATION",
           "_DCAUSE", "DEAT", "DEATH", "_DEATH_OF_SPOUSE", "DEED", "_DEG", "_DEGREE", "DEPA", "DPRT", "DSCR", "DWEL",
           "EDUC", "EDUCATION", "_ELEC", "EMAIL", "EMIG", "EMIGRATION", "EMPL", "_EMPLOY", "ENGA", "ENLIST", "EVEN", "EVENT", "_EXCM", "EXCO", "EYES",
           "FACT", "FCOM", "FIRST_COMMUNION", "_FNRL", "_FUN",
           "_FA1", "_FA2", "_FA3", "_FA4", "_FA5", "_FA6", "_FA7", "_FA8", "_FA9", "_FA10", "_FA11", "_FA12", "_FA13",
           "GRAD", "GRADUATION",
           "HAIR", "HEIG", "_HEIG", "_HEIGHT",
           "IDNO", "IDENT_NUMBER", "_INTE",
           "ILL", "ILLN", "IMMI", "IMMIGRATION",
           "LVG", "LVNG",
           "MARR", "MARRIAGE_COUNT", "_MDCL", "_MEDICAL", "MIL", "_MIL", "MILA", "MILD", "MILI", "_MILI", "MILT", "_MILT", "_MILTID", "MISE", "_MISE", "_MILITARY_SERVICE", "MISN ", "_MISN", "MOVE",
           "_NAMS", "NATI", "NATIONALITY", "NATU", "NATURALIZATION", "NCHI", "NMR",
           "OCCU", "OCCUPATION", "ORDI", "ORDL", "ORDN", "ORDINATION",
           "PHON", "PHY_DESCRIPTION", "PROB", "PROBATE", "PROP", "PROPERTY",
           "RACE", "RELI", "RELIGION", "RESI", "RESIR", "RESIDENCE", "RETI", "RETIREMENT",
           "SEX", "SOC_SEC_NUMBER", "SSN", "STIL", "STLB",
           "TITL", "TITLE",
           "WEIG", "_WEIG", "_WEIGHT",
           "WILL"
  ));
   public static final Set<String> FAMILY_EVENT_FACT_TAGS = new HashSet<String>(Arrays.asList(
      "ANUL", "ANNULMENT",
      "CENS", "CLAW",
      "_DEATH_OF_SPOUSE", "DIV", "DIVF", "DIVORCE", "_DIV",
      "EMIG", "ENGA", "EVEN", "EVENT",
      "IMMI",
      "MARB", "MARC", "MARL", "MARR", "MARRIAGE", "MARS", "_MBON",
      "NCHI",
      "RESI",
      "SEPA", "_SEPR", "_SEPARATED"
   ));

   public static final Map<String,String> DISPLAY_TYPE;
   static {
      Map<String, String> m = new HashMap<String, String>();
      m.put("ADOP", "adop");
      m.put("ADOPTION", "adop");
      m.put("AFN", "afn");
      m.put("ANUL", "anul");
      m.put("ANNULMENT", "anul");
      m.put("ARRIVAL", "arvl");
      m.put("ARRI", "arvl");
      m.put("ARVL", "arvl");
      m.put("_ATTR", "attr");
      m.put("BAP", "bapm");
      m.put("BAPM", "bapm");
      m.put("BAPT", "bapm");
      m.put("BAPTISM", "bapm");
      m.put("BARM", "barm");
      m.put("BAR_MITZVAH", "barm");
      m.put("BATM", "batm");
      m.put("BIRT", "birt");
      m.put("BIRTH", "birt");
      m.put("BLES", "bles");
      m.put("BURI", "buri");
      m.put("BURIAL", "buri");
      m.put("CAST", "cast");
      m.put("CAUS", "caus");
      m.put("CAUSE", "caus");
      m.put("CENS", "cens");
      m.put("CHR", "chr");
      m.put("CHRISTENING", "chr");
      m.put("CLAW", "claw");
      m.put("_COLOR", "color");
      m.put("CONF", "conf");
      m.put("CREM", "crem");
      m.put("_DCAUSE", "caus");
      m.put("DEAT", "deat");
      m.put("DEATH", "deat");
      m.put("_DEATH_OF_SPOUSE", "death_of_spouse");
      m.put("DEED", "deed");
      m.put("_DEG", "deg");
      m.put("_DEGREE", "deg");
      m.put("DEPA", "dprt");
      m.put("DPRT", "dprt");
      m.put("DIV", "div");
      m.put("DIVF", "divf");
      m.put("DIVORCE", "div");
      m.put("_DIV", "div");
      m.put("DSCR", "dscr");
      m.put("EDUC", "educ");
      m.put("EDUCATION", "educ");
      m.put("_ELEC", "elec");
      m.put("EMAIL", "email");
      m.put("EMIG", "emig");
      m.put("EMIGRATION", "emig");
      m.put("EMPL", "empl");
      m.put("_EMPLOY", "empl");
      m.put("ENGA", "enga");
      m.put("ENLIST", "milt");
      m.put("EVEN", "even");
      m.put("EVENT", "even");
      m.put("_EXCM", "excm");
      m.put("EYES", "eyes");
      m.put("FCOM", "fcom");
      m.put("_FNRL", "fnrl");
      m.put("_FUN", "fnrl");
      m.put("GRAD", "grad");
      m.put("GRADUATION", "grad");
      m.put("HAIR", "hair");
      m.put("HEIG", "heig");
      m.put("_HEIG", "heig");
      m.put("_HEIGHT", "heig");
      m.put("ILL", "ill");
      m.put("IMMI", "immi");
      m.put("IMMIGRATION", "immi");
      m.put("MARB", "marb");
      m.put("MARC", "marc");
      m.put("MARL", "marl");
      m.put("MARR", "marr");
      m.put("MARRIAGE", "marr");
      m.put("MARS", "mars");
      m.put("_MBON", "marb");
      m.put("_MDCL", "mdcl");
      m.put("_MEDICAL", "mdcl");
      m.put("MIL", "milt");
      m.put("_MIL", "milt");
      m.put("MILI", "milt");
      m.put("_MILI", "milt");
      m.put("_MILT", "milt");
      m.put("_MILTID", "milt");
      m.put("_MILITARY_SERVICE", "milt");
      m.put("MISE", "milt");
      m.put("_MISN", "misn");
      m.put("_NAMS", "nams");
      m.put("NATI", "nati");
      m.put("NATU", "natu");
      m.put("NATURALIZATION", "natu");
      m.put("NCHI", "nchi");
      m.put("OCCU", "occu");
      m.put("OCCUPATION", "occu");
      m.put("ORDI", "ordn");
      m.put("ORDN", "ordn");
      m.put("PHON", "phon");
      m.put("PROB", "prob");
      m.put("PROP", "prop");
      m.put("RELI", "reli");
      m.put("RELIGION", "reli");
      m.put("RESI", "resi");
      m.put("RESIDENCE", "resi");
      m.put("RETI", "reti");
      m.put("SEPA", "sepa");
      m.put("_SEPARATED", "sepa");
      m.put("_SEPR", "sepa");
      m.put("SEX", "sex");
      m.put("SSN", "ssn");
      m.put("SOC_SEC_NUMBER", "ssn");
      m.put("TITL", "titl");
      m.put("TITLE", "titl");
      m.put("_WEIG", "weig");
      m.put("_WEIGHT", "weig");
      m.put("WILL", "will");
      DISPLAY_TYPE = Collections.unmodifiableMap(m);
   }
   @Deprecated
   public static final String OTHER_TYPE = "Other";

   private String value = null;
   private String tag = null;
   private String type = null;
   private String date = null;
   private String place = null;
   private Address addr = null;
   private String age = null;
   private Spouse husb = null;
   private Spouse wife = null;
   private String phon = null;
   private String fax = null;
   private String rin = null;
   private String caus = null;
   private String _uid = null;
   private String uidTag = null;
   private String _email = null;
   private String emailTag = null;
   private String _www = null;
   private String wwwTag = null;

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
      ResourceBundle resourceBundle = ResourceBundle.getBundle("EventFact", Locale.getDefault());
      if (tag != null) {
         String key = DISPLAY_TYPE.get(tag.toUpperCase());
         if (key != null) {
            return resourceBundle.getString(key);
         }
      }
      return resourceBundle.getString("other");
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

   public String getAge() {
      return age;
   }

   public void setAge(String age) {
      this.age = age;
   }

   public Spouse getHusband() {
      return husb;
   }

   public void setHusband(Spouse husb) {
      this.husb = husb;
   }

   public Spouse getWife() {
      return wife;
   }

   public void setWife(Spouse wife) {
      this.wife = wife;
   }

   public String getPhone() {
      return phon;
   }

   public void setPhone(String phon) {
      this.phon = phon;
   }

   public String getFax() {
      return fax;
   }

   public void setFax(String fax) {
      this.fax = fax;
   }

   public String getCause() {
      return caus;
   }

   public void setCause(String caus) {
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

   public String getEmail() {
      return _email;
   }

   public void setEmail(String email) {
      this._email = email;
   }

   public String getEmailTag() {
      return emailTag;
   }

   public void setEmailTag(String emailTag) {
      this.emailTag = emailTag;
   }

   public String getWww() {
      return _www;
   }

   public void setWww(String www) {
      this._www = www;
   }

   public String getWwwTag() {
      return wwwTag;
   }

   public void setWwwTag(String wwwTag) {
      this.wwwTag = wwwTag;
   }

   @Override
   public void visitContainedObjects(Visitor visitor) {
      if (addr != null) {
         addr.accept(visitor);
      }
      if (husb != null) {
         husb.accept(visitor, true);
      }
      if (wife != null) {
         wife.accept(visitor, false);
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

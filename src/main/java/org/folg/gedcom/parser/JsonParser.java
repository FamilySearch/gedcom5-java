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

package org.folg.gedcom.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.GedcomTag;

import java.util.List;

/**
 * User: dallan
 * Date: 1/2/12
 */
public class JsonParser {
   private Gson gson;

   public JsonParser() {
      gson = new GsonBuilder()
                 .setPrettyPrinting()
                 .registerTypeAdapter(Gedcom.class, new GedcomTypeAdapter())
                 .create();
   }

   /**
    * Convert json-encoded string to Gedcom object
    * @param json json-encoded string
    * @return Gedcom object
    */
   public Gedcom fromJson(String json) {
      return gson.fromJson(json, Gedcom.class);
   }

   /**
    * Convert Gedcom object to json
    * @param gedcom Gedcom
    * @return json-encoded string
    */
   public String toJson(Gedcom gedcom) {
      return gson.toJson(gedcom);
   }

   /**
    * Convert output of TreeParser to json
    * @param gedcomTags output of TreeParser
    * @return json-encoded string
    */
   public String toJson(List<GedcomTag> gedcomTags) {
      return gson.toJson(gedcomTags);
   }
}

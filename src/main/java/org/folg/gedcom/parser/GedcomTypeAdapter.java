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

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.folg.gedcom.model.Extensions;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.GedcomTag;

import java.lang.reflect.Type;
import java.util.List;

/**
 * User: Dallan
 * Date: 12/31/11
 */
public class GedcomTypeAdapter implements JsonSerializer<Gedcom>, JsonDeserializer<Gedcom> {

   private Gson gson;
   private ExtensionsTypeAdapter extensionsTypeAdapter;

   public GedcomTypeAdapter() {
      extensionsTypeAdapter = new ExtensionsTypeAdapter();
      // built-in extension
      extensionsTypeAdapter.registerExtension(ModelParser.MORE_TAGS_EXTENSION_KEY, new TypeToken<List<GedcomTag>>(){}.getType());
      gson = new GsonBuilder()
              .registerTypeAdapter(Extensions.class, extensionsTypeAdapter)
              .create();
   }

   public void registerExtension(String extensionKey, Class clazz) {
      extensionsTypeAdapter.registerExtension(extensionKey, clazz);
   }

   public void registerExtension(String extensionKey, Type type) {
      extensionsTypeAdapter.registerExtension(extensionKey, type);
   }

   public JsonElement serialize(Gedcom src, Type typeOfSrc, JsonSerializationContext context) {
      return gson.toJsonTree(src);
   }

   public Gedcom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      Gedcom gedcom = gson.fromJson(json, Gedcom.class);
      gedcom.createIndexes();
      return gedcom;
   }
}

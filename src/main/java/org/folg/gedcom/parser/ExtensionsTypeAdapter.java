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
import org.folg.gedcom.model.Extensions;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Dallan
 * Date: 12/18/11
 */
public class ExtensionsTypeAdapter implements JsonSerializer<Extensions>, JsonDeserializer<Extensions> {
   private Gson gson;
   private Map<String,Class> classExtensions;
   private Map<String,Type> typeExtensions;

   public ExtensionsTypeAdapter() {
      gson = new Gson();
      classExtensions = new HashMap<String, Class>();
      typeExtensions = new HashMap<String, Type>();
   }

   public void registerExtension(String extensionKey, Class clazz) {
      classExtensions.put(extensionKey, clazz);
   }

   public void registerExtension(String extensionKey, Type type) {
      typeExtensions.put(extensionKey, type);
   }

   public JsonElement serialize(Extensions src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject o = new JsonObject();
      for (Map.Entry<String, Object> entry : src.getExtensions().entrySet()) {
         String key = entry.getKey();
         Object extension = entry.getValue();
         JsonElement elm = (extension instanceof JsonElement ? (JsonElement) extension : gson.toJsonTree(extension));
         o.add(key, elm);
      }
      return o;
   }

   @SuppressWarnings("unchecked")
   public Extensions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonObject o = json.getAsJsonObject();
      Extensions extensions = new Extensions();
      for (Map.Entry<String, JsonElement> entry : o.entrySet()) {
         String key = entry.getKey();
         JsonElement jsonElement = entry.getValue();
         Object extension;
         Type type = typeExtensions.get(key);
         if (type != null) {
            extension = gson.fromJson(jsonElement, type);
         }
         else {
            Class clazz = classExtensions.get(key);
            if (clazz != null) {
               extension = gson.fromJson(jsonElement, clazz);
            }
            else {
               extension = jsonElement;
            }
         }
         extensions.put(key, extension);
      }
      return extensions;
   }
}

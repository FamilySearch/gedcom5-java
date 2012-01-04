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

import java.util.Collections;
import java.util.Map;

/**
 * User: Dallan
 * Date: 12/25/11
 */
public abstract class ExtensionContainer implements Visitable {
   private Extensions extensions = null;

   public Map<String,Object> getExtensions() {
      return extensions != null ? extensions.getExtensions() : Collections.<String,Object>emptyMap();
   }
   
   public Object getExtension(String key) {
      return extensions != null ? extensions.get(key) : null;
   }

   public void setExtensions(Map<String, Object> exts) {
      if (exts != null && exts.size() > 0) {
         if (extensions == null) {
            extensions = new Extensions();
         }
         extensions.setExtensions(exts);
      }
      else {
         extensions = null;
      }
   }
   
   public void putExtension(String id, Object extension) {
      if (extensions == null) {
         extensions = new Extensions();
      }
      extensions.put(id, extension);
   }

   public void visitContainedObjects(Visitor visitor) {
      for (Map.Entry<String, Object> entry : getExtensions().entrySet()) {
         visitor.visit(entry.getKey(), entry.getValue());
      }
   }
}

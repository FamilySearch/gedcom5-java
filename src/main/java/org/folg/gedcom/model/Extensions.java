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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: Dallan
 * Date: 12/18/11
 *
 * Make Extensions a class so we can use an ExtensionsTypeAdapter with Gson
 */
public class Extensions {
    private Map<String, Object> extensions;
    
    public Extensions() {
        extensions = new HashMap<String, Object>();
    }
    
    public Map<String,Object> getExtensions() {
        return extensions;
    }
    
    public void setExtensions(Map<String,Object> extensions) {
        this.extensions = extensions;
    }

    public Object get(String key) {
        return extensions.get(key);
    }

    public void put(String key, Object extension) {
        extensions.put(key, extension);
    }

    public Set<String> getKeys() {
        return extensions.keySet();
    }
}

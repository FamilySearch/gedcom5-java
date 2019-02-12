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

import java.io.*;
import java.nio.charset.Charset;
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
     *
     * @param json json-encoded string
     * @return Gedcom object
     */
    public Gedcom fromJson(String json) {
        return gson.fromJson(json, Gedcom.class);
    }

    /**
     * Read Gedcom object from json character stream
     *
     * @param reader json source reader
     * @return Gedcom object
     */
    public Gedcom fromJson(Reader reader) {
        return gson.fromJson(reader, Gedcom.class);
    }

    /**
     * Read Gedcom object from json byte stream
     *
     * @param is json source stream
     * @return Gedcom object
     */
    public Gedcom fromJson(InputStream is) {
        InputStreamReader reader = new InputStreamReader(is, Charset.forName("UTF-8"));
        return gson.fromJson(reader, Gedcom.class);
    }

    /**
     * Convert Gedcom object to json
     *
     * @param gedcom Gedcom
     * @return json-encoded string
     */
    public String toJson(Gedcom gedcom) {
        return gson.toJson(gedcom);
    }

    /**
     * Convert output of TreeParser to json
     *
     * @param gedcomTags output of TreeParser
     * @return json-encoded string
     */
    public String toJson(List<GedcomTag> gedcomTags) {
        return gson.toJson(gedcomTags);
    }

    /**
     * Write Gedcom object to json character stream
     *
     * @param gedcom Gedcom object
     * @param writer json output writer
     */
    public void write(Gedcom gedcom, Writer writer) {
        gson.toJson(gedcom, writer);
        close(writer);
    }

    /**
     * Write Gedcom object to json byte stream
     *
     * @param gedcom Gedcom object
     * @param os     json output stream
     */
    public void write(Gedcom gedcom, OutputStream os) {
        OutputStreamWriter writer = new OutputStreamWriter(os, Charset.forName("UTF-8"));
        gson.toJson(gedcom, writer);
        close(writer);
    }

    /**
     * Write Gedcom Tags from TreeParser to json character stream
     *
     * @param gedcomTags Gedcom tags
     * @param writer     json output writer
     */
    public void write(List<GedcomTag> gedcomTags, Writer writer) {
        gson.toJson(gedcomTags, writer);
        close(writer);
    }

    /**
     * Write Gedcom Tags from TreeParser to json byte stream
     *
     * @param gedcomTags Gedcom tags
     * @param os         json output stream
     */
    public void write(List<GedcomTag> gedcomTags, OutputStream os) {
        OutputStreamWriter writer = new OutputStreamWriter(os, Charset.forName("UTF-8"));
        gson.toJson(gedcomTags, writer);
        close(writer);
    }

    private void close(Writer writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Closing writer failed", e);
        }
    }

}

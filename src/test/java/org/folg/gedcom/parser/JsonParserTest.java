package org.folg.gedcom.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.folg.gedcom.model.*;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;

import static org.testng.Assert.*;

public class JsonParserTest {

    @Test
    public void testToJson_gedcom() {
        String expectedLanguage = "English";
        String expectedCopyright = "2019";
        String expectedName = "Bobby /ROBERTSON/";
        String expectedId = "I1";

        Header header = new Header();
        header.setLanguage(expectedLanguage);
        header.setCopyright(expectedCopyright);

        Name name = new Name();
        name.setValue(expectedName);
        Person individual = new Person();
        individual.setId(expectedId);
        individual.setNames(Collections.singletonList(name));

        Gedcom gedcom = new Gedcom();
        gedcom.setHeader(header);
        gedcom.addPerson(individual);

        JsonParser jsonParser = new JsonParser();
        String json = jsonParser.toJson(gedcom);
        assertNotNull(json);

        com.google.gson.JsonParser gsonParser = new com.google.gson.JsonParser();
        JsonElement jsonElement = gsonParser.parse(json);
        assertNotNull(jsonElement);
        assertTrue(jsonElement.isJsonObject());

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonElement head = jsonObject.get("head");
        assertNotNull(head);
        assertTrue(head.isJsonObject());

        JsonObject headObj = head.getAsJsonObject();

        JsonElement copr = headObj.get("copr");
        assertNotNull(copr);
        assertTrue(copr.isJsonPrimitive());
        assertEquals(expectedCopyright, copr.getAsString());

        JsonElement lang = headObj.get("lang");
        assertNotNull(lang);
        assertTrue(lang.isJsonPrimitive());
        assertEquals(expectedLanguage, lang.getAsString());

        JsonElement people = jsonObject.get("people");
        assertNotNull(people);
        assertTrue(people.isJsonArray());

        JsonArray peopleArray = people.getAsJsonArray();
        assertEquals(1, peopleArray.size());

        JsonElement indi = peopleArray.get(0);
        assertNotNull(indi);
        assertTrue(indi.isJsonObject());

        JsonObject indiObj = indi.getAsJsonObject();

        JsonElement id = indiObj.get("id");
        assertNotNull(id);
        assertTrue(id.isJsonPrimitive());
        assertEquals(expectedId, id.getAsString());

        JsonElement names = indiObj.get("names");
        assertNotNull(names);
        assertTrue(names.isJsonArray());

        JsonArray namesArray = names.getAsJsonArray();
        assertEquals(1, namesArray.size());

        JsonElement nameElt = namesArray.get(0);
        assertNotNull(nameElt);
        assertTrue(nameElt.isJsonObject());

        JsonObject nameObj = nameElt.getAsJsonObject();

        JsonElement nameValue = nameObj.get("value");
        assertNotNull(nameValue);
        assertTrue(nameValue.isJsonPrimitive());
        assertEquals(expectedName, nameValue.getAsString());
    }

    @Test
    public void testToJson_gedcomTags() {
        GedcomTag tag = new GedcomTag("", "head", "");
        JsonParser jsonParser = new JsonParser();
        String json = jsonParser.toJson(Collections.singletonList(tag));

        assertGedcomTagsJson(json);
    }

    @Test
    public void testFromJson() {
        Header header = new Header();
        header.setLanguage("English");
        header.setCopyright("2019");

        Gedcom gedcom = new Gedcom();
        gedcom.setHeader(header);

        JsonParser jsonParser = new JsonParser();
        String json = jsonParser.toJson(gedcom);

        assertGedcomJson(gedcom, json);
    }

    @Test
    public void testFromJson_withReader() {
        Header header = new Header();
        header.setLanguage("English");
        header.setCopyright("2019");

        Gedcom gedcom = new Gedcom();
        gedcom.setHeader(header);

        JsonParser jsonParser = new JsonParser();
        String json = jsonParser.toJson(gedcom);

        StringReader reader = new StringReader(json);
        Gedcom actualGedcom = jsonParser.fromJson(reader);
        assertNotNull(actualGedcom);
        assertEquals(gedcom.getHeader().getLanguage(), actualGedcom.getHeader().getLanguage());
        assertEquals(gedcom.getHeader().getCopyright(), actualGedcom.getHeader().getCopyright());
    }

    @Test
    public void testFromJson_withInputStream() {
        Header header = new Header();
        header.setLanguage("English");
        header.setCopyright("2019");

        Gedcom gedcom = new Gedcom();
        gedcom.setHeader(header);

        JsonParser jsonParser = new JsonParser();
        String json = jsonParser.toJson(gedcom);

        ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8")));
        Gedcom actualGedcom = jsonParser.fromJson(is);
        assertNotNull(actualGedcom);
        assertEquals(gedcom.getHeader().getLanguage(), actualGedcom.getHeader().getLanguage());
        assertEquals(gedcom.getHeader().getCopyright(), actualGedcom.getHeader().getCopyright());
    }

    @Test
    public void testWrite_withGedcomAndWriter() {
        Header header = new Header();
        header.setLanguage("English");
        header.setCopyright("2019");

        Gedcom gedcom = new Gedcom();
        gedcom.setHeader(header);

        Writer writer = new StringWriter();
        JsonParser jsonParser = new JsonParser();
        jsonParser.write(gedcom, writer);

        assertGedcomJson(gedcom, writer.toString());
    }

    @Test
    public void testWrite_withGedcomAndOutputStream() {
        Header header = new Header();
        header.setLanguage("English");
        header.setCopyright("2019");

        Gedcom gedcom = new Gedcom();
        gedcom.setHeader(header);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JsonParser jsonParser = new JsonParser();
        jsonParser.write(gedcom, os);

        assertGedcomJson(gedcom, os.toString());
    }

    @Test
    public void testWrite_withGedcomTagsAndWriter() {
        GedcomTag tag = new GedcomTag("", "head", "");

        Writer writer = new StringWriter();
        JsonParser jsonParser = new JsonParser();
        jsonParser.write(Collections.singletonList(tag), writer);

        assertGedcomTagsJson(writer.toString());
    }

    @Test
    public void testWrite_withGedcomTagsAndOutputStream() {
        GedcomTag tag = new GedcomTag("", "head", "");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JsonParser jsonParser = new JsonParser();
        jsonParser.write(Collections.singletonList(tag), os);

        assertGedcomTagsJson(os.toString());
    }

    private void assertGedcomJson(Gedcom gedcom, String json) {
        JsonParser jsonParser = new JsonParser();
        Gedcom actualGedcom = jsonParser.fromJson(json);
        assertNotNull(actualGedcom);
        assertEquals(gedcom.getHeader().getLanguage(), actualGedcom.getHeader().getLanguage());
        assertEquals(gedcom.getHeader().getCopyright(), actualGedcom.getHeader().getCopyright());
    }

    private void assertGedcomTagsJson(String json) {
        com.google.gson.JsonParser gsonParser = new com.google.gson.JsonParser();
        JsonElement jsonElement = gsonParser.parse(json);
        assertNotNull(jsonElement);
        assertTrue(jsonElement.isJsonArray());

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        JsonElement head = jsonArray.get(0);
        assertNotNull(head);
        assertTrue(head.isJsonObject());

        JsonObject headObj = head.getAsJsonObject();

        JsonElement jsonTag = headObj.get("tag");
        assertNotNull(jsonTag);
        assertTrue(jsonTag.isJsonPrimitive());
        assertEquals("head", jsonTag.getAsString());
    }

}

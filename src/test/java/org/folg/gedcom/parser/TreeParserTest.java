package org.folg.gedcom.parser;

import org.folg.gedcom.model.GedcomTag;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class TreeParserTest {

    @Test
    public void testParse_withFile() throws Exception {
        URL gedcomUrl = this.getClass().getClassLoader().getResource("Case001-AddressStructure.ged");
        File gedcomFile = new File(gedcomUrl.toURI());
        TreeParser treeParser = new TreeParser();

        List<GedcomTag> gedcomTags = treeParser.parseGedcom(gedcomFile);
        assertNotNull(gedcomTags);
        assertEquals(4, gedcomTags.size());

        GedcomTag headTag = gedcomTags.get(0);
        assertNotNull(headTag);
        assertEquals("HEAD", headTag.getTag());

        GedcomTag submissionTag = gedcomTags.get(1);
        assertNotNull(submissionTag);
        assertEquals("SUBM", submissionTag.getTag());

        GedcomTag individualTag = gedcomTags.get(2);
        assertNotNull(individualTag);
        assertEquals("INDI", individualTag.getTag());

        GedcomTag repoTag = gedcomTags.get(3);
        assertNotNull(repoTag);
        assertEquals("REPO", repoTag.getTag());
    }

    @Test
    public void testParse_withInputStream() throws Exception {
        URL gedcomUrl = this.getClass().getClassLoader().getResource("Case001-AddressStructure.ged");
        TreeParser treeParser = new TreeParser();

        InputStream is = gedcomUrl.openStream();
        assertNotNull(is);

        List<GedcomTag> gedcomTags = treeParser.parseGedcom(is);
        assertNotNull(gedcomTags);
        assertEquals(4, gedcomTags.size());
    }

    @Test
    public void testParse_withReader() throws Exception {
        URL gedcomUrl = this.getClass().getClassLoader().getResource("Case001-AddressStructure.ged");
        TreeParser treeParser = new TreeParser();

        InputStream is = gedcomUrl.openStream();
        assertNotNull(is);

        Reader reader = new InputStreamReader(is, "UTF-8");

        List<GedcomTag> gedcomTags = treeParser.parseGedcom(reader);
        assertNotNull(gedcomTags);
        assertEquals(4, gedcomTags.size());
    }

}

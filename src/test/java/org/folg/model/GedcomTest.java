package org.folg.model;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class GedcomTest {
    @Test
    public void testUpdateReferenceIntegrity() throws Exception {
        URL gedcomUrl = this.getClass().getClassLoader().getResource("Case002-ReferenceIntegrity.ged");
        ModelParser modelParser = new ModelParser();

        InputStream is = gedcomUrl.openStream();
        assertNotNull(is);

        Gedcom gedcom = modelParser.parseGedcom(is);
        assertNotNull(gedcom);

        gedcom.createIndexes();
        gedcom.updateReferences();
        assertNotNull(gedcom);

        assertEquals(gedcom.getPerson("I2").getSpouseFamilyRefs().size(), 3);
        assertEquals(gedcom.getFamily("F1").getHusbandRefs().size(), 1);
        assertEquals(gedcom.getFamily("F1").getWifeRefs().size(), 1);
        assertEquals(gedcom.getFamily("F2").getChildRefs().size(), 1);
    }
}

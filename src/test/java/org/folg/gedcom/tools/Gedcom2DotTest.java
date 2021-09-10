package org.folg.gedcom.tools;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXParseException;

public class Gedcom2DotTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Test
	public void simpleRun() throws URISyntaxException, SAXParseException, IOException {
	    URL gedcomUrl = this.getClass().getClassLoader().getResource("Muster_GEDCOM_UTF-8.ged");
	    File gedcomFile = new File(gedcomUrl.toURI());
	    
	    Gedcom2Dot g2d = new Gedcom2Dot();
	    g2d.setRootId("I1");
	    g2d.setGedcomIn(gedcomFile);
	    g2d.setDotOut(new File(folder.getRoot().getAbsolutePath() + "/out.dot"));
	    g2d.process();
	}

	@Test
	public void list() throws URISyntaxException, SAXParseException, IOException {
	    URL gedcomUrl = this.getClass().getClassLoader().getResource("Muster_GEDCOM_UTF-8.ged");
	    File gedcomFile = new File(gedcomUrl.toURI());
	    
	    Gedcom2Dot g2d = new Gedcom2Dot();
	    g2d.setListMode(true);
	    g2d.setGedcomIn(gedcomFile);
	    g2d.list();
	}

}

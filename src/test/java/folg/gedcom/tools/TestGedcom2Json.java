package folg.gedcom.tools;

import junit.framework.*;
import org.folg.gedcom.tools.Gedcom2Json;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * <Description>
 *
 * @author DaveySE
 *         Creation Date: 4/30/12
 */
public class TestGedcom2Json extends TestCase {

  public void testSimpleSmoke() {
    URL inUrl = this.getClass().getResource("/Simple_Test.ged");
    assertNotNull(inUrl);
    String inFilePath = inUrl.getFile();
    File outFile = new File((new File(inFilePath)).getParent(),"Simple_Test.json");
    String[] args = {"-i",inFilePath,"-o",outFile.getAbsolutePath()};
    try {
      Gedcom2Json.main(args);
      assertTrue(outFile.exists());
    }
    catch (IOException ioe) {
      fail("IOException thrown: " + ioe.getMessage());
    }
    catch (SAXParseException spe) {
      fail("SAXParseException thrown: " + spe.getMessage());
    }
  }

}

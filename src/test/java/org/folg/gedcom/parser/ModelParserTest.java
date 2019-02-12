package org.folg.gedcom.parser;

import org.folg.gedcom.model.*;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class ModelParserTest {
  @Test
  public void testAddressStructureParsing() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case001-AddressStructure.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    Gedcom gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);

    gedcom.createIndexes();

    assertNotNull(gedcom.getHeader());
    assertNotNull(gedcom.getHeader().getGenerator());
    GeneratorCorporation generatorCorporation = gedcom.getHeader().getGenerator().getGeneratorCorporation();
    assertNotNull(generatorCorporation);
    assertEquals(generatorCorporation.getAddress().getValue()
      , "5000 MyCorpCampus Dr\n" +
        "Hometown, ZZ  99999\n" +
        "United States");
    assertEquals(generatorCorporation.getAddress().getAddressLine1(), "__ADR1_VALUE__");
    assertEquals(generatorCorporation.getAddress().getAddressLine2(), "__ADR2_VALUE__");
    assertEquals(generatorCorporation.getAddress().getAddressLine3(), "5000 MyCorpCampus Dr");
    assertEquals(generatorCorporation.getAddress().getCity(), "Hometown");
    assertEquals(generatorCorporation.getAddress().getState(), "ZZ");
    assertEquals(generatorCorporation.getAddress().getPostalCode(), "99999");
    assertEquals(generatorCorporation.getAddress().getCountry(), "United States");
    assertEquals(generatorCorporation.getPhone(), "866-000-0000");
    assertEquals(generatorCorporation.getEmail(), "info@mycorporation.com");
    assertEquals(generatorCorporation.getFax(), "866-111-1111");
    assertEquals(generatorCorporation.getWww(), "http://www.mycorporation.org/");

    Submitter submitter = gedcom.getSubmitter("SUB1");
    assertNotNull(submitter);
    assertEquals(submitter.getAddress().getValue()
      , "5000 MyCorpCampus Dr\n" +
        "Hometown, ZZ  99999\n" +
        "United States");
    assertEquals(submitter.getAddress().getAddressLine1(), "__ADR1_VALUE__");
    assertEquals(submitter.getAddress().getAddressLine2(), "__ADR2_VALUE__");
    assertEquals(submitter.getAddress().getAddressLine3(), "5000 MyCorpCampus Dr");
    assertEquals(submitter.getAddress().getCity(), "Hometown");
    assertEquals(submitter.getAddress().getState(), "ZZ");
    assertEquals(submitter.getAddress().getPostalCode(), "99999");
    assertEquals(submitter.getAddress().getCountry(), "United States");
    assertEquals(submitter.getPhone(), "866-000-0000");
    assertEquals(submitter.getEmail(), "info@mycorporation.com");
    assertEquals(submitter.getFax(), "866-111-1111");
    assertEquals(submitter.getWww(), "http://www.mycorporation.org/");

    assertNotNull(gedcom.getPeople());
    assertEquals(gedcom.getPeople().size(), 1);
    Person person = gedcom.getPeople().get(0);
    assertNotNull(person);
    assertEquals(person.getAddress().getValue()
      , "5000 MyCorpCampus Dr\n" +
      "Hometown, ZZ  99999\n" +
      "United States");
    assertEquals(person.getAddress().getAddressLine1(), "__ADR1_VALUE__");
    assertEquals(person.getAddress().getAddressLine2(), "__ADR2_VALUE__");
    assertEquals(person.getAddress().getAddressLine3(), "5000 MyCorpCampus Dr");
    assertEquals(person.getAddress().getCity(), "Hometown");
    assertEquals(person.getAddress().getState(), "ZZ");
    assertEquals(person.getAddress().getPostalCode(), "99999");
    assertEquals(person.getAddress().getCountry(), "United States");
    assertEquals(person.getPhone(), "866-000-0000");
    assertEquals(person.getEmail(), "info@mycorporation.com");
    assertEquals(person.getFax(), "866-111-1111");
    assertEquals(person.getWww(), "http://www.mycorporation.org/");

    assertNotNull(person.getEventsFacts());
    assertEquals(person.getEventsFacts().size(), 1);
    EventFact eventFact = person.getEventsFacts().get(0);
    assertEquals(eventFact.getAddress().getValue()
      , "Arlington National Cemetery\n" +
        "State Hwy 110 & Memorial Dr\n" +
        "Arlington, VA  22211\n" +
        "United States");
    assertEquals(eventFact.getAddress().getAddressLine1(), "__ADR1_VALUE__");
    assertEquals(eventFact.getAddress().getAddressLine2(), "__ADR2_VALUE__");
    assertEquals(eventFact.getAddress().getAddressLine3(), "__ADR3_VALUE__");
    assertEquals(eventFact.getAddress().getCity(), "Arlington");
    assertEquals(eventFact.getAddress().getState(), "VA");
    assertEquals(eventFact.getAddress().getPostalCode(), "22211");
    assertEquals(eventFact.getAddress().getCountry(), "United States");
    assertEquals(eventFact.getPhone(), "877-907-8585");
    assertEquals(eventFact.getEmail(), "info@arlingtoncemetery.mil");
    assertEquals(eventFact.getFax(), "877-111-1111");
    assertEquals(eventFact.getWww(), "http://www.arlingtoncemetery.mil/");

    assertNotNull(gedcom.getRepositories());
    assertEquals(gedcom.getRepositories().size(), 1);
    Repository repository = gedcom.getRepositories().get(0);
    assertEquals(repository.getAddress().getValue()
      , "5000 MyCorpCampus Dr\n" +
        "Hometown, ZZ  99999\n" +
        "United States");
    assertEquals(repository.getAddress().getAddressLine1(), "__ADR1_VALUE__");
    assertEquals(repository.getAddress().getAddressLine2(), "__ADR2_VALUE__");
    assertEquals(repository.getAddress().getAddressLine3(), "5000 MyCorpCampus Dr");
    assertEquals(repository.getAddress().getCity(), "Hometown");
    assertEquals(repository.getAddress().getState(), "ZZ");
    assertEquals(repository.getAddress().getPostalCode(), "99999");
    assertEquals(repository.getAddress().getCountry(), "United States");
    assertEquals(repository.getPhone(), "866-000-0000");
    assertEquals(repository.getEmail(), "info@mycorporation.com");
    assertEquals(repository.getFax(), "866-111-1111");
    assertEquals(repository.getWww(), "https://www.mycorporation.com/");
  }

  @Test
  public void testParse_withInputStream() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case001-AddressStructure.ged");
    ModelParser modelParser = new ModelParser();

    InputStream is = gedcomUrl.openStream();
    assertNotNull(is);

    Gedcom gedcom = modelParser.parseGedcom(is);
    assertNotNull(gedcom);
  }

  @Test
  public void testParse_withReader() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case001-AddressStructure.ged");
    ModelParser modelParser = new ModelParser();

    InputStream is = gedcomUrl.openStream();
    assertNotNull(is);

    Reader reader = new InputStreamReader(is, "UTF-8");

    Gedcom gedcom = modelParser.parseGedcom(reader);
    assertNotNull(gedcom);
  }

}

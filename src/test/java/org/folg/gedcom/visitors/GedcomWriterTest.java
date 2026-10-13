package org.folg.gedcom.visitors;

import org.folg.gedcom.model.*;
import org.folg.gedcom.parser.ModelParser;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class GedcomWriterTest {
  @Test
  public void testContactStringsRoundTrip() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case001-AddressStructure.ged");
    Gedcom gedcom = new ModelParser().parseGedcom(new File(gedcomUrl.toURI()));
    String output = write(gedcom);

    // contact strings follow ADDR (and its children) at the same level
    assertTrue(output.contains("4 CTRY United States\n3 PHON 866-000-0000\n3 EMAIL info@mycorporation.com\n3 FAX 866-111-1111\n3 WWW http://www.mycorporation.org/\n"), output);
    assertTrue(output.contains("3 CTRY United States\n2 PHON 877-907-8585\n2 EMAIL info@arlingtoncemetery.mil\n2 FAX 877-111-1111\n2 WWW http://www.arlingtoncemetery.mil/\n"), output);

    Gedcom reparsed = new ModelParser().parseGedcom(new java.io.ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8)));
    reparsed.createIndexes();

    GeneratorCorporation corp = reparsed.getHeader().getGenerator().getGeneratorCorporation();
    assertContacts(corp.getPhone(), corp.getEmail(), corp.getFax(), corp.getWww(),
        "866-000-0000", "info@mycorporation.com", "866-111-1111", "http://www.mycorporation.org/");

    Submitter submitter = reparsed.getSubmitter("SUB1");
    assertContacts(submitter.getPhone(), submitter.getEmail(), submitter.getFax(), submitter.getWww(),
        "866-000-0000", "info@mycorporation.com", "866-111-1111", "http://www.mycorporation.org/");

    Person person = reparsed.getPeople().get(0);
    assertContacts(person.getPhone(), person.getEmail(), person.getFax(), person.getWww(),
        "866-000-0000", "info@mycorporation.com", "866-111-1111", "http://www.mycorporation.org/");

    EventFact burial = person.getEventsFacts().get(0);
    assertContacts(burial.getPhone(), burial.getEmail(), burial.getFax(), burial.getWww(),
        "877-907-8585", "info@arlingtoncemetery.mil", "877-111-1111", "http://www.arlingtoncemetery.mil/");

    Repository repository = reparsed.getRepository("REPO3");
    assertContacts(repository.getPhone(), repository.getEmail(), repository.getFax(), repository.getWww(),
        "866-000-0000", "info@mycorporation.com", "866-111-1111", "https://www.mycorporation.com/");
  }

  @Test
  public void testContactStringsWithoutTagsOrAddress() throws Exception {
    EventFact eventFact = new EventFact();
    eventFact.setTag("BURI");
    eventFact.setPhone("555-1234");
    eventFact.setEmail("a@b.com");
    eventFact.setWww("http://example.com/");
    Person person = new Person();
    person.setId("I1");
    person.setFax("555-9999");
    person.addEventFact(eventFact);
    Gedcom gedcom = new Gedcom();
    gedcom.addPerson(person);

    String output = write(gedcom);
    assertTrue(output.contains("1 BURI\n2 PHON 555-1234\n2 EMAIL a@b.com\n2 WWW http://example.com/\n"), output);
    assertTrue(output.contains("1 FAX 555-9999\n"), output);
  }

  private static String write(Gedcom gedcom) throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    new GedcomWriter().write(gedcom, out);
    return out.toString(StandardCharsets.UTF_8.name()).replace("\r\n", "\n");
  }

  private static void assertContacts(String phone, String email, String fax, String www,
                                     String expectedPhone, String expectedEmail, String expectedFax, String expectedWww) {
    assertEquals(phone, expectedPhone);
    assertEquals(email, expectedEmail);
    assertEquals(fax, expectedFax);
    assertEquals(www, expectedWww);
  }
}

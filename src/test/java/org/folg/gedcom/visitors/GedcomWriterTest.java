package org.folg.gedcom.visitors;

import org.folg.gedcom.model.*;
import org.folg.gedcom.parser.ModelParser;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.*;


public class GedcomWriterTest {
  @Test
  public void testContactStringsRoundTrip() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case001-AddressStructure.ged");
    Gedcom gedcom = new ModelParser().parseGedcom(new File(gedcomUrl.toURI()));
    String output = write(gedcom);

    // contact strings follow ADDR (and its children) at the same level
    assertTrue(output.contains("4 CTRY United States\n3 PHON 866-000-0000\n3 EMAIL info@mycorporation.com\n3 FAX 866-111-1111\n3 WWW http://www.mycorporation.org/\n"), output);
    assertTrue(output.contains("3 CTRY United States\n2 PHON 877-907-8585\n2 EMAIL info@arlingtoncemetery.mil\n2 FAX 877-111-1111\n2 WWW http://www.arlingtoncemetery.mil/\n"), output);

    Gedcom reparsed = new ModelParser().parseGedcom(new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8)));
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

  @Test
  public void testFoneAndRomnRoundTrip() throws Exception {
    String input = "0 HEAD\n1 CHAR UTF-8\n0 @I1@ INDI\n1 NAME 山田 /太郎/\n2 FONE Yamada /Taro/\n2 ROMN Yamada /Taro/\n0 TRLR\n";
    Gedcom gedcom = new ModelParser().parseGedcom(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    String output = write(gedcom);
    assertTrue(output.contains("1 NAME 山田 /太郎/\n2 FONE Yamada /Taro/\n2 ROMN Yamada /Taro/\n"), output);

    Gedcom reparsed = new ModelParser().parseGedcom(new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8)));
    Name name = reparsed.getPeople().get(0).getNames().get(0);
    assertEquals(name.getFone(), "Yamada /Taro/");
    assertEquals(name.getRomn(), "Yamada /Taro/");
  }

  @Test
  public void testFoneAndRomnWithoutTags() throws Exception {
    Name name = new Name();
    name.setValue("John /Doe/");
    name.setFone("Jon /Doh/");
    name.setRomn("John /Doe/");
    Person person = new Person();
    person.setId("I1");
    person.addName(name);
    Gedcom gedcom = new Gedcom();
    gedcom.addPerson(person);

    String output = write(gedcom);
    assertTrue(output.contains("1 NAME John /Doe/\n2 FONE Jon /Doh/\n2 ROMN John /Doe/\n"), output);
  }

  @Test
  public void testRepositoryRefsRoundTrip() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case003-RepositoryRefs.ged");
    assertNotNull(gedcomUrl);
    Gedcom gedcom = new ModelParser().parseGedcom(new File(gedcomUrl.toURI()));

    String output = write(gedcom);
    assertTrue(output.contains("1 REPO @R1@\n2 CALN Call-number 123\n3 MEDI Media type\n2 NOTE @T1@\n2 NOTE A repo citation.\n3 CONT Second line.\n"), output);
    assertTrue(output.contains("1 REPO @R2@\n2 NOTE Another repo citation.\n"), output);
    assertTrue(output.contains("1 REPO @R3@\n2 CALN CN-456\n3 MEDI Article\n2 NOTE @T1@\n2 NOTE Third repo citation.\n"), output);

    Gedcom reparsed = new ModelParser().parseGedcom(new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8)));
    assertEquals(reparsed.getSources().size(), 1);
    Source source = reparsed.getSources().get(0);
    assertNotNull(source);
    assertEquals(source.getTitle(), "Source title");
    assertEquals(source.getRepositoryRefs().size(), 3);

    RepositoryRef repositoryRef1 = source.getRepositoryRefs().get(0);
    assertNotNull(repositoryRef1);
    assertEquals(repositoryRef1.getRef(), "R1");
    assertEquals(repositoryRef1.getCallNumber(), "Call-number 123");
    assertEquals(repositoryRef1.getMediaType(), "Media type");
    assertEquals(repositoryRef1.getNoteRefs().size(), 1);
    NoteRef noteRef = repositoryRef1.getNoteRefs().get(0);
    assertNotNull(noteRef);
    assertEquals(noteRef.getRef(), "T1");
    assertEquals(repositoryRef1.getNotes().size(), 1);
    Note note1 = repositoryRef1.getNotes().get(0);
    assertNotNull(note1);
    assertEquals(note1.getValue(), "A repo citation.\nSecond line.");

    RepositoryRef repositoryRef2 = source.getRepositoryRefs().get(1);
    assertNotNull(repositoryRef2);
    assertEquals(repositoryRef2.getRef(), "R2");
    assertEquals(repositoryRef2.getNotes().size(), 1);
    Note note2 = repositoryRef2.getNotes().get(0);
    assertNotNull(note2);
    assertEquals(note2.getValue(), "Another repo citation.");

    RepositoryRef repositoryRef3 = source.getRepositoryRefs().get(2);
    assertNotNull(repositoryRef3);
    assertEquals(repositoryRef3.getRef(), "R3");
    assertEquals(repositoryRef3.getCallNumber(), "CN-456");
    assertEquals(repositoryRef3.getMediaType(), "Article");
    assertEquals(repositoryRef3.getNotes().size(), 1);
    Note note3 = repositoryRef3.getNotes().get(0);
    assertNotNull(note3);
    assertEquals(note3.getValue(), "Third repo citation.");
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

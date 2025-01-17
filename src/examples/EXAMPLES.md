## Examples
See all parsers in [main/java/org/folg/gedcom/parser](../main/java/org/folg/gedcom/parser) directory.

### 1. Parse gedcom and print all people names

    String filePath = "/path-to-family-tree-file.ged";
    File file = new File(filePath);

    ModelParser modelParser = new ModelParser();
    Gedcom gedcom = modelParser.parseGedcom(file);
    List<Person> people = gedcom.getPeople();
    System.out.println("People: " + people.size());

    int index = 1;
    for (Person person : people) {
        for (Name name : person.getNames()) {
            System.out.printf("\n%d: %s %s", index, name.getSurname(), name.getGiven());
        }
        index ++;
        }
    }

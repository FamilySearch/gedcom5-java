package org.folg.gedcom.tools;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.GedcomTag;
import org.folg.gedcom.parser.ModelParser;
import org.folg.gedcom.parser.TreeParser;
import org.folg.gedcom.visitors.GedcomWriter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * User: Ryan K.
 * Date: 1/7/12
 * <p/>
 * Convert a GEDCOM file or directory to the model and back again
 */
public class CompareGedcom2Gedcom {
    private static final Logger logger = Logger.getLogger("org.folg.gedcom.tools");

    @Option(name = "-i", required = true, usage = "file or directory containing gedcom files to convert")
    private File gedcomIn;

    @Option(name = "-o", required = false, usage = "temporary directory to store generated gedcom in")
    private File gedcomOut;


    private int totalGedcomTags = 0;
    private int equalsCount = 0;
    private int notEqualsCount = 0;

    public CompareGedcom2Gedcom() {

    }

    public void convertGedcom(File file) {

        ModelParser parser = new ModelParser();
        GedcomWriter writer = new GedcomWriter();

        try {
            Gedcom gedcom = parser.parseGedcom(file);

            File tempFile = (gedcomOut != null ? new File(gedcomOut, file.getName()) :
                    File.createTempFile(file.getName(), "tmp"));
            OutputStream out = new FileOutputStream(tempFile);

            writer.write(gedcom, out);
            if (out != null) {
                out.close();
            }

            compareGedcom(file, tempFile);

        } catch (SAXParseException e) {
            logger.severe("SaxParseException for file: " + file.getName() + " " + e.getMessage() + " @ " + e.getLineNumber());
        } catch (IOException e) {
            logger.severe("IOException for file: " + file.getName() + " " + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.severe("Exception for file: " + file.getName() + " " + e.getMessage());
        }
    }

    private void compareGedcom(File file, File tempFile) throws SAXParseException, IOException {
        TreeParser treeParser = new TreeParser();

        List<GedcomTag> gedcomTags = treeParser.parseGedcom(file);
        List<GedcomTag> compareGedcomTags = treeParser.parseGedcom(tempFile);

        Collections.sort(gedcomTags, compareGedcomTags());
        Collections.sort(compareGedcomTags, compareGedcomTags());

        for (int indx = 0; indx < gedcomTags.size(); indx++) {
            GedcomTag gedcomTagOne = gedcomTags.get(indx);
            GedcomTag gedcomTagTwo = gedcomTags.get(indx);

            totalGedcomTags++;

            if (gedcomTagOne.equals(gedcomTagTwo)) {
                equalsCount++;
            } else {
                notEqualsCount++;
            }

        }
    }


    private Comparator<GedcomTag> compareGedcomTags() {
        return new Comparator<GedcomTag>() {
            @Override
            public int compare(GedcomTag gedcomTagOne, GedcomTag gedcomTagTwo) {

                //the only case so far where the id is null is the head and trlr tag
                //need to verify if this is a correct assumption?
                String idOne = gedcomTagOne.getId();
                String idTwo = gedcomTagTwo.getId();
                if ((idOne == null) && (idTwo == null)) {
                    String tagOne = gedcomTagOne.getTag();
                    String tagTwo = gedcomTagTwo.getTag();
                    return tagOne.compareTo(tagTwo);
                }

                if (idOne == null)
                    return -1;
                else if (idTwo == null)
                    return 1;
                else
                    return idOne.compareTo(idTwo);
            }
        };
    }

    private void doMain() throws FileNotFoundException {
        if (gedcomIn.isDirectory()) {
            for (File file : gedcomIn.listFiles()) {
                convertGedcom(file);
            }
        } else if (gedcomIn.isFile()) {
            convertGedcom(gedcomIn);
        }

        System.out.println("Total Number of Gedcom Tags = " + totalGedcomTags);
        System.out.println("Number of Tags that are Equal = " + equalsCount);
        System.out.println("Number of Tags that are NOT Equal = " + notEqualsCount);
    }

    public static void main(String[] args) throws FileNotFoundException {
        CompareGedcom2Gedcom self = new CompareGedcom2Gedcom();
        CmdLineParser parser = new CmdLineParser(self);
        try {
            parser.parseArgument(args);
            self.doMain();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
    }
}

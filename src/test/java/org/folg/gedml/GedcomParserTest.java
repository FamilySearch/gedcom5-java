package org.folg.gedml;

import org.gedml.GedcomParser;
import org.testng.annotations.Test;
import org.xml.sax.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class GedcomParserTest {

    class MockContentHandler implements ContentHandler {

        final List<String> tags;

        MockContentHandler() {
            this.tags = new ArrayList<String>();
        }

        @Override
        public void setDocumentLocator(Locator locator) {

        }

        @Override
        public void startDocument() {

        }

        @Override
        public void endDocument() {

        }

        @Override
        public void startPrefixMapping(String prefix, String uri) {

        }

        @Override
        public void endPrefixMapping(String prefix) {

        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) {
            this.tags.add(localName);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {

        }

        @Override
        public void characters(char[] ch, int start, int length) {

        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) {

        }

        @Override
        public void processingInstruction(String target, String data) {

        }

        @Override
        public void skippedEntity(String name) {

        }

    }

    class MockErrorHandler implements ErrorHandler {

        @Override
        public void warning(SAXParseException exception) {

        }

        @Override
        public void error(SAXParseException exception) {

        }

        @Override
        public void fatalError(SAXParseException exception) {

        }
    }

    class MockReader extends Reader {

        private final StringReader internalReader;
        boolean readWasCalled = false;
        boolean closeWasCalled = false;

        MockReader(String chars) {
            this.internalReader = new StringReader(chars);
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            this.readWasCalled = true;
            return internalReader.read(cbuf, off, len);
        }

        @Override
        public void close() {
            this.closeWasCalled = true;
            internalReader.close();
        }
    }

    class MockInputStream extends InputStream {

        private final ByteArrayInputStream internalStream;
        boolean readWasCalled = false;

        MockInputStream(String chars) {
            this.internalStream = new ByteArrayInputStream(chars.getBytes(Charset.forName("UTF-8")));
        }

        @Override
        public int read() {
            this.readWasCalled = true;
            return internalStream.read();
        }
    }

    @Test
    public void testParse_withInputSourceAndCharacterStream() throws Exception {
        MockContentHandler contentHandler = new MockContentHandler();
        MockErrorHandler errorHandler = new MockErrorHandler();

        String mockSystemId = "Mock system Id";
        MockReader characterStream = new MockReader("0 HEAD\n1 CHAR UTF-8\n0 @I1@ INDI\n");
        MockInputStream byteStream = new MockInputStream("I should not get called!");
        InputSource input = new InputSource();
        input.setCharacterStream(characterStream);
        input.setByteStream(byteStream);
        input.setSystemId(mockSystemId);

        GedcomParser parser = new GedcomParser();
        parser.setContentHandler(contentHandler);
        parser.setErrorHandler(errorHandler);
        parser.parse(input);

        assertTrue(characterStream.readWasCalled, "Failed to read character stream");
        assertTrue(characterStream.closeWasCalled, "Failed to close character stream");
        assertFalse(byteStream.readWasCalled, "Should not have read byte stream");
        assertEquals(parser.getSystemId(), mockSystemId);

        assertEquals(contentHandler.tags, Arrays.asList("GED", "HEAD", "CHAR", "INDI"));
    }

    @Test
    public void testParse_withInputSourceAndByteStream() throws Exception {
        MockContentHandler contentHandler = new MockContentHandler();
        MockErrorHandler errorHandler = new MockErrorHandler();

        String mockSystemId = "Mock system Id";
        MockReader characterStream = new MockReader("I should not get called!");
        MockInputStream byteStream = new MockInputStream("0 HEAD\n1 CHAR UTF-8\n0 @I1@ INDI\n");
        InputSource input = new InputSource();
        input.setByteStream(byteStream);
        input.setSystemId(mockSystemId);

        GedcomParser parser = new GedcomParser();
        parser.setContentHandler(contentHandler);
        parser.setErrorHandler(errorHandler);
        parser.parse(input);

        assertFalse(characterStream.readWasCalled, "Should not have read character stream");
        assertFalse(characterStream.closeWasCalled, "Should not have closed character stream");
        assertTrue(byteStream.readWasCalled, "Failed to read byte stream");
        assertEquals(parser.getSystemId(), mockSystemId);

        assertEquals(contentHandler.tags, Arrays.asList("GED", "HEAD", "CHAR", "INDI"));
    }

    @Test
    public void testParse_withInputSourceAndSystemId() throws Exception {
        MockContentHandler contentHandler = new MockContentHandler();
        MockErrorHandler errorHandler = new MockErrorHandler();

        URL gedcomUrl = this.getClass().getClassLoader().getResource("Case001-AddressStructure.ged");
        String expectedSystemId = gedcomUrl.toString();
        MockReader characterStream = new MockReader("I should not get called!");
        MockInputStream byteStream = new MockInputStream("0 HEAD\n1 CHAR UTF-8\n0 @I1@ INDI\n");
        InputSource input = new InputSource();
        input.setSystemId(expectedSystemId);

        GedcomParser parser = new GedcomParser();
        parser.setContentHandler(contentHandler);
        parser.setErrorHandler(errorHandler);
        parser.parse(input);

        assertFalse(characterStream.readWasCalled, "Should not have read character stream");
        assertFalse(characterStream.closeWasCalled, "Should not have closed character stream");
        assertFalse(byteStream.readWasCalled, "Failed to read byte stream");
        assertEquals(parser.getSystemId(), expectedSystemId);

        assertEquals(contentHandler.tags.size(), 91);
        assertEquals(contentHandler.tags.get(0), "GED");
        assertEquals(contentHandler.tags.get(1), "HEAD");
    }

}

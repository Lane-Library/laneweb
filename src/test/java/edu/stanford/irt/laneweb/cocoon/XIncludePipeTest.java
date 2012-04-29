package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;

import javax.xml.transform.dom.DOMResult;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.source.impl.MultiSourceValidity;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.XMLBaseSupport;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.cocoon.xml.dom.DOMBuilder;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.xml.xpath.PrefixResolver;
import org.apache.excalibur.xml.xpath.XPathProcessor;
import org.apache.excalibur.xmlizer.XMLizer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class XIncludePipeTest {

    private static final class TestExceptionListener implements XIncludeExceptionListener {

        private boolean failFast = true;

        @Override
        public void exception(final Exception e) {
            if (this.failFast) {
                throw new LanewebException(e);
            }
        }

        private void setFailFast(final boolean failFast) {
            this.failFast = failFast;
        }
    }

    private Attributes attributes;

    private TestExceptionListener exceptionListener;

    private InputStream inputStream;

    private XIncludePipe pipe;

    private SAXParser saxParser;

    private ServiceManager serviceManager;

    private Source source;

    private SourceResolver sourceResolver;

    private MultiSourceValidity validity;

    private XMLBaseSupport xmlBaseSupport;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.sourceResolver = createMock(SourceResolver.class);
        this.validity = createMock(MultiSourceValidity.class);
        this.attributes = createMock(Attributes.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.source = createMock(Source.class);
        this.inputStream = createMock(InputStream.class);
        this.serviceManager = createMock(ServiceManager.class);
        this.saxParser = createMock(SAXParser.class);
        this.exceptionListener = new TestExceptionListener();
        this.xmlBaseSupport = createMock(XMLBaseSupport.class);
        this.pipe = new XIncludePipe(this.sourceResolver, this.validity, this.serviceManager, this.saxParser,
                this.exceptionListener, this.xmlBaseSupport);
        // TODO: have init make sense
        this.pipe.init(null, null);
        this.pipe.setConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        char[] characters = new char[0];
        this.xmlConsumer.characters(characters, 0, 0);
        replay(this.xmlConsumer);
        this.pipe.characters(characters, 0, 0);
        verify(this.xmlConsumer);
    }

    @Test
    public void testComment() throws SAXException {
        char[] comment = new char[0];
        this.xmlConsumer.comment(comment, 0, 0);
        replay(this.xmlConsumer);
        this.pipe.comment(comment, 0, 0);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndCDATA() throws SAXException {
        this.xmlConsumer.endCDATA();
        replay(this.xmlConsumer);
        this.pipe.endCDATA();
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndDocument() throws SAXException {
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer);
        this.pipe.endDocument();
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndElementStringStringString() throws SAXException {
        this.xmlBaseSupport.endElement("uri", "localname", "qname");
        this.xmlConsumer.endElement("uri", "localname", "qname");
        replay(this.xmlConsumer, this.xmlBaseSupport);
        this.pipe.endElement("uri", "localname", "qname");
        verify(this.xmlConsumer, this.xmlBaseSupport);
    }

    @Test
    public void testEndEntityString() throws SAXException {
        this.xmlConsumer.endEntity("name");
        replay(this.xmlConsumer);
        this.pipe.endEntity("name");
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndFallbackElement() throws SAXException {
        replay(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser);
        this.pipe.endElement("http://www.w3.org/2001/XInclude", "fallback", "fallback");
        verify(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser);
    }

    @Test
    public void testEndIncludeElement() throws SAXException {
        replay(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser);
        this.pipe.endElement("http://www.w3.org/2001/XInclude", "include", "include");
        verify(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser);
    }

    @Test
    public void testEndPrefixMappingString() throws SAXException {
        this.xmlConsumer.endPrefixMapping("prefix");
        replay(this.xmlConsumer);
        this.pipe.endPrefixMapping("prefix");
        verify(this.xmlConsumer);
    }

    @Test
    public void testFallbackElement() throws SAXException {
        this.xmlBaseSupport.startElement("http://www.w3.org/2001/XInclude", "fallback", "fallback", this.attributes);
        replay(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
        this.pipe.startElement("http://www.w3.org/2001/XInclude", "fallback", "fallback", this.attributes);
        verify(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
    }

    @Test
    public void testIgnorableWhitespace() throws SAXException {
        char[] characters = new char[0];
        this.xmlConsumer.ignorableWhitespace(characters, 0, 0);
        replay(this.xmlConsumer);
        this.pipe.ignorableWhitespace(characters, 0, 0);
        verify(this.xmlConsumer);
    }

    @Test
    public void testProcessingInstructionStringString() throws SAXException {
        this.xmlConsumer.processingInstruction("target", "data");
        replay(this.xmlConsumer);
        this.pipe.processingInstruction("target", "data");
        verify(this.xmlConsumer);
    }

    @Test
    public void testSetDocumentLocatorLocator() throws IOException, SAXException {
        Locator locator = createMock(Locator.class);
        expect(locator.getSystemId()).andReturn("systemid");
        expect(this.sourceResolver.resolveURI("systemid")).andReturn(this.source);
        expect(this.source.getURI()).andReturn("uri");
        this.xmlBaseSupport.setDocumentLocation("uri");
        this.xmlConsumer.setDocumentLocator(locator);
        replay(this.xmlConsumer, locator, this.sourceResolver, this.source, this.xmlBaseSupport);
        this.pipe.setDocumentLocator(locator);
        verify(this.xmlConsumer, locator, this.sourceResolver, this.source, this.xmlBaseSupport);
    }

    @Test
    public void testSetDocumentLocatorLocatorThrowsIOException() throws IOException {
        Locator locator = createMock(Locator.class);
        expect(locator.getSystemId()).andReturn("systemid");
        expect(this.sourceResolver.resolveURI("systemid")).andThrow(new IOException());
        replay(this.xmlConsumer, locator, this.sourceResolver, this.source, this.xmlBaseSupport);
        try {
            this.pipe.setDocumentLocator(locator);
            fail();
        } catch (LanewebException e) {
        }
        verify(this.xmlConsumer, locator, this.sourceResolver, this.source, this.xmlBaseSupport);
    }

    @Test
    public void testSetDocumentLocatorLocatorThrowsSAXException() throws IOException, SAXException {
        Locator locator = createMock(Locator.class);
        expect(locator.getSystemId()).andReturn("systemid");
        expect(this.sourceResolver.resolveURI("systemid")).andReturn(this.source);
        expect(this.source.getURI()).andReturn("uri");
        this.xmlBaseSupport.setDocumentLocation("uri");
        expectLastCall().andThrow(new SAXException());
        replay(this.xmlConsumer, locator, this.sourceResolver, this.source, this.xmlBaseSupport);
        try {
            this.pipe.setDocumentLocator(locator);
            fail();
        } catch (LanewebException e) {
        }
        verify(this.xmlConsumer, locator, this.sourceResolver, this.source, this.xmlBaseSupport);
    }

    @Test
    public void testSkippedEntityString() throws SAXException {
        this.xmlConsumer.skippedEntity("name");
        replay(this.xmlConsumer);
        this.pipe.skippedEntity("name");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartCDATA() throws SAXException {
        this.xmlConsumer.startCDATA();
        replay(this.xmlConsumer);
        this.pipe.startCDATA();
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElementStringStringStringAttributes() throws SAXException {
        this.xmlConsumer.startElement("uri", "localname", "qName", this.attributes);
        replay(this.xmlConsumer);
        this.pipe.startElement("uri", "localname", "qName", this.attributes);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartEntityString() throws SAXException {
        this.xmlConsumer.startEntity("name");
        replay(this.xmlConsumer);
        this.pipe.startEntity("name");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartPrefixMappingStringString() throws SAXException {
        this.xmlConsumer.startPrefixMapping("prefix", "uri");
        replay(this.xmlConsumer);
        this.pipe.startPrefixMapping("prefix", "uri");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartXIncludeElement() throws SAXException, IOException {
        this.xmlBaseSupport.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
        expect(this.attributes.getValue("", "href")).andReturn("foo");
        expect(this.attributes.getValue("", "parse")).andReturn(null);
        expect(this.attributes.getValue("", "xpointer")).andReturn(null);
        expect(this.xmlBaseSupport.makeAbsolute("foo")).andReturn(this.source);
        this.validity.addSource(this.source);
        expect(this.source.getURI()).andReturn("foo");
        expect(this.source.getInputStream()).andReturn(this.inputStream);
        this.saxParser.parse(isA(InputSource.class), isA(XMLConsumer.class));
        this.inputStream.close();
        replay(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
        this.pipe.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
        verify(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
    }

    @Test
    public void testStartXIncludeElementText() throws SAXException, IOException {
        this.xmlBaseSupport.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
        expect(this.attributes.getValue("", "href")).andReturn("foo");
        expect(this.attributes.getValue("", "parse")).andReturn("text");
        expect(this.attributes.getValue("", "xpointer")).andReturn(null);
        expect(this.xmlBaseSupport.makeAbsolute("foo")).andReturn(this.source);
        this.validity.addSource(this.source);
        expect(this.source.getInputStream()).andReturn(new ByteArrayInputStream("hi".getBytes()));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(2));
        replay(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
        this.pipe.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
        verify(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
    }

    // This test demonstrated a bug where an exception occurred during include
    // processing but
    // before the try/catch block incrementing the fallback state.
    @Test
    public void testStartXIncludeElementThrows() throws MalformedURLException, IOException, SAXException {
        this.exceptionListener.setFailFast(false);
        this.xmlBaseSupport.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
        expect(this.attributes.getValue("", "href")).andReturn("foo");
        expect(this.attributes.getValue("", "parse")).andReturn(null);
        expect(this.attributes.getValue("", "xpointer")).andReturn(null);
        expect(this.xmlBaseSupport.makeAbsolute("foo")).andThrow(new LanewebException("oopsie"));
        replay(this.sourceResolver, this.attributes, this.xmlBaseSupport);
        // must call init() to create xmlBaseSupport object
        try {
            this.pipe.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
        } catch (Exception e) {
            fail("should catch all exceptions");
        }
        verify(this.sourceResolver, this.attributes, this.xmlBaseSupport);
    }

    @Test
    // TODO: wow, is this freaking complicated. It is testing the
    // XPointerFramework more than XIncludePipe
    // refactor so that XIncludePipe does not do 'new' xpointer stuff.
    public void testStartXIncludeElementXPointer() throws SAXException, IOException, ServiceException {
        this.xmlBaseSupport.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
        expect(this.attributes.getValue("", "href")).andReturn("foo");
        expect(this.attributes.getValue("", "parse")).andReturn("xml");
        expect(this.attributes.getValue("", "xpointer")).andReturn("xpointer(/bar)");
        expect(this.xmlBaseSupport.makeAbsolute("foo")).andReturn(this.source);
        this.validity.addSource(this.source);
        expect(this.source.getURI()).andReturn("foo").times(2);
        expect(this.source.getMimeType()).andReturn(null);
        final DOMResult domResult = createMock(DOMResult.class);
        XMLizer xmlizer = new XMLizer() {

            @Override
            public void toSAX(final InputStream stream, final String mimeType, final String systemID, final ContentHandler handler)
                    throws SAXException, IOException {
                DOMBuilder builder = (DOMBuilder) handler;
                try {
                    Field result = DOMBuilder.class.getDeclaredField("result");
                    result.setAccessible(true);
                    result.set(builder, domResult);
                } catch (SecurityException e) {
                    throw new LanewebException(e);
                } catch (NoSuchFieldException e) {
                    throw new LanewebException(e);
                } catch (IllegalArgumentException e) {
                    throw new LanewebException(e);
                } catch (IllegalAccessException e) {
                    throw new LanewebException(e);
                }
            }
        };
        expect(this.serviceManager.lookup("org.apache.excalibur.xmlizer.XMLizer")).andReturn(xmlizer);
        this.serviceManager.release(xmlizer);
        expect(this.source.getInputStream()).andReturn(this.inputStream);
        Document doc = createMock(Document.class);
        expect(domResult.getNode()).andReturn(doc).times(3);
        expect(doc.getNodeType()).andReturn(Node.DOCUMENT_NODE);
        XPathProcessor xpathProcessor = createMock(XPathProcessor.class);
        expect(this.serviceManager.lookup("org.apache.excalibur.xml.xpath.XPathProcessor")).andReturn(xpathProcessor);
        NodeList nodeList = createMock(NodeList.class);
        expect(xpathProcessor.selectNodeList(eq(doc), eq("/bar"), isA(PrefixResolver.class))).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        this.serviceManager.release(xpathProcessor);
        replay(nodeList, xpathProcessor, doc, domResult, this.sourceResolver, this.attributes, this.source, this.inputStream,
                this.validity, this.xmlConsumer, this.serviceManager, this.saxParser, this.xmlBaseSupport);
        this.pipe.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
        verify(nodeList, xpathProcessor, doc, domResult, this.sourceResolver, this.attributes, this.source, this.inputStream,
                this.validity, this.xmlConsumer, this.serviceManager, this.saxParser, this.xmlBaseSupport);
    }

    @Test
    public void testUnknownElement() throws SAXException {
        this.xmlBaseSupport.startElement("http://www.w3.org/2001/XInclude", "fooey", "fooey", this.attributes);
        replay(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
        try {
            this.pipe.startElement("http://www.w3.org/2001/XInclude", "fooey", "fooey", this.attributes);
            fail();
        } catch (SAXException e) {
        }
        verify(this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
    }

    @Test
    public void testUnknownElementLocator() throws SAXException, MalformedURLException, IOException {
        Locator locator = createMock(Locator.class);
        expect(locator.getSystemId()).andReturn("systemid").times(2);
        expect(this.sourceResolver.resolveURI("systemid")).andReturn(this.source);
        expect(this.source.getURI()).andReturn("uri");
        this.xmlBaseSupport.setDocumentLocation("uri");
        this.xmlConsumer.setDocumentLocator(locator);
        this.xmlBaseSupport.startElement("http://www.w3.org/2001/XInclude", "fooey", "fooey", this.attributes);
        expect(locator.getColumnNumber()).andReturn(12);
        expect(locator.getLineNumber()).andReturn(44);
        replay(locator, this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
        try {
            this.pipe.setDocumentLocator(locator);
            this.pipe.startElement("http://www.w3.org/2001/XInclude", "fooey", "fooey", this.attributes);
            fail();
        } catch (SAXException e) {
        }
        verify(locator, this.sourceResolver, this.attributes, this.source, this.inputStream, this.validity, this.xmlConsumer,
                this.serviceManager, this.saxParser, this.xmlBaseSupport);
    }
}

package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class HtmlProxyLinkTransformerTest {

    private Attributes attributes;

    private ProxyHostManager proxyHostManager;

    private HtmlProxyLinkTransformer transformer;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.proxyHostManager = mock(ProxyHostManager.class);
        this.transformer = new HtmlProxyLinkTransformer(this.proxyHostManager);
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer.setXMLConsumer(this.xmlConsumer);
        this.attributes = mock(Attributes.class);
        this.transformer.setModel(Collections.singletonMap(Model.BASE_PROXY_URL, "baseProxyUrl"));
    }

    @Test
    public void testAClassNoProxy() throws SAXException {
        expect(this.attributes.getValue("href")).andReturn("http://url");
        expect(this.attributes.getValue("class")).andReturn("noproxy");
        this.xmlConsumer.startElement("", "a", "a", this.attributes);
        replay(this.xmlConsumer, this.attributes, this.proxyHostManager);
        this.transformer.startElement("", "a", "a", this.attributes);
        verify(this.xmlConsumer, this.attributes, this.proxyHostManager);
    }

    @Test
    public void testANotHttp() throws SAXException {
        expect(this.attributes.getValue("href")).andReturn("/url");
        this.xmlConsumer.startElement("", "a", "a", this.attributes);
        replay(this.xmlConsumer, this.attributes, this.proxyHostManager);
        this.transformer.startElement("", "a", "a", this.attributes);
        verify(this.xmlConsumer, this.attributes, this.proxyHostManager);
    }

    @Test
    public void testANotProxied() throws SAXException {
        expect(this.attributes.getValue("href")).andReturn("http://url");
        expect(this.attributes.getValue("class")).andReturn("");
        expect(this.proxyHostManager.isProxyableLink("http://url")).andReturn(false);
        this.xmlConsumer.startElement("", "a", "a", this.attributes);
        replay(this.xmlConsumer, this.attributes, this.proxyHostManager);
        this.transformer.startElement("", "a", "a", this.attributes);
        verify(this.xmlConsumer, this.attributes, this.proxyHostManager);
    }

    @Test
    public void testANullClass() throws SAXException {
        expect(this.attributes.getValue("href")).andReturn("http://url");
        expect(this.attributes.getValue("class")).andReturn(null);
        expect(this.proxyHostManager.isProxyableLink("http://url")).andReturn(false);
        this.xmlConsumer.startElement("", "a", "a", this.attributes);
        replay(this.xmlConsumer, this.attributes, this.proxyHostManager);
        this.transformer.startElement("", "a", "a", this.attributes);
        verify(this.xmlConsumer, this.attributes, this.proxyHostManager);
    }

    @Test
    public void testANullHref() throws SAXException {
        expect(this.attributes.getValue("href")).andReturn(null);
        this.xmlConsumer.startElement("", "a", "a", this.attributes);
        replay(this.xmlConsumer, this.attributes, this.proxyHostManager);
        this.transformer.startElement("", "a", "a", this.attributes);
        verify(this.xmlConsumer, this.attributes, this.proxyHostManager);
    }

    @Test
    public void testAProxied() throws SAXException {
        expect(this.attributes.getValue("href")).andReturn("http://url");
        expect(this.attributes.getValue("class")).andReturn("");
        expect(this.proxyHostManager.isProxyableLink("http://url")).andReturn(true);
        expect(this.attributes.getLength()).andReturn(1);
        expect(this.attributes.getURI(0)).andReturn("");
        expect(this.attributes.getLocalName(0)).andReturn("href");
        expect(this.attributes.getQName(0)).andReturn("href");
        expect(this.attributes.getType(0)).andReturn("CDATA");
        expect(this.attributes.getValue(0)).andReturn("http://url");
        // TODO: use a capture here and check the href
        this.xmlConsumer.startElement(eq(""), eq("a"), eq("a"), isA(Attributes.class));
        replay(this.xmlConsumer, this.attributes, this.proxyHostManager);
        this.transformer.startElement("", "a", "a", this.attributes);
        verify(this.xmlConsumer, this.attributes, this.proxyHostManager);
    }

    @Test
    public void testNotA() throws SAXException {
        this.xmlConsumer.startElement("", "b", "b", this.attributes);
        replay(this.xmlConsumer, this.attributes, this.proxyHostManager);
        this.transformer.startElement("", "b", "b", this.attributes);
        verify(this.xmlConsumer, this.attributes, this.proxyHostManager);
    }
}

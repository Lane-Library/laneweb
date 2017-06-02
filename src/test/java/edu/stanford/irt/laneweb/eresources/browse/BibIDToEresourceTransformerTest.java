package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;

public class BibIDToEresourceTransformerTest {

    private Attributes attributes;

    private SAXStrategy<Eresource> saxStrategy;

    private SolrService solrService;

    private BibIDToEresourceTransformer transformer;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.solrService = createMock(SolrService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.transformer = new BibIDToEresourceTransformer(this.solrService, this.saxStrategy, "type", AlwaysValid.SHARED_INSTANCE);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.transformer.setXMLConsumer(this.xmlConsumer);
        this.attributes = createMock(Attributes.class);
    }

    @Test
    public void testStartElement() throws SAXException {
        this.xmlConsumer.startElement(null, null, null, this.attributes);
        expect(this.attributes.getValue("data-bibid")).andReturn("12");
        expect(this.solrService.getByBibID("12")).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.solrService, this.saxStrategy, this.xmlConsumer, this.attributes);
        this.transformer.startElement(null, null, null, this.attributes);
        verify(this.solrService, this.saxStrategy, this.xmlConsumer, this.attributes);
    }

    @Test
    public void testStartElementNoDataBibId() throws SAXException {
        this.xmlConsumer.startElement(null, null, null, this.attributes);
        expect(this.attributes.getValue("data-bibid")).andReturn(null);
        replay(this.solrService, this.saxStrategy, this.xmlConsumer, this.attributes);
        this.transformer.startElement(null, null, null, this.attributes);
        verify(this.solrService, this.saxStrategy, this.xmlConsumer, this.attributes);
    }
}

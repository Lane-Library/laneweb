package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.laneweb.eresources.model.Eresource;

public class BibIDToEresourceTransformerTest {

    private Attributes attributes;

    private Eresource eresource;

    private SAXStrategy<Eresource> saxStrategy;

    private EresourceSearchService solrService;

    private BibIDToEresourceTransformer transformer;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.solrService = mock(EresourceSearchService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.transformer = new BibIDToEresourceTransformer(this.solrService, this.saxStrategy, "type",
                AlwaysValid.SHARED_INSTANCE);
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer.setXMLConsumer(this.xmlConsumer);
        this.attributes = mock(Attributes.class);
        this.eresource = mock(Eresource.class);
    }

    @Test
    public void testStartElement() throws SAXException {
        this.xmlConsumer.startElement(null, null, null, this.attributes);
        expect(this.attributes.getValue("data-bibid")).andReturn("12");
        expect(this.solrService.getByBibID("12")).andReturn(this.eresource);
        this.saxStrategy.toSAX(this.eresource, this.xmlConsumer);
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

    @Test
    public void testStartElementNullEresource() throws SAXException {
        this.xmlConsumer.startElement(null, null, null, this.attributes);
        expect(this.attributes.getValue("data-bibid")).andReturn("12");
        expect(this.solrService.getByBibID("12")).andReturn(null);
        replay(this.solrService, this.saxStrategy, this.xmlConsumer, this.attributes);
        this.transformer.startElement(null, null, null, this.attributes);
        verify(this.solrService, this.saxStrategy, this.xmlConsumer, this.attributes);
    }
}

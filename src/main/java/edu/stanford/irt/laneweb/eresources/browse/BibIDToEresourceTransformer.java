package edu.stanford.irt.laneweb.eresources.browse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;

public class BibIDToEresourceTransformer extends AbstractXMLPipe implements Transformer {

    private SAXStrategy<Eresource> saxStrategy;

    private SolrService solrService;

    private XMLConsumer xmlConsumer;

    public BibIDToEresourceTransformer(final SolrService solrService, final SAXStrategy<Eresource> saxStrategy) {
        this.solrService = solrService;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setXMLConsumer(xmlConsumer);
    }

    @Override
    public void startElement(final String uri, final String loc, final String raw, final Attributes atts)
            throws SAXException {
        super.startElement(uri, loc, raw, atts);
        String bibID = atts.getValue("data-bibid");
        if (bibID != null) {
            this.saxStrategy.toSAX(this.solrService.getByBibID(bibID), this.xmlConsumer);
        }
    }
}

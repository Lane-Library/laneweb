package edu.stanford.irt.laneweb.eresources.browse;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.solr.UncategorizedSolrException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BibIDToEresourceTransformer extends AbstractXMLPipe implements Transformer {

    private static final Logger log = LoggerFactory.getLogger(BibIDToEresourceTransformer.class);

    private boolean connectionFailed;

    private String key;

    private SAXStrategy<Eresource> saxStrategy;

    private SolrService solrService;

    private String type;

    private Validity validity;

    private XMLConsumer xmlConsumer;

    public BibIDToEresourceTransformer(final SolrService solrService, final SAXStrategy<Eresource> saxStrategy,
            final String type, final Validity validity) {
        this.solrService = solrService;
        this.saxStrategy = saxStrategy;
        this.type = type;
        this.validity = validity;
    }

    @Override
    public Serializable getKey() {
        return this.key;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Validity getValidity() {
        return this.validity;
    }

    @Override
    @Deprecated
    public void setModel(final Map<String, Object> model) {
        this.key = ModelUtil.getString(model, Model.SITEMAP_URI);
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
        if (bibID != null && !this.connectionFailed) {
            try {
                Eresource eresource = this.solrService.getByBibID(bibID);
                if (eresource != null) {
                    this.saxStrategy.toSAX(eresource, this.xmlConsumer);
                }
            } catch (UncategorizedSolrException e) {
                log.error("failed to retrieve eresource for {}, ignoring additional attempts: {}", bibID,
                        e.getMessage());
                this.connectionFailed = true;
            }
        }
    }
}

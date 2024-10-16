package edu.stanford.irt.laneweb.eresources.browse;

import java.io.Serializable;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BibIDToEresourceTransformer extends AbstractXMLPipe implements Transformer {

    private String key;

    private SAXStrategy<Eresource> saxStrategy;

    private EresourceSearchService searchService;

    private String type;

    private Validity validity;

    private XMLConsumer xmlConsumer;

    public BibIDToEresourceTransformer(final EresourceSearchService restSearchService,
            final SAXStrategy<Eresource> saxStrategy, final String type, final Validity validity) {
        this.searchService = restSearchService;
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
        // TODO: phase out use of data-bibid in favor of data-lsid
        // and consider renaming this class to LaneSearchIdToEresourceTransformer
        String bibID = atts.getValue("data-bibid");
        if (bibID != null) {
            Eresource eresource = this.searchService.getByBibID(bibID);
            if (eresource != null) {
                this.saxStrategy.toSAX(eresource, this.xmlConsumer);
            }
        }
        String laneSearchId = atts.getValue("data-lsid");
        if (laneSearchId != null) {
            Eresource eresource = this.searchService.getByLaneSearchId(laneSearchId);
            if (eresource != null) {
                this.saxStrategy.toSAX(eresource, this.xmlConsumer);
            }
        }
    }
}

package edu.stanford.irt.laneweb.images;

import java.util.Map;

import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.solr.BassettImage;
import edu.stanford.irt.solr.service.SolrImageService;

/**
 * @author alainb
 */
public class BassettAccordionGenerator extends AbstractGenerator {

    protected SolrImageService service;

    private String query;

    private SAXStrategy<FacetPage<BassettImage>> saxStrategy;

    public BassettAccordionGenerator(final SolrImageService service,
            final SAXStrategy<FacetPage<BassettImage>> saxStrategy) {
        this.service = service;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        if (null == this.query || "".equals(this.query)) {
            this.query = "*";
        }
        FacetPage<BassettImage> facet = this.service.facetBassettOnRegionAndSubRegion(this.query);
        this.saxStrategy.toSAX(facet, xmlConsumer);
    }
}

package edu.stanford.irt.laneweb.images;

import java.util.Map;

import edu.stanford.irt.bassett.service.BassettImageService;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;


/**
 * @author alainb
 */
public class BassettAccordionGenerator extends AbstractGenerator {

    protected BassettImageService service;

    private String query;

    private SAXStrategy<Map<String, Map<String, Integer>>> saxStrategy;

    public BassettAccordionGenerator(final BassettImageService service,
            final SAXStrategy<Map<String, Map<String, Integer>>> saxStrategy) {
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
        Map<String, Map<String, Integer>> facet = this.service.facetBassettOnRegionAndSubRegion();
        this.saxStrategy.toSAX(facet, xmlConsumer);
    }
}

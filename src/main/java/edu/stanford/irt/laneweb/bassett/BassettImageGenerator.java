package edu.stanford.irt.laneweb.bassett;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
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
public class BassettImageGenerator extends AbstractGenerator implements ModelAware {

    private static final int IMAGES_BY_PAGE = 30;

    private String bassettNumber;

    private int currentPage;

    private String query;

    private String region;

    private SAXStrategy<Page<BassettImage>> saxStrategy;

    private SolrImageService service;

    public BassettImageGenerator(final SolrImageService service, final SAXStrategy<Page<BassettImage>> saxStrategy) {
        this.service = service;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY, "*");
        if ("".equals(this.query)) {
            this.query = "*";
        }
        this.region = ModelUtil.getString(model, Model.REGION);
        if (null != this.region) {
            this.region = this.region.replace(' ', '_');
        }
        this.bassettNumber = ModelUtil.getString(model, Model.BASSETT_NUMBER);
        this.currentPage = Integer.parseInt(ModelUtil.getString(model, Model.PAGE, "1"));
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Pageable page = new PageRequest(this.currentPage - 1, IMAGES_BY_PAGE);
        Page<BassettImage> eresources = null;
        if (this.bassettNumber != null) {
            eresources = this.service.findBassettByNumber(this.bassettNumber);
        } else if (this.region != null) {
            if (this.region.contains("--")) {
                this.region = this.region.replace("--", "_sub_region_");
                eresources = this.service.findBassettByQueryFilterByRegionAndSubRegion(this.query, this.region, page);
            } else {
                eresources = this.service.findBassettByQueryFilterByRegion(this.query, this.region, page);
            }
        } else if (this.query != null) {
            eresources = this.service.findBassettByQuery(this.query, page);
        }
        this.saxStrategy.toSAX(eresources, xmlConsumer);
    }
}

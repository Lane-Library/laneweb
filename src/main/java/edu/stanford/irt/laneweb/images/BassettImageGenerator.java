package edu.stanford.irt.laneweb.images;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import edu.stanford.irt.bassett.model.BassettImage;
import edu.stanford.irt.bassett.service.BassettImageService;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;


/**
 * @author alainb
 */
public class BassettImageGenerator extends AbstractGenerator {

    private static final int IMAGES_BY_PAGE = 30;

    private String bassettNumber;

    private int currentPage;

    private String region;

    private SAXStrategy<Page<BassettImage>> saxStrategy;

    private BassettImageService service;

    public BassettImageGenerator(final BassettImageService service, final SAXStrategy<Page<BassettImage>> saxStrategy) {
        this.service = service;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.region = ModelUtil.getString(model, Model.REGION);
        this.bassettNumber = ModelUtil.getString(model, Model.BASSETT_NUMBER);
        this.currentPage = Integer.parseInt(ModelUtil.getString(model, Model.PAGE, "1"));
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Pageable page = PageRequest.of(this.currentPage - 1, IMAGES_BY_PAGE);
        Page<BassettImage> eresources = null;
        if (this.bassettNumber != null) {
            eresources = this.service.findBassettByNumber(this.bassettNumber);
        } else if (this.region != null) {
            if (this.region.contains("--")) {
                eresources = this.service.findBassettByRegionAndSubRegion(this.region, page);
            } else {
                eresources = this.service.findBassettByRegion(this.region, page);
            }            
        }
        this.saxStrategy.toSAX(eresources, xmlConsumer);
    }
}

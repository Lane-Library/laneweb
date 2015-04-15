package edu.stanford.irt.laneweb.bassett;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * @author alainb
 */
public class BassettImageGenerator extends AbstractGenerator implements ModelAware {

    private String bassettNumber;

    private BassettCollectionManager collectionManager;

    private String query;

    private String region;

    private SAXStrategy<List<BassettImage>> saxStrategy;

    public BassettImageGenerator(final BassettCollectionManager collectionManager,
            final SAXStrategy<List<BassettImage>> saxStrategy) {
        this.collectionManager = collectionManager;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
        this.region = ModelUtil.getString(model, Model.REGION);
        this.bassettNumber = ModelUtil.getString(model, Model.BASSETT_NUMBER);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        List<BassettImage> eresources = null;
        if (this.bassettNumber != null) {
            eresources = this.collectionManager.getById(this.bassettNumber);
        } else if (this.region != null) {
            if (this.query != null) {
                eresources = this.collectionManager.searchRegion(this.region, this.query);
            } else {
                eresources = this.collectionManager.getRegion(this.region);
            }
        } else if (this.query != null) {
            eresources = this.collectionManager.search(this.query);
        } else {
            eresources = Collections.emptyList();
        }
        this.saxStrategy.toSAX(eresources, xmlConsumer);
    }
}

package edu.stanford.irt.laneweb.bassett;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLizingStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * @author alainb
 */
public class BassettEresourcesGenerator extends AbstractGenerator implements ModelAware {

    private String bassettNumber;

    private BassettCollectionManager collectionManager;

    private String query;

    private String region;

    private XMLizingStrategy<Collection<BassettEresource>> xmlizingStrategy;

    public BassettEresourcesGenerator(final BassettCollectionManager collectionManager,
            final XMLizingStrategy<Collection<BassettEresource>> xmlizingStrategy) {
        this.collectionManager = collectionManager;
        this.xmlizingStrategy = xmlizingStrategy;
    }

    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
        this.region = ModelUtil.getString(model, Model.REGION);
        this.bassettNumber = ModelUtil.getString(model, Model.BASSETT_NUMBER);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Collection<BassettEresource> eresources = null;
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
            eresources = Collections.emptySet();
        }
        this.xmlizingStrategy.toSAX(eresources, xmlConsumer);
    }
}

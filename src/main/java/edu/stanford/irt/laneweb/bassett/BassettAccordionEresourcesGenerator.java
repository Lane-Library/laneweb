package edu.stanford.irt.laneweb.bassett;

import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * @author alainb
 */
public class BassettAccordionEresourcesGenerator extends AbstractGenerator implements ModelAware {

    private BassettCollectionManager collectionManager;

    private String query;

    private SAXStrategy<Map<String, Integer>> saxStrategy;

    public BassettAccordionEresourcesGenerator(final BassettCollectionManager collectionManager,
            final SAXStrategy<Map<String, Integer>> saxStrategy) {
        this.collectionManager = collectionManager;
        this.saxStrategy = saxStrategy;
    }

    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        String q = this.query == null || this.query.isEmpty() ? "bassett" : this.query;
        this.saxStrategy.toSAX(this.collectionManager.searchCount(q), xmlConsumer);
    }
}

package edu.stanford.irt.laneweb.bassett;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

// import edu.stanford.irt.laneweb.model.Model;
// import edu.stanford.irt.laneweb.model.ModelUtil;
public class BassettAccordionGenerator extends AbstractGenerator {

    // private String query;
    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        // marshall(getCollectionManager().searchCount(this.query));
    }

    // @Override
    protected void initialize() {
        // this.query = ModelUtil.getString(getModel(), Model.QUERY, "bassett");
    }
}

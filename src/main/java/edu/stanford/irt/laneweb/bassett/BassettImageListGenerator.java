package edu.stanford.irt.laneweb.bassett;

import org.apache.cocoon.xml.XMLConsumer;

// import java.util.Collection;
//
// import edu.stanford.irt.laneweb.model.Model;
// import edu.stanford.irt.laneweb.model.ModelUtil;
public class BassettImageListGenerator extends AbstractBassettGenerator {

    // private String bassettNumber;
    //
    // private String query;
    //
    // private String region;
    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        // Collection<BassettImage> images = null;
        // if (this.bassettNumber != null) {
        // images = getCollectionManager().getById(this.bassettNumber);
        // } else if (this.region != null) {
        // if (this.query != null) {
        // images = getCollectionManager().searchRegion(this.region,
        // this.query);
        // } else {
        // images = getCollectionManager().getRegion(this.region);
        // }
        // } else if (this.query != null) {
        // images = getCollectionManager().search(this.query);
        // }
        // marshall(images);
    }

    // @Override
    protected void initialize() {
        // this.query = ModelUtil.getString(getModel(), Model.QUERY);
        // this.region = ModelUtil.getString(getModel(), Model.REGION);
        // this.bassettNumber = ModelUtil.getString(getModel(),
        // Model.BASSETT_NUMBER);
    }
}

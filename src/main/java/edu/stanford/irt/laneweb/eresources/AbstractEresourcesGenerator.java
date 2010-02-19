package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public abstract class AbstractEresourcesGenerator extends AbstractGenerator {

    protected String alpha;

    protected CollectionManager collectionManager;

    protected String mesh;

    protected String subset;

    protected String type;
    
    public void generate() throws SAXException {
        XMLizableEresourceList eresources = new XMLizableEresourceList(getEresourceList());
        eresources.toSAX(this.xmlConsumer);
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }
    
    protected void initialize() {
        this.type = this.parameterMap.containsKey(LanewebObjectModel.TYPE) ?
                this.parameterMap.get(LanewebObjectModel.TYPE) :
                    this.model.getString(LanewebObjectModel.TYPE);
        this.subset = this.parameterMap.containsKey(LanewebObjectModel.SUBSET) ?
                this.parameterMap.get(LanewebObjectModel.SUBSET) :
                    this.model.getString(LanewebObjectModel.SUBSET);
        this.alpha = this.model.getString(LanewebObjectModel.ALPHA);
        this.mesh = this.model.getString(LanewebObjectModel.MESH);
        if (this.mesh != null) {
            this.mesh = this.mesh.toLowerCase();
        }
    }

    protected abstract Collection<Eresource> getEresourceList();
}

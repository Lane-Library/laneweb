package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;

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
        this.type = this.parameterMap.containsKey(Model.TYPE) ?
                this.parameterMap.get(Model.TYPE) :
                    this.model.getString(Model.TYPE);
        this.subset = this.parameterMap.containsKey(Model.SUBSET) ?
                this.parameterMap.get(Model.SUBSET) :
                    this.model.getString(Model.SUBSET);
        this.alpha = this.model.getString(Model.ALPHA);
        if (this.alpha != null && this.alpha.length() > 1) {
            if ("all".equals(this.alpha)) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.substring(0, 1);
            }
        }
        this.mesh = this.model.getString(Model.MESH);
        if (this.mesh != null) {
            this.mesh = this.mesh.toLowerCase();
        }
    }

    protected abstract Collection<Eresource> getEresourceList();
}

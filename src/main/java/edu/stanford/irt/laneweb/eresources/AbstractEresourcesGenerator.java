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
    
    protected int show;

    public void generate() throws SAXException {
//        new XMLizableEresourceList(getEresourceList()).toSAX(this.xmlConsumer);
        new PagingXMLizableEresourceList(getEresourceList(), this.show).toSAX(this.xmlConsumer);
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    protected abstract Collection<Eresource> getEresourceList();

    @Override
    protected void initialize() {
        this.type = this.parameterMap.containsKey(Model.TYPE) ? this.parameterMap.get(Model.TYPE) : getString(this.model, Model.TYPE);
        this.subset = this.parameterMap.containsKey(Model.SUBSET) ? this.parameterMap.get(Model.SUBSET) : getString(this.model, Model.SUBSET);
        this.alpha = getString(this.model, Model.ALPHA);
        if (this.alpha != null && this.alpha.length() > 1) {
            if ("all".equals(this.alpha)) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.substring(0, 1);
            }
        }
        this.mesh = getString(this.model, Model.MESH);
        if (this.mesh != null) {
            this.mesh = this.mesh.toLowerCase();
        }
        String show = getString(this.model, Model.SHOW, "0");
        this.show = "all".equals(show) ? -1 : Integer.parseInt(show);
    }
}

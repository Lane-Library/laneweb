package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.util.ModelUtil;

public abstract class AbstractEresourcesGenerator extends AbstractGenerator {

    protected String alpha;

    protected CollectionManager collectionManager;

    protected String mesh;

    protected String subset;

    protected String type;
    
    protected int page;

    public void generate() throws SAXException {
        new PagingXMLizableEresourceList(getEresourceList(), this.page).toSAX(this.xmlConsumer);
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
        this.type = this.parameterMap.containsKey(Model.TYPE) ? this.parameterMap.get(Model.TYPE) : ModelUtil.getString(this.model, Model.TYPE);
        this.subset = this.parameterMap.containsKey(Model.SUBSET) ? this.parameterMap.get(Model.SUBSET) : ModelUtil.getString(this.model, Model.SUBSET);
        this.alpha = ModelUtil.getString(this.model, Model.ALPHA);
        if (this.alpha != null && this.alpha.length() > 1) {
            if ("all".equals(this.alpha)) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.substring(0, 1);
            }
        }
        this.mesh = ModelUtil.getString(this.model, Model.MESH);
        if (this.mesh != null) {
            this.mesh = this.mesh.toLowerCase();
        }
        String page = ModelUtil.getString(this.model, Model.PAGE, "1");
        this.page = "all".equals(page) ? -1 : Integer.parseInt(page) - 1;
    }
}

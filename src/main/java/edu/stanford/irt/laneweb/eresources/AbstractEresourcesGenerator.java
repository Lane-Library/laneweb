package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractEresourcesGenerator extends AbstractGenerator {

    protected String alpha;

    protected CollectionManager collectionManager;

    protected String mesh;

    protected int page;

    protected String subset;

    protected String type;

    public void generate() {
        try {
            new PagingXMLizableEresourceList(getEresourceList(), this.page).toSAX(getXMLConsumer());
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    protected abstract Collection<edu.stanford.irt.eresources.Eresource> getEresourceList();

    @Override
    protected void initialize() {
        this.type = getParameterMap().containsKey(Model.TYPE) ? getParameterMap().get(Model.TYPE) : ModelUtil.getString(getModel(),
                Model.TYPE);
        this.subset = getParameterMap().containsKey(Model.SUBSET) ? getParameterMap().get(Model.SUBSET) : ModelUtil.getString(
                getModel(), Model.SUBSET);
        this.alpha = ModelUtil.getString(getModel(), Model.ALPHA);
        if (this.alpha != null && this.alpha.length() > 1) {
            if ("all".equals(this.alpha)) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.substring(0, 1);
            }
        }
        this.mesh = ModelUtil.getString(getModel(), Model.MESH);
        if (this.mesh != null) {
            this.mesh = this.mesh.toLowerCase();
        }
        String page = ModelUtil.getString(getModel(), Model.PAGE, "1");
        this.page = "all".equals(page) ? -1 : Integer.parseInt(page) - 1;
    }
}

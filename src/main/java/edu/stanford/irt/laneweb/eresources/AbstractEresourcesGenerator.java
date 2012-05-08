package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Map;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.cocoon.Initializable;
import edu.stanford.irt.laneweb.cocoon.ModelAware;
import edu.stanford.irt.laneweb.cocoon.ParametersAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractEresourcesGenerator extends AbstractGenerator implements ParametersAware, ModelAware, Initializable {

    protected String alpha;

    protected CollectionManager collectionManager;

    protected String mesh;

    protected int page;

    protected String subset;

    protected String type;

    private Map<String, Object> model;

    private Map<String, String> parameters;

    public void generate() {
        try {
            new PagingXMLizableEresourceList(getEresourceList(), this.page).toSAX(getXMLConsumer());
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    public void initialize() {
        this.type = this.parameters.containsKey(Model.TYPE) ? this.parameters.get(Model.TYPE) : ModelUtil.getString(this.model,
                Model.TYPE);
        this.subset = this.parameters.containsKey(Model.SUBSET) ? this.parameters.get(Model.SUBSET) : ModelUtil.getString(
                this.model, Model.SUBSET);
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
        try {
            this.page = "all".equals(page) ? -1 : Integer.parseInt(page) - 1;
        } catch (NumberFormatException e) {
            //if not 'all' or a number just give the first page:
            this.page = 0;
        }
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    public void setModel(final Map<String, Object> model) {
        this.model = model;
    }

    public void setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
    }

    protected abstract Collection<edu.stanford.irt.eresources.Eresource> getEresourceList();
}

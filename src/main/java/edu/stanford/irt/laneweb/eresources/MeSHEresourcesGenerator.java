package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class MeSHEresourcesGenerator extends AbstractEresourcesGenerator {

    private String mesh;

    private String type;

    public MeSHEresourcesGenerator(final String componentType, final CollectionManager collectionManager,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        super(componentType, collectionManager, saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.type = ModelUtil.getString(model, Model.TYPE);
        this.mesh = ModelUtil.getString(model, Model.MESH);
        if (this.mesh != null) {
            this.mesh = this.mesh.toLowerCase();
        }
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (parameters.containsKey(Model.TYPE)) {
            this.type = parameters.get(Model.TYPE);
        }
    }

    @Override
    protected StringBuilder createKey() {
        return super.createKey().append(";t=").append(null == this.type ? "" : this.type).append(";m=")
                .append(null == this.mesh ? "" : this.mesh);
    }

    @Override
    protected Collection<Eresource> getEresourceList(final CollectionManager collectionManager) {
        if (this.mesh == null || this.type == null) {
            return Collections.emptySet();
        }
        return collectionManager.getMesh(this.type, this.mesh);
    }
}

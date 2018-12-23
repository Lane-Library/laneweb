package edu.stanford.irt.laneweb.eresources.browse;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class CoreEresourcesGenerator extends AbstractEresourcesGenerator {

    private String type;

    public CoreEresourcesGenerator(final String componentType, final SolrService solrService,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        super(componentType, solrService, saxStrategy);
    }

    @Override
    @Deprecated
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.type = ModelUtil.getString(model, Model.TYPE);
    }

    @Override
    @Deprecated
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (parameters.containsKey(Model.TYPE)) {
            this.type = parameters.get(Model.TYPE);
        }
    }

    @Override
    protected StringBuilder createKey() {
        return super.createKey().append(";t=").append(null == this.type ? "" : this.type);
    }

    @Override
    protected List<Eresource> getEresourceList(final SolrService solrService) {
        if (this.type == null) {
            return Collections.emptyList();
        }
        return solrService.getCore(this.type);
    }

    @Override
    protected String getHeading() {
        return "\u00A0";
    }
}

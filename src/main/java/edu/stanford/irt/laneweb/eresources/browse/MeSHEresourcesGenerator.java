package edu.stanford.irt.laneweb.eresources.browse;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class MeSHEresourcesGenerator extends AbstractEresourcesGenerator {

    private String mesh;

    private String type;

    public MeSHEresourcesGenerator(final String componentType, final SolrService solrService,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        super(componentType, solrService, saxStrategy);
    }

    /**
     * @deprecated this will be replaced with constructor injection
     */
    @Override
    @Deprecated
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.type = ModelUtil.getString(model, Model.TYPE);
        this.mesh = ModelUtil.getString(model, Model.MESH);
    }

    /**
     * @deprecated this will be replaced with constructor injection
     */
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
        return super.createKey().append(";t=").append(null == this.type ? "" : this.type).append(";m=")
                .append(null == this.mesh ? "" : this.mesh);
    }

    @Override
    protected List<Eresource> getEresourceList(final SolrService solrService) {
        if (this.mesh == null || this.type == null) {
            return Collections.emptyList();
        }
        return solrService.getMesh(this.type, this.mesh);
    }

    @Override
    protected String getHeading() {
        String heading = null;
        if (this.mesh != null) {
            if ("aids/hiv".equals(this.mesh)) {
                heading = "AIDS/HIV";
            } else {
                heading = getCapitalizedMesh();
            }
        }
        return heading;
    }

    private String getCapitalizedMesh() {
        StringBuilder sb = new StringBuilder();
        for (StringTokenizer st = new StringTokenizer(this.mesh, " -", true); st.hasMoreTokens();) {
            String token = st.nextToken();
            if ("and".equals(token) || "of".equals(token)) {
                sb.append(token);
            } else {
                sb.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1));
            }
            if (st.hasMoreTokens()) {
                sb.append(st.nextToken());
            }
        }
        return sb.toString();
    }
}

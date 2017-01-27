package edu.stanford.irt.laneweb.eresources.browse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.PagingData;

public class BrowseEresourcesGenerator extends AbstractEresourcesGenerator {

    private static final String DEFAULT_ALPHA = "a";

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private String alpha;

    private String type;

    public BrowseEresourcesGenerator(final String type, final SolrService solrService,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        super(type, solrService, saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.type = ModelUtil.getString(model, Model.TYPE);
        this.alpha = ModelUtil.getString(model, Model.ALPHA, DEFAULT_ALPHA);
        if (this.alpha.length() == 0) {
            this.alpha = DEFAULT_ALPHA;
        }
        if (this.alpha.length() > 1) {
            this.alpha = this.alpha.substring(0, 1);
        }
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (parameters.containsKey(Model.TYPE)) {
            this.type = decode(parameters.get(Model.TYPE));
        }
    }

    @Override
    protected StringBuilder createKey() {
        return super.createKey().append(";a=").append(this.alpha).append(";t=")
                .append(null == this.type ? "" : this.type);
    }

    /**
     * A convenience method to URLDecode a string.
     *
     * @param string
     *            a string to decode
     * @return the decoded string
     */
    protected String decode(final String string) {
        try {
            return URLDecoder.decode(string, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException("won't happen", e);
        }
    }

    @Override
    protected List<Eresource> getEresourceList(final SolrService solrService) {
        List<Eresource> list;
        if (this.type == null) {
            list = Collections.emptyList();
        } else {
            list = solrService.getType(this.type, this.alpha.charAt(0));
        }
        return list;
    }

    @Override
    protected String getHeading() {
        String heading = null;
        if (this.type.indexOf("software, installed") == -1) {
            heading = this.alpha.toUpperCase();
        }
        return heading;
    }

    @Override
    protected PagingData getPagingData(final List<Eresource> eresources, final int page, final String baseQuery) {
        return new EresourceListPagingData(eresources, page, baseQuery, this.alpha);
    }
}
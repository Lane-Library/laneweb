package edu.stanford.irt.laneweb.eresources.browse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.EresourceBrowseService;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.PagingData;

public class BrowseEresourcesGenerator extends AbstractEresourcesGenerator {

    private static final String DEFAULT_ALPHA = "a";

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private String alpha;

    private String query;

    public BrowseEresourcesGenerator(final String type, final EresourceBrowseService restBrowseService,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        super(type, restBrowseService, saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.query = ModelUtil.getString(model, Model.QUERY);
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
        if (parameters.containsKey(Model.QUERY)) {
            this.query = decode(parameters.get(Model.QUERY));
        }
    }

    @Override
    protected StringBuilder createKey() {
        return super.createKey().append(";a=").append(this.alpha).append(";q=")
                .append(null == this.query ? "" : this.query);
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
    protected List<Eresource> getEresourceList(final EresourceBrowseService restBrowseService) {
        List<Eresource> list;
        if (this.query == null) {
            list = Collections.emptyList();
        } else {
            list = restBrowseService.browseByQuery(this.query, this.alpha.charAt(0));
        }
        return list;
    }

    @Override
    protected String getHeading() {
        return this.alpha.toUpperCase(Locale.US);
    }

    @Override
    protected PagingData getPagingData(final List<Eresource> eresources, final int page, final String baseQuery) {
        return new EresourceListPagingData(eresources, page, baseQuery, this.alpha);
    }
}

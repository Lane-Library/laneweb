package edu.stanford.irt.laneweb.eresources.browse;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;

public abstract class AbstractBrowseGenerator extends AbstractGenerator {

    protected static final String BASE_BROWSE_QUERY = "advanced:true recordType:bib AND isRecent:1";

    // the default cache expiration time, 20 minutes
    private static final long DEFAULT_EXPIRES = Duration.ofMinutes(20).toMillis();

    protected String basePath;

    protected String browseQuery;

    protected SolrService solrService;

    private String componentType;

    private long expires = DEFAULT_EXPIRES;

    private Validity validity;

    public AbstractBrowseGenerator(final String componentType, final SolrService solrService) {
        this.componentType = componentType;
        this.solrService = solrService;
    }

    @Override
    public Serializable getKey() {
        return (new StringBuilder("q=").append(this.browseQuery)).toString();
    }

    @Override
    public String getType() {
        return this.componentType;
    }

    @Override
    public Validity getValidity() {
        if (this.validity == null) {
            this.validity = new ExpiresValidity(this.expires);
        }
        return this.validity;
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        if (parameters.containsKey(Model.QUERY)) {
            try {
                this.browseQuery = URLDecoder.decode(parameters.get(Model.QUERY), StandardCharsets.UTF_8.displayName());
            } catch (UnsupportedEncodingException e) {
                throw new LanewebException("won't happen", e);
            }
        }
        if (parameters.containsKey(Model.BASE_PATH)) {
            this.basePath = parameters.get(Model.BASE_PATH);
        }
    }
}

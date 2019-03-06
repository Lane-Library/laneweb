package edu.stanford.irt.laneweb.eresources.browse;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;

/**
 * A generator that returns from Solr a faceted list of MeSH with hit counts, limited by resource type
 */
public class SubjectBrowseGenerator extends AbstractMarshallingGenerator {

    // the default cache expiration time
    private static final long DEFAULT_EXPIRES = Duration.ofMinutes(20).toMillis();

    // number of facet values to return from Solr: must be large enough to fetch all mesh (high cardinality)
    private static final int MAX_FACETS = 20_000;

    private String browseType;

    private String componentType;

    private long expires = DEFAULT_EXPIRES;

    private SolrService service;

    private Validity validity;

    public SubjectBrowseGenerator(final String componentType, final SolrService service, final Marshaller marshaller) {
        super(marshaller);
        this.componentType = componentType;
        this.service = service;
    }

    @Override
    public Serializable getKey() {
        return (new StringBuilder("t=").append(this.browseType)).toString();
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
        if (parameters.containsKey(Model.TYPE)) {
            try {
                this.browseType = URLDecoder.decode(parameters.get(Model.TYPE), StandardCharsets.UTF_8.displayName());
            } catch (UnsupportedEncodingException e) {
                throw new LanewebException("won't happen", e);
            }
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        if (null != this.browseType) {
            FacetPage<Eresource> fps = this.service.facetByField(
                    "advanced:true recordType:bib AND (isRecent:1 OR isLaneConnex:1)",
                    "type:\"" + this.browseType + '"', "mesh", 0, MAX_FACETS, 1, FacetSort.INDEX);
            Map<String, Long> map = new HashMap<>();
            for (Page<FacetFieldEntry> page : fps.getFacetResultPages()) {
                for (FacetFieldEntry entry : page) {
                    map.put(entry.getValue(), entry.getValueCount());
                }
            }
            marshal(map, xmlConsumer);
        }
    }
}

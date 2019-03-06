package edu.stanford.irt.laneweb.eresources.browse;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;

public class AtoZBrowseGenerator extends AbstractGenerator {

    private static final Pattern ACCEPTABLE_LETTERS = Pattern.compile("[1a-z]");

    // the default cache expiration time, 20 minutes
    private static final long DEFAULT_EXPIRES = Duration.ofMinutes(20).toMillis();

    // number of facet values to return from Solr: must be large enough to fetch all browse letters
    private static final int MAX_FACETS = 200;

    private static final String PREFIX = "ertlsw";

    private String basePath;

    private String browseType;

    private String componentType;

    private long expires = DEFAULT_EXPIRES;

    private SAXStrategy<List<BrowseLetter>> saxStrategy;

    private SolrService service;

    private Validity validity;

    public AtoZBrowseGenerator(final String componentType, final SolrService service,
            final SAXStrategy<List<BrowseLetter>> saxStrategy) {
        this.componentType = componentType;
        this.service = service;
        this.saxStrategy = saxStrategy;
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
        if (parameters.containsKey(Model.BASE_PATH)) {
            this.basePath = parameters.get(Model.BASE_PATH);
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        if (null != this.browseType) {
            FacetPage<Eresource> fps = this.service.facetByField(
                    "advanced:true recordType:bib AND (isRecent:1 OR isLaneConnex:1)",
                    "type:\"" + this.browseType + '"', "title_starts", 0, MAX_FACETS, 0, FacetSort.INDEX);
            List<BrowseLetter> letters = extractFacets(fps.getFacetResultPages());
            this.saxStrategy.toSAX(letters, xmlConsumer);
        }
    }

    private List<BrowseLetter> extractFacets(final Collection<Page<FacetFieldEntry>> facetResultPages) {
        List<BrowseLetter> letters = new ArrayList<>();
        for (Page<FacetFieldEntry> page : facetResultPages) {
            for (FacetFieldEntry entry : page) {
                String entryValue = entry.getValue();
                String letter = null;
                if (entryValue.startsWith(PREFIX) && entryValue.length() == PREFIX.length() + 1) {
                    letter = entryValue.substring(PREFIX.length());
                }
                if (null != letter && ACCEPTABLE_LETTERS.matcher(letter).matches()) {
                    letters.add(new BrowseLetter(this.basePath, letter, (int) entry.getValueCount()));
                }
            }
        }
        return letters;
    }
}

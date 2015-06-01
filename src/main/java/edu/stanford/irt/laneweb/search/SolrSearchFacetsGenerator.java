package edu.stanford.irt.laneweb.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.FacetQueryEntry;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.solr.Facet;
import edu.stanford.irt.laneweb.solr.FacetComparator;
import edu.stanford.irt.laneweb.solr.SolrSearchService;

public class SolrSearchFacetsGenerator extends AbstractMarshallingGenerator implements ModelAware {

    private static final String COLON = ":";

    private static final String DOT_STAR = ".*";

    private static final String EMPTY = "";

    private static final String QUOTE = "\"";

    private String facet;

    private String facets;

    private int pageNumber = 0;

    private String query;

    private SolrSearchService service;

    public SolrSearchFacetsGenerator(final SolrSearchService service, final Marshaller marshaller) {
        super(marshaller);
        this.service = service;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.facet = ModelUtil.getString(model, Model.FACET, null);
        this.facets = ModelUtil.getString(model, Model.FACETS, EMPTY);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.valueOf(page) - 1;
        }
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        FacetPage<Eresource> fps = null;
        if (null == this.facet) {
            fps = this.service.facetByManyFields(this.query, this.facets, this.pageNumber);
        } else {
            fps = this.service.facetByField(this.query, this.facets, this.facet, this.pageNumber);
        }
        marshal(processFacets(fps), xmlConsumer);
    }

    private String encodeString(final String string) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException(e);
        }
        return encoded;
    }

    private String getUrl(final String fieldName, final String facetName, final boolean isEnabled) {
        String url = null;
        String joiner = (this.facets.isEmpty()) ? EMPTY : SolrSearchService.FACETS_SEPARATOR;
        String maybeQuote = (facetName.startsWith("[") && facetName.endsWith("]")) ? EMPTY : QUOTE;
        String escapedFacetName = Pattern.quote(facetName);
        if (isEnabled) {
            url = this.facets.replaceFirst("(^|::)" + fieldName + COLON + maybeQuote + escapedFacetName + maybeQuote,
                    EMPTY);
        } else {
            url = this.facets + joiner + fieldName + COLON + maybeQuote + facetName + maybeQuote;
        }
        url = url.replaceAll("(^::|::$)", EMPTY);
        return encodeString(url);
    }

    private boolean isEnabled(final String fieldName, final String facetName) {
        if (null == this.facets) {
            return false;
        }
        String maybeQuote = (facetName.startsWith("[") && facetName.endsWith("]")) ? EMPTY : QUOTE;
        String escapedFacetName = Pattern.quote(facetName);
        return this.facets
                .matches(DOT_STAR + fieldName + COLON + maybeQuote + escapedFacetName + maybeQuote + DOT_STAR);
    }

    private Map<String, Set<Facet>> processFacets(final FacetPage<Eresource> facetpage) {
        Map<String, Set<Facet>> facetsMap = new LinkedHashMap<String, Set<Facet>>();
        // extract from facet queries
        for (FacetQueryEntry page : facetpage.getFacetQueryResult()) {
            Set<Facet> facetSet = new TreeSet<Facet>(new FacetComparator());
            String value = page.getValue();
            String fieldName = value.split(COLON)[0];
            String facetName = value.split(COLON)[1];
            long count = page.getValueCount();
            if (count > 0) {
                boolean isEnabled = isEnabled(fieldName, facetName);
                if (facetsMap.containsKey(fieldName)) {
                    facetSet = facetsMap.get(fieldName);
                }
                facetSet.add(new Facet(value, count, isEnabled, getUrl(fieldName, facetName, isEnabled)));
                facetsMap.put(fieldName, facetSet);
            }
        }
        // extract from facet fields
        for (Page<FacetFieldEntry> page : facetpage.getFacetResultPages()) {
            if (!page.hasContent()) {
                continue;
            }
            Set<Facet> facetSet = new TreeSet<Facet>(new FacetComparator());
            String fieldName = null;
            for (FacetFieldEntry entry : page) {
                if (fieldName == null) {
                    fieldName = entry.getField().getName();
                }
                String facetName = entry.getValue();
                boolean isEnabled = isEnabled(fieldName, facetName);
                if (facetsMap.containsKey(fieldName)) {
                    facetSet = facetsMap.get(fieldName);
                }
                facetSet.add(new Facet(facetName, entry.getValueCount(), isEnabled, getUrl(fieldName, facetName,
                        isEnabled)));
            }
            facetsMap.put(fieldName, facetSet);
        }
        return facetsMap;
    }
}

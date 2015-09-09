package edu.stanford.irt.laneweb.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
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

    private static final String PUBLICATION_TYPE = "publicationType";

    private static final String QUOTE = "\"";

    private static final Collection<String> REQUIRED_PUBLICATION_TYPES = Arrays.asList("Clinical Trial",
            "Randomized Controlled Trial", "Systematic Review");

    private String facet;

    private String facets;

    private int pageNumber = 0;

    private String query;

    private SolrSearchService service;

    private String facetSort;

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
        this.facetSort = ModelUtil.getString(model, Model.FACET_SORT, "");
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        FacetPage<Eresource> fps = null;
        Map<String, Collection<Facet>> facetsMap;
        if (null == this.facet) {
            // search mode
            fps = this.service.facetByManyFields(this.query, this.facets, 11);
            facetsMap = processFacets(fps);
            maybeAddRequiredPublicationTypes(facetsMap);
            marshal(sortFacets(facetsMap), xmlConsumer);
        } else {
            // browse mode
            fps = this.service.facetByField(this.query, this.facets, this.facet, this.pageNumber, 21, 1, parseSort());
            facetsMap = processFacets(fps);
            marshal(facetsMap, xmlConsumer);
        }
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

    private Map<String, Collection<Facet>> maybeAddRequiredPublicationTypes(
            final Map<String, Collection<Facet>> facetsMap) {
        Collection<Facet> facetSet = facetsMap.get(PUBLICATION_TYPE);
        if (null == facetSet) {
            facetSet = new ArrayList<Facet>();
        }
        long required = facetSet.stream().filter(s -> REQUIRED_PUBLICATION_TYPES.contains(s.getName())).count();
        if (required < 3) {
            FacetPage<Eresource> fps = this.service.facetByField(this.query, this.facets, PUBLICATION_TYPE, 0, 1000, 0,
                    parseSort());
            Map<String, Collection<Facet>> publicationTypeFacetMap = processFacets(fps);
            for (Facet f : publicationTypeFacetMap.get(PUBLICATION_TYPE)) {
                if (REQUIRED_PUBLICATION_TYPES.contains(f.getName())) {
                    facetSet.add(f);
                }
            }
            facetsMap.put(PUBLICATION_TYPE, facetSet);
        }
        return facetsMap;
    }

    private FacetSort parseSort() {
        if ("index".equals(this.facetSort)) {
            return FacetSort.INDEX;
        }
        return FacetSort.COUNT;
    }

    private Map<String, Collection<Facet>> processFacets(final FacetPage<Eresource> facetpage) {
        Map<String, Collection<Facet>> facetsMap = new LinkedHashMap<String, Collection<Facet>>();
        // extract from facet queries
        for (FacetQueryEntry page : facetpage.getFacetQueryResult()) {
            Collection<Facet> facetSet = new ArrayList<Facet>();
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
            Collection<Facet> facetSet = new ArrayList<Facet>();
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

    private Map<String, Set<Facet>> sortFacets(final Map<String, Collection<Facet>> facetsMap) {
        Map<String, Set<Facet>> sortedFacetsMap = new LinkedHashMap<String, Set<Facet>>();
        for (Map.Entry<String, Collection<Facet>> entry : facetsMap.entrySet()) {
            Set<Facet> facetSet = new TreeSet<Facet>(new FacetComparator());
            for (Facet f : entry.getValue()) {
                facetSet.add(f);
            }
            sortedFacetsMap.put(entry.getKey(), facetSet);
        }
        return sortedFacetsMap;
    }
}

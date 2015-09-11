package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.FacetQueryEntry;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.solr.Facet;
import edu.stanford.irt.laneweb.solr.FacetComparator;
import edu.stanford.irt.laneweb.solr.SolrSearchService;

public class SolrSearchFacetsGenerator extends AbstractMarshallingGenerator implements ModelAware {

    private static final String COLON = ":";

    private static final String EMPTY = "";

    private static final String MESH = "mesh";

    private static final String PUBLICATION_TYPE = "publicationType";

    private String facet;

    private String facets;

    private String facetSort;

    private int facetsToShowBrowse;

    private int facetsToShowSearch;

    private Collection<String> meshToIgnoreInSearch;

    private int pageNumber = 0;

    private String query;

    private Collection<String> requiredPublicationTypes;

    private SolrSearchService service;

    public SolrSearchFacetsGenerator(final SolrSearchService service, final Marshaller marshaller) {
        super(marshaller);
        this.service = service;
    }

    public void setFacetsToShowBrowse(final int facetsToShowBrowse) {
        // increment by one so we know if "next" link is needed
        this.facetsToShowBrowse = facetsToShowBrowse + 1;
    }

    public void setFacetsToShowSearch(final int facetsToShowSearch) {
        // increment by one so we know if "more" link is needed
        this.facetsToShowSearch = facetsToShowSearch + 1;
    }

    public void setMeshToIgnoreInSearch(final Collection<String> meshToIgnoreInSearch) {
        this.meshToIgnoreInSearch = meshToIgnoreInSearch;
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

    public void setRequiredPublicationTypes(final Collection<String> requiredPublicationTypes) {
        this.requiredPublicationTypes = requiredPublicationTypes;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        FacetPage<Eresource> fps = null;
        Map<String, Collection<Facet>> facetsMap;
        if (null == this.facet) {
            // search mode
            fps = this.service.facetByManyFields(this.query, this.facets, this.facetsToShowSearch);
            facetsMap = processFacets(fps);
            maybeAddRequiredPublicationTypes(facetsMap);
            maybeRequestMoreMesh(facetsMap);
            marshal(sortFacets(facetsMap), xmlConsumer);
        } else {
            // browse mode
            fps = this.service.facetByField(this.query, this.facets, this.facet, this.pageNumber,
                    this.facetsToShowBrowse, 1, parseSort());
            facetsMap = processFacets(fps);
            marshal(facetsMap, xmlConsumer);
        }
    }

    private Map<String, Collection<Facet>> maybeAddRequiredPublicationTypes(
            final Map<String, Collection<Facet>> facetsMap) {
        Collection<Facet> facetList = facetsMap.get(PUBLICATION_TYPE);
        if (null == facetList) {
            facetList = new ArrayList<Facet>();
        }
        long required = facetList.stream().filter(s -> this.requiredPublicationTypes.contains(s.getValue())).count();
        if (required < 3) {
            FacetPage<Eresource> fps = this.service.facetByField(this.query, this.facets, PUBLICATION_TYPE, 0, 1000, 0,
                    parseSort());
            Map<String, Collection<Facet>> publicationTypeFacetMap = processFacets(fps);
            for (Facet f : publicationTypeFacetMap.get(PUBLICATION_TYPE)) {
                if (this.requiredPublicationTypes.contains(f.getValue())) {
                    facetList.add(f);
                }
            }
            facetsMap.put(PUBLICATION_TYPE, facetList);
        }
        return facetsMap;
    }

    private Map<String, Collection<Facet>> maybeRequestMoreMesh(final Map<String, Collection<Facet>> facetsMap) {
        Collection<Facet> facetList = facetsMap.get(MESH);
        if (null == facetList) {
            facetList = new ArrayList<Facet>();
        }
        Collection<Facet> reduced = facetList.stream()
                .filter(s -> !this.meshToIgnoreInSearch.contains(s.getValue()) || s.isEnabled())
                .collect(Collectors.toList());
        if (reduced.size() < this.facetsToShowSearch) {
            int limit = this.meshToIgnoreInSearch.size() + this.facetsToShowSearch + 1;
            FacetPage<Eresource> fps = this.service.facetByField(this.query, this.facets, MESH, 0, limit, 0,
                    parseSort());
            Map<String, Collection<Facet>> meshFacetMap = processFacets(fps);
            for (Facet f : meshFacetMap.get(MESH)) {
                if (!this.meshToIgnoreInSearch.contains(f.getValue())) {
                    reduced.add(f);
                }
            }
            facetsMap.put(MESH, reduced);
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
            Collection<Facet> facetList = new ArrayList<Facet>();
            String value = page.getValue();
            String fieldName = value.split(COLON)[0];
            String facetValue = value.split(COLON)[1];
            long facetValueCount = page.getValueCount();
            if (facetsMap.containsKey(fieldName)) {
                facetList = facetsMap.get(fieldName);
            }
            if (facetValueCount > 0) {
                facetList.add(new Facet(fieldName, facetValue, facetValueCount, this.facets));
                facetsMap.put(fieldName, facetList);
            }
        }
        // extract from facet fields
        for (Page<FacetFieldEntry> page : facetpage.getFacetResultPages()) {
            if (!page.hasContent()) {
                continue;
            }
            Collection<Facet> facetList = new ArrayList<Facet>();
            String fieldName = null;
            for (FacetFieldEntry entry : page) {
                if (fieldName == null) {
                    fieldName = entry.getField().getName();
                }
                String facetValue = entry.getValue();
                if (facetsMap.containsKey(fieldName)) {
                    facetList = facetsMap.get(fieldName);
                }
                facetList.add(new Facet(fieldName, facetValue, entry.getValueCount(), this.facets));
            }
            facetsMap.put(fieldName, facetList);
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

package edu.stanford.irt.laneweb.eresources.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.EresourceFacetService;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetSort;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class FacetsGenerator extends AbstractGenerator {

    private static final String EMPTY = "";

    private static final String PUBLICATION_TYPE = "publicationType";

    private FacetComparator facetComparator;

    private JsonObject facetConfig;

    private Map<String, Collection<FacetFieldEntry>> facetFieldEntries;

    private Collection<String> facetFields;

    private String facets;

    private Collection<String> prioritizedPublicationTypes;

    private String query;

    private SAXStrategy<Map<String, Collection<FacetFieldEntry>>> saxStrategy;

    private EresourceFacetService service;

    public FacetsGenerator(final EresourceFacetService service,
            final SAXStrategy<Map<String, Collection<FacetFieldEntry>>> facetSolrSAXStrategy,
            final JsonObject facetConfig) {
        this.service = service;
        this.facetConfig = facetConfig;
        this.saxStrategy = facetSolrSAXStrategy;
        JsonObject facetResultMatch = facetConfig.asMap().get("facet-result-match").getAsJsonObject();
        String publicationTypeMatch = facetResultMatch.get(PUBLICATION_TYPE).getAsString();
        this.prioritizedPublicationTypes = Arrays.asList(publicationTypeMatch.split("\\|"));
        this.facetComparator = new FacetComparator(prioritizedPublicationTypes);
        this.facetFields = new ArrayList<String>();
        facetConfig.get("facets").getAsJsonArray().forEach(el -> this.facetFields.add(el.getAsString()));
        this.facetFieldEntries = new HashMap<>();

    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.facets = ModelUtil.getString(model, Model.FACETS, EMPTY);
        this.facetComparator.addTopPrioritiesFromFacets(this.facets);
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    private void maybeRequestMorePublicationTypes() {
        Collection<FacetFieldEntry> facetList = this.facetFieldEntries.get(PUBLICATION_TYPE);
        long count = facetList.stream()
                .filter((final FacetFieldEntry s) -> this.prioritizedPublicationTypes.contains(s.getValue())).count();
        if (facetList.size() > 0 && count < this.prioritizedPublicationTypes.size()) {
            Map<String, List<FacetFieldEntry>> fps = this.service.facetByField(this.query, this.facets,
                    PUBLICATION_TYPE, 1000, 1, FacetSort.COUNT);
            List<FacetFieldEntry> facetPage = fps.get(PUBLICATION_TYPE);
            orderFacet(PUBLICATION_TYPE, facetPage);
        }
    }

    private void orderFacet(final String facetName, final List<FacetFieldEntry> entryList) {
        Collection<FacetFieldEntry> orderedFacets = new TreeSet<>(this.facetComparator);
        orderedFacets.addAll(entryList);
        this.facetFieldEntries.put(facetName, orderedFacets);
    }

    private void orderFacets(final Map<String, List<FacetFieldEntry>> facetEntries) {
        for (String facetName : this.facetFields) {
            List<FacetFieldEntry> facetPage = facetEntries.get(facetName);
            orderFacet(facetName, facetPage);
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Map<String, List<FacetFieldEntry>> fps = this.service.facetByManyFields(this.query, this.facets,
                this.facetConfig);
        orderFacets(fps);
        maybeRequestMorePublicationTypes();
        this.saxStrategy.toSAX(this.facetFieldEntries, xmlConsumer);
    }
}

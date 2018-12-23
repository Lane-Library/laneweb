package edu.stanford.irt.laneweb.eresources.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.FacetQueryEntry;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class SolrSearchFacetsGenerator extends AbstractMarshallingGenerator {

    private static final int ALL_BOOK_OR_JOURNAL_TYPES = 3;

    private static final String COLON = ":";

    private static final Pattern COLON_PATTERN = Pattern.compile(COLON);

    private static final String EMPTY = "";

    private static final Pattern FACET_SEPARATOR_PATTERN = Pattern.compile(SolrService.FACETS_SEPARATOR);

    private static final Pattern LEADING_QUESTIONMARK_PATTERN = Pattern.compile("^?\"$?");

    private static final Logger log = LoggerFactory.getLogger(SolrSearchFacetsGenerator.class);

    private static final String MESH = "mesh";

    private static final int PAGE_SIZE = 1000;

    private static final String PUBLICATION_TYPE = "publicationType";

    private static final Pattern STARTS_WITH_BOOK_OR_JOURNAL_PATTERN = Pattern.compile("^(Book|Journal).*");

    private static final String TYPE = "type";

    private FacetComparator comparator;

    private String facet;

    private String facets;

    private String facetSort;

    private int facetsToShowBrowse;

    private int facetsToShowSearch;

    private Collection<String> meshToIgnoreInSearch;

    private int pageNumber;

    private Collection<String> prioritizedPublicationTypes;

    private String query;

    private SolrService service;

    public SolrSearchFacetsGenerator(final SolrService service, final Marshaller marshaller,
            final int facetsToShowBrowse, final int facetsToShowSearch, final Collection<String> meshToIgnoreInSearch,
            final Collection<String> publicationTypes, final FacetComparator comparator) {
        super(marshaller);
        this.service = service;
        // increment by one so we know if "next" link is needed
        this.facetsToShowBrowse = facetsToShowBrowse + 1;
        this.facetsToShowSearch = facetsToShowSearch + 1;
        this.meshToIgnoreInSearch = new HashSet<>(meshToIgnoreInSearch);
        this.prioritizedPublicationTypes = new HashSet<>(publicationTypes);
        this.comparator = comparator;
    }

    @Override
    @Deprecated
    public void setModel(final Map<String, Object> model) {
        this.facet = ModelUtil.getString(model, Model.FACET, null);
        this.facets = ModelUtil.getString(model, Model.FACETS, EMPTY);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.parseInt(page) - 1;
        }
        this.query = ModelUtil.getString(model, Model.QUERY);
        this.facetSort = ModelUtil.getString(model, Model.FACET_SORT, EMPTY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        FacetPage<Eresource> fps;
        Map<String, Collection<Facet>> facetsMap;
        try {
            if (null == this.facet) {
                // search mode
                fps = this.service.facetByManyFields(this.query, this.facets, this.facetsToShowSearch);
                facetsMap = processFacets(fps);
                maybeRequestMoreTypes(facetsMap);
                maybeRequestMorePublicationTypes(facetsMap);
                maybeAddActiveFacets(facetsMap);
                maybeRequestMoreMesh(facetsMap);
                marshal(sortFacets(facetsMap), xmlConsumer);
            } else {
                // browse mode
                fps = this.service.facetByField(this.query, this.facets, this.facet, this.pageNumber,
                        this.facetsToShowBrowse, 1, parseSort());
                facetsMap = processFacets(fps);
                marshal(facetsMap, xmlConsumer);
            }
        } catch (UncategorizedSolrException e) {
            log.error(e.toString());
            marshal("searching for facets failed", xmlConsumer);
        }
    }

    private void extractFromFacetFields(final Collection<Page<FacetFieldEntry>> facetResultPages,
            final Map<String, Collection<Facet>> facetsMap) {
        for (Page<FacetFieldEntry> page : facetResultPages) {
            if (!page.hasContent()) {
                continue;
            }
            Collection<Facet> facetList = new ArrayList<>();
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
    }

    /**
     * need to include active/enabled facets, even if they have zero results
     *
     * @param facetsMap
     * @return augmented facetsMap
     */
    private Map<String, Collection<Facet>> maybeAddActiveFacets(final Map<String, Collection<Facet>> facetsMap) {
        if (this.facets.isEmpty()) {
            return facetsMap;
        }
        String[] tokens = FACET_SEPARATOR_PATTERN.split(this.facets);
        for (String facetToken : tokens) {
            if (facetToken.contains(COLON)) {
                String[] token = COLON_PATTERN.split(facetToken);
                String fieldName = token[0];
                String facetValue = LEADING_QUESTIONMARK_PATTERN.matcher(token[1]).replaceAll(EMPTY);
                Collection<Facet> facetList = facetsMap.get(fieldName);
                if (null == facetList) {
                    facetList = new ArrayList<>();
                }
                long present = facetList.stream().filter((final Facet s) -> facetValue.equals(s.getValue())).count();
                if (present < 1) {
                    facetList.add(new Facet(fieldName, facetValue, 0, this.facets));
                    facetsMap.put(fieldName, facetList);
                }
            }
        }
        return facetsMap;
    }

    /**
     * case 110340 don't show MeSH checktags in search (unless they are active)
     *
     * @param facetsMap
     * @return augmented facetsMap
     */
    private Map<String, Collection<Facet>> maybeRequestMoreMesh(final Map<String, Collection<Facet>> facetsMap) {
        Collection<Facet> facetList = facetsMap.get(MESH);
        if (null == facetList) {
            facetList = new ArrayList<>();
        }
        Collection<Facet> reduced = facetList.stream()
                .filter((final Facet s) -> !this.meshToIgnoreInSearch.contains(s.getValue()) || s.isEnabled())
                .collect(Collectors.toList());
        long enabled = facetList.stream().filter(Facet::isEnabled).count();
        if (reduced.size() < this.facetsToShowSearch || enabled >= this.facetsToShowSearch) {
            int limit = this.meshToIgnoreInSearch.size() + this.facetsToShowSearch + 1;
            FacetPage<Eresource> fps = this.service.facetByField(this.query, this.facets, MESH, 0, limit, 1,
                    parseSort());
            facetList = processFacets(fps).get(MESH);
            if (null != facetList) {
                Collection<Facet> moreMesh = facetList.stream()
                        .filter((final Facet s) -> !this.meshToIgnoreInSearch.contains(s.getValue()) || s.isEnabled())
                        .collect(Collectors.toList());
                facetsMap.put(MESH, moreMesh);
            }
        }
        return facetsMap;
    }

    /**
     * cases 110125 and 121834: give some Article Types display priority
     *
     * @param facetsMap
     * @return augmented facetsMap
     */
    private Map<String, Collection<Facet>> maybeRequestMorePublicationTypes(
            final Map<String, Collection<Facet>> facetsMap) {
        Collection<Facet> facetList = facetsMap.get(PUBLICATION_TYPE);
        if (null == facetList) {
            facetList = new ArrayList<>();
        }
        long count = facetList.stream()
                .filter((final Facet s) -> this.prioritizedPublicationTypes.contains(s.getValue())).count();
        if (count < this.prioritizedPublicationTypes.size()) {
            FacetPage<Eresource> fps = this.service.facetByField(this.query, this.facets, PUBLICATION_TYPE, 0,
                    PAGE_SIZE, 1, parseSort());
            Map<String, Collection<Facet>> publicationTypeFacetMap = processFacets(fps);
            facetList = publicationTypeFacetMap.get(PUBLICATION_TYPE);
            if (null != facetList) {
                Collection<Facet> moreTypes = facetList.stream()
                        .filter((final Facet s) -> this.prioritizedPublicationTypes.contains(s.getValue()))
                        .collect(Collectors.toList());
                facetList.addAll(moreTypes);
            }
            facetsMap.put(PUBLICATION_TYPE, facetList);
        }
        return facetsMap;
    }

    private Map<String, Collection<Facet>> maybeRequestMoreTypes(final Map<String, Collection<Facet>> facetsMap) {
        Collection<Facet> facetList = facetsMap.get(TYPE);
        if (null == facetList) {
            facetList = new ArrayList<>();
        }
        long books = facetList.stream().filter((final Facet s) -> s.getValue().startsWith("Book")).count();
        long journals = facetList.stream().filter((final Facet s) -> s.getValue().startsWith("Journal")).count();
        if ((books > 0 && books < ALL_BOOK_OR_JOURNAL_TYPES)
                || (journals > 0 && journals < ALL_BOOK_OR_JOURNAL_TYPES)) {
            FacetPage<Eresource> fps = this.service.facetByField(this.query, this.facets, TYPE, 0, PAGE_SIZE, 1,
                    parseSort());
            Map<String, Collection<Facet>> typeFacetMap = processFacets(fps);
            Collection<Facet> allTypes = typeFacetMap.get(TYPE);
            if (null != allTypes) {
                Collection<Facet> moreTypes = allTypes.stream()
                        .filter((final Facet s) -> STARTS_WITH_BOOK_OR_JOURNAL_PATTERN.matcher(s.getValue()).matches())
                        .collect(Collectors.toList());
                facetList.addAll(moreTypes);
            }
            facetsMap.put(TYPE, facetList);
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
        Map<String, Collection<Facet>> facetsMap = new LinkedHashMap<>();
        // extract from facet queries
        for (FacetQueryEntry page : facetpage.getFacetQueryResult()) {
            Collection<Facet> facetList = new ArrayList<>();
            String pageValue = page.getValue();
            if (pageValue.contains(COLON)) {
                String[] value = COLON_PATTERN.split(pageValue);
                String fieldName = value[0];
                String facetValue = value[1];
                long facetValueCount = page.getValueCount();
                if (facetsMap.containsKey(fieldName)) {
                    facetList = facetsMap.get(fieldName);
                }
                if (facetValueCount > 0) {
                    facetList.add(new Facet(fieldName, facetValue, facetValueCount, this.facets));
                    facetsMap.put(fieldName, facetList);
                }
            }
        }
        // extract from facet fields
        extractFromFacetFields(facetpage.getFacetResultPages(), facetsMap);
        return facetsMap;
    }

    private Map<String, Set<Facet>> sortFacets(final Map<String, Collection<Facet>> facetsMap) {
        Map<String, Set<Facet>> sortedFacetsMap = new LinkedHashMap<>();
        for (Map.Entry<String, Collection<Facet>> entry : facetsMap.entrySet()) {
            Set<Facet> facetSet = new TreeSet<>(this.comparator);
            Collection<Facet> facetList = entry.getValue();
            if (null != facetList) {
                facetSet.addAll(facetList);
            }
            sortedFacetsMap.put(entry.getKey(), facetSet);
        }
        return sortedFacetsMap;
    }
}

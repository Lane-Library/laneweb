package edu.stanford.irt.laneweb.solr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import edu.stanford.irt.laneweb.eresources.CollectionManager;
import edu.stanford.irt.laneweb.eresources.Eresource;

public class SolrSearchService implements CollectionManager {

    private static final String AND = " AND ";

    private static final String EMPTY = "";

    @Autowired
    private SolrRepository repository;

    public FacetPage<Eresource> facetByManyFields(final String query, final String filters,
            final PageRequest pageRequest) {
        return this.repository.facetByManyFields(query, facetStringToFilters(filters), pageRequest);
    }

    @Override
    public List<Eresource> getCore(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return this.repository.browseAllCoreByType(SolrTypeManager.backwardsCompatibleType(type), new PageRequest(0,
                Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getMesh(final String type, final String mesh) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        if (null == mesh) {
            throw new IllegalArgumentException("null mesh");
        }
        return this.repository.browseAllByMeshAndType(mesh, SolrTypeManager.backwardsCompatibleType(type),
                new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getSubset(final String subset) {
        if (null == subset) {
            throw new IllegalArgumentException("null subset");
        }
        return this.repository.browseAllBySubset(subset, new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getType(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return this.repository.browseAllByType(SolrTypeManager.backwardsCompatibleType(type), new PageRequest(0,
                Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getType(final String type, final char alpha) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        char sAlpha = alpha;
        // solr stores starts with numeric as '1'
        if ('#' == sAlpha) {
            sAlpha = '1';
        }
        return this.repository.browseByTypeTitleStartingWith(SolrTypeManager.backwardsCompatibleType(type),
                Character.toString(sAlpha), new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> search(final String query) {
        return this.repository.searchFindAllWithFilter(query, EMPTY, new PageRequest(0, 50)).getContent();
        // return this.repository.searchFindAll(query, new PageRequest(0, 50)).getContent();
    }

    @Override
    public Map<String, Integer> searchCount(final Set<String> types, final String query) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        SolrResultPage<?> facets = this.repository.facetByType(query, new PageRequest(0, 1));
        int total = (int) facets.getTotalElements();
        result.put("all", Integer.valueOf(total));
        for (Page<FacetFieldEntry> page : facets.getFacetResultPages()) {
            for (FacetFieldEntry entry : page) {
                int value = (int) entry.getValueCount();
                String fieldName = entry.getValue();
                if (types.contains(fieldName)) {
                    result.put(fieldName, Integer.valueOf(value));
                }
            }
        }
        return result;
    }

    @Override
    public List<Eresource> searchType(final String type, final String query) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return this.repository.searchFindByType(query, SolrTypeManager.backwardsCompatibleType(type),
                new PageRequest(0, 50)).getContent();
    }

    public Page<Eresource> searchType(final String type, final String query, final PageRequest pageRequest) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return this.repository.searchFindByType(query, SolrTypeManager.backwardsCompatibleType(type), pageRequest);
    }

    public Page<Eresource> searchWithFilters(final String query, final String facets, final PageRequest pageRequest) {
        return this.repository.searchFindAllWithFilter(query, facetStringToFilters(facets), pageRequest);
    }

    public String facetStringToFilters(final String facets) {
        String filters = EMPTY;
        if (null != facets) {
            filters = facets.replaceFirst("::$", EMPTY);
            filters = filters.replaceAll("::", AND);
        }
        return filters;
    }
}
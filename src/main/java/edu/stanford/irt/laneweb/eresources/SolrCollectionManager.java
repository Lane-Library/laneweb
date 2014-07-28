package edu.stanford.irt.laneweb.eresources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import edu.stanford.irt.laneweb.solr.SolrRepository;

public class SolrCollectionManager implements CollectionManager {

    @Autowired
    private SolrRepository repository;

    @Override
    public List<Eresource> getCore(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return this.repository.browseAllCoreByType(type, new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getMesh(final String type, final String mesh) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        if (null == mesh) {
            throw new IllegalArgumentException("null mesh");
        }
        return this.repository.browseAllByMeshAndType(mesh, type, new PageRequest(0, Integer.MAX_VALUE));
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
        return this.repository.browseAllByType(type, new PageRequest(0, Integer.MAX_VALUE));
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
        return this.repository.browseByTypeTitleStartingWith(type, Character.toString(sAlpha), new PageRequest(0,
                Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> search(final String query) {
        return this.repository.searchFindAll(query, new PageRequest(0, 50));
    }

    // TODO: remove these when upgrading to 1.8
    @Override
    public Map<String, Integer> searchCount(final Set<String> types, final Set<String> subsets, final String query) {
        return searchCount(types, query);
    }

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

    public List<Eresource> searchSubset(final String subset, final String query) {
        if (null == subset) {
            throw new IllegalArgumentException("null subset");
        }
        return this.repository.searchFindBySubset(query, subset, new PageRequest(0, 50));
    }

    @Override
    public List<Eresource> searchType(final String type, final String query) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return this.repository.searchFindByType(query, type, new PageRequest(0, 50));
    }
}
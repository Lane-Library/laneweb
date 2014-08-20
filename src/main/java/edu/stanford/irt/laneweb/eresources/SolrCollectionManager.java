package edu.stanford.irt.laneweb.eresources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import edu.stanford.irt.laneweb.solr.SolrRepository;

public class SolrCollectionManager implements CollectionManager {

    private static final Map<String, String> TYPES_MAP = new HashMap<String, String>();
    static {
        TYPES_MAP.put("ej", "Journal");
        TYPES_MAP.put("cc", "Clinical Decision Tools");
        TYPES_MAP.put("m051 software, installed", "Software, Installed - M051");
        TYPES_MAP.put("redwood software, installed", "Software, Installed - Redwood Room");
        TYPES_MAP.put("duck software, installed", "Software, Installed - Duck Room");
        TYPES_MAP.put("stone software, installed", "Software, Installed - Stone Room");
        TYPES_MAP.put("lksc-public software, installed", "Software, Installed - LKSC Public");
        TYPES_MAP.put("lksc-student software, installed", "Software, Installed - LKSC Student");
    }

    @Autowired
    private SolrRepository repository;

    /**
     * backwards-compatible type-mapping; remove once types changed in biomed-resources browse pages
     * 
     * @param maybeOldType
     * @return new type from TYPES_MAP or title-case version of maybeOldType
     */
    private String backwardsCompatibleType(final String maybeOldType) {
        if (TYPES_MAP.containsKey(maybeOldType)) {
            return TYPES_MAP.get(maybeOldType);
        }
        return WordUtils.capitalize(maybeOldType);
    }

    @Override
    public List<Eresource> getCore(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return this.repository
                .browseAllCoreByType(backwardsCompatibleType(type), new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getMesh(final String type, final String mesh) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        if (null == mesh) {
            throw new IllegalArgumentException("null mesh");
        }
        return this.repository.browseAllByMeshAndType(mesh, backwardsCompatibleType(type), new PageRequest(0,
                Integer.MAX_VALUE));
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
        return this.repository.browseAllByType(backwardsCompatibleType(type), new PageRequest(0, Integer.MAX_VALUE));
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
        return this.repository.browseByTypeTitleStartingWith(backwardsCompatibleType(type), Character.toString(sAlpha),
                new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> search(final String query) {
        return this.repository.searchFindAll(query, new PageRequest(0, 50));
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
        return this.repository.searchFindByType(query, backwardsCompatibleType(type), new PageRequest(0, 50));
    }
}
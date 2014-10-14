package edu.stanford.irt.laneweb.solr;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

public class SolrTypeManager {

    private static final Map<String, String> TYPES_MAP = new HashMap<String, String>();
    static {
        TYPES_MAP.put("book", "Book");
        TYPES_MAP.put("ej", "Journal");
        TYPES_MAP.put("cc", "Clinical Decision Tools");
        TYPES_MAP.put("m051 software, installed", "Software, Installed - M051");
        TYPES_MAP.put("redwood software, installed", "Software, Installed - Redwood Room");
        TYPES_MAP.put("duck software, installed", "Software, Installed - Duck Room");
        TYPES_MAP.put("stone software, installed", "Software, Installed - Stone Room");
        TYPES_MAP.put("lksc-public software, installed", "Software, Installed - LKSC Public");
        TYPES_MAP.put("lksc-student software, installed", "Software, Installed - LKSC Student");
    }

    /**
     * backwards-compatible type-mapping; remove once types changed in biomed-resources browse pages
     *
     * @param maybeOldType
     * @return new type from TYPES_MAP or title-case version of maybeOldType
     */
    public static final String backwardsCompatibleType(final String maybeOldType) {
        if (TYPES_MAP.containsKey(maybeOldType)) {
            return TYPES_MAP.get(maybeOldType);
        }
        return WordUtils.capitalize(maybeOldType);
    }
}

package edu.stanford.irt.laneweb.solr;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.WordUtils;

public class SolrTypeManager {

    private static final Map<String, String> NEW_TO_OLD_TYPES_MAP = new HashMap<String, String>();

    private static final Map<String, String> OLD_TO_NEW_TYPES_MAP = new HashMap<String, String>();

    static {
        OLD_TO_NEW_TYPES_MAP.put("bookdigital", "Book Digital");
        OLD_TO_NEW_TYPES_MAP.put("bookprint", "Book Print");
        OLD_TO_NEW_TYPES_MAP.put("ej", "Journal");
        OLD_TO_NEW_TYPES_MAP.put("cc", "Clinical Decision Tools");
        OLD_TO_NEW_TYPES_MAP.put("journaldigital", "Journal Digital");
        OLD_TO_NEW_TYPES_MAP.put("journalprint", "Journal Print");
        OLD_TO_NEW_TYPES_MAP.put("laneclass", "Lane Class");
        OLD_TO_NEW_TYPES_MAP.put("lanesite", "Lane Web Page");
        OLD_TO_NEW_TYPES_MAP.put("m051 software, installed", "Software, Installed - M051");
        OLD_TO_NEW_TYPES_MAP.put("redwood software, installed", "Software, Installed - Redwood Room");
        OLD_TO_NEW_TYPES_MAP.put("duck software, installed", "Software, Installed - Duck Room");
        OLD_TO_NEW_TYPES_MAP.put("stone software, installed", "Software, Installed - Stone Room");
        OLD_TO_NEW_TYPES_MAP.put("lksc-public software, installed", "Software, Installed - LKSC Public");
        OLD_TO_NEW_TYPES_MAP.put("lksc-student software, installed", "Software, Installed - LKSC Student");
    }

    private SolrTypeManager() {
        // empty private constructor
    }

    /**
     * backwards-compatible type-mapping TODO: remove once types changed in biomed-resources browse pages
     *
     * @param maybeOldType
     *            type that may be in non-Solr format
     * @return new type from OLD_TO_NEW_TYPES_MAP or title-case version of maybeOldType
     */
    public static final String convertToNewType(final String maybeOldType) {
        if (OLD_TO_NEW_TYPES_MAP.containsKey(maybeOldType)) {
            return OLD_TO_NEW_TYPES_MAP.get(maybeOldType);
        }
        return WordUtils.capitalize(maybeOldType);
    }

    /**
     * backwards-compatible type-mapping TODO: remove once types changed in search-lane lane.xml engine
     *
     * @param maybeNewType
     *            type that may be in Solr format
     * @return old type from OLD_TO_NEW_TYPES_MAP or lower-case version of maybeNewType
     */
    public static final String convertToOldType(final String maybeNewType) {
        if (NEW_TO_OLD_TYPES_MAP.isEmpty()) {
            for (Entry<String, String> entry : OLD_TO_NEW_TYPES_MAP.entrySet()) {
                NEW_TO_OLD_TYPES_MAP.put(entry.getValue(), entry.getKey());
            }
        }
        if (NEW_TO_OLD_TYPES_MAP.containsKey(maybeNewType)) {
            return NEW_TO_OLD_TYPES_MAP.get(maybeNewType);
        }
        return maybeNewType.toLowerCase();
    }
}

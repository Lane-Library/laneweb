package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;

public class EresourceCollectionManager extends AbstractSuggestCollectionManager {
  
    private static final String SEARCH = 
    "SELECT TITLE, "
    + "    ERESOURCE_ID,  "
    + "    NLSSORT_TITLE, "
    + "    300 as SCORE "
    + "      FROM ERESOURCE "
    + "      WHERE ASCII_TITLE LIKE CONVERT(?,'US7ASCII') || '%' "
    + "UNION "
    + "SELECT TITLE, "
    + "    ERESOURCE_ID, "
    + "    NLSSORT_TITLE, "
    + "    (CONTAINS(TITLE,?) * 2) + SCORE(1) AS SCORE "
    + "      FROM ERESOURCE "
    + "      WHERE CONTAINS(TEXT,?,1) > 0 "
    + "ORDER BY SCORE DESC, "
    + "  NLSSORT_TITLE ";
  
  private static final String SEARCH_TYPE = 
    "SELECT TITLE, "
    + "    ERESOURCE.ERESOURCE_ID, "
    + "    NLSSORT_TITLE, "
    + "    300 as SCORE "
    + "      FROM ERESOURCE, TYPE "
    + "      WHERE ASCII_TITLE LIKE CONVERT(?,'US7ASCII') || '%' "
    + "          AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
    + "          AND TYPE = ? "
    + "UNION "
    + "SELECT TITLE, "
    + "    ERESOURCE.ERESOURCE_ID, "
    + "    NLSSORT_TITLE, "
    + "    (CONTAINS(TITLE,?) * 2) + SCORE(1) AS SCORE "
    + "      FROM ERESOURCE, TYPE "
    + "      WHERE CONTAINS(TEXT,?,1) > 0 "
    + "          AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
    + "          AND TYPE = ? "
    + "ORDER BY SCORE DESC, "
    + "  NLSSORT_TITLE ";

    @Override
    protected String getSearchSQL() {
        return SEARCH;
    }

    @Override
    protected String getSearchTypeSQL() {
        return SEARCH_TYPE;
    }

    @Override
    protected Collection<String> getSearchParams(final String query) {
        Collection<String> params = new LinkedList<String>();
        params.add(this.queryNormalizer.normalizeForLike(query));
        params.add(this.queryNormalizer.normalizeForWildcardContains(query));
        params.add(this.queryNormalizer.normalizeForContains(query));
        return params;
    }

    @Override
    protected Collection<String> getSearchTypeParams(String type, String query) {
        Collection<String> params = new LinkedList<String>();
        params.add(this.queryNormalizer.normalizeForLike(query));
        params.add(type);
        params.add(this.queryNormalizer.normalizeForWildcardContains(query));
        params.add(this.queryNormalizer.normalizeForContains(query));
        params.add(type);
        return params;
    }
}

package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;

public class HistoryCollectionManager extends AbstractSuggestCollectionManager {

    private static final String SEARCH =
    "SELECT TITLE, "
    + "    ERESOURCE_ID, "
    + "    NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE  "
    + "    SCORE(1)     * 2     AS SCORE "
    + "      FROM H_ERESOURCE "
    + "      WHERE CONTAINS(TITLE,?,1) > 0 "
    + "      AND ROWNUM < 100 "
    + "UNION "
    + "SELECT TITLE, "
    + "    ERESOURCE_ID, "
    + "    NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE  "
    + "    SCORE(1)          AS SCORE "
    + "      FROM H_ERESOURCE "
    + "      WHERE CONTAINS(TEXT,?,1) > 0 "
    + "      AND ROWNUM < 100 "
    + "ORDER BY SCORE DESC, "
    + "  SORT_TITLE ";

    @Override
    protected String getSearchSQL() {
        return SEARCH;
    }

    @Override
    protected String getSearchTypeSQL() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Collection<String> getSearchParams(String query) {
        Collection<String> params = new LinkedList<String>();
        String normalizedQuery = this.queryNormalizer.normalizeForContains(query);
        params.add(normalizedQuery);
        params.add(normalizedQuery);
        return params;
    }

    @Override
    protected Collection<String> getSearchTypeParams(String type, String query) {
        throw new UnsupportedOperationException();
    }
}

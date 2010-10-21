package edu.stanford.irt.laneweb.suggest;

public class HistoryCollectionManager extends AbstractSuggestCollectionManager {

    private static final String SEARCH =
    "WITH FOUND AS( "
    + "SELECT TITLE, "
    + "    H_ERESOURCE.ERESOURCE_ID, "
    + "    SCORE(1)     * 2     AS SCORE "
    + "      FROM H_ERESOURCE "
    + "      WHERE CONTAINS(TITLE,?,1) > 0 "
    + "      AND ROWNUM < 100 "
    + "UNION "
    + "SELECT TITLE, "
    + "    H_ERESOURCE.ERESOURCE_ID, "
    + "    SCORE(1)          AS SCORE "
    + "      FROM H_ERESOURCE "
    + "      WHERE CONTAINS(TEXT,?,1) > 0 "
    + "      AND ROWNUM < 100 "
    + ") "
    + "SELECT FOUND.ERESOURCE_ID, "
    + "  TITLE, "
    + "  SCORE, "
    + "  NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE  "
    + "  FROM FOUND "
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
}

package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;

public class EresourceCollectionManager extends AbstractSuggestCollectionManager {
  
    private static final String SEARCH = 
    "WITH FOUND AS( "
    + "SELECT TITLE, "
    + "    ERESOURCE.ERESOURCE_ID,  "
    + "    300 as SCORE "
    + "      FROM ERESOURCE "
    + "      WHERE CONVERT(LOWER(TITLE),'US7ASCII') LIKE CONVERT(?,'US7ASCII') || '%' "
    + "UNION "
    + "SELECT TITLE, "
    + "    ERESOURCE.ERESOURCE_ID, "
    + "    SCORE(1)   * 2       AS SCORE "
    + "      FROM ERESOURCE "
    + "      WHERE CONTAINS(TITLE,?,1) > 0 "
    + "UNION "
    + "SELECT TITLE, "
    + "    ERESOURCE.ERESOURCE_ID, "
    + "    SCORE(1)          AS SCORE "
    + "      FROM ERESOURCE "
    + "      WHERE CONTAINS(TEXT,?,1) > 0 "
    + ") "
    + "SELECT FOUND.ERESOURCE_ID, "
    + "  TITLE, "
    + "  SCORE, "
    + "  NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE  "
    + "  FROM FOUND "
    + "ORDER BY SCORE DESC, "
    + "  SORT_TITLE ";
  
  private static final String SEARCH_TYPE = 
    "WITH FOUND AS( "
    + "SELECT TITLE, "
    + "    ERESOURCE.ERESOURCE_ID,  "
    + "    300 as SCORE "
    + "      FROM ERESOURCE "
    + "      WHERE CONVERT(LOWER(TITLE),'US7ASCII') LIKE CONVERT(?,'US7ASCII') || '%' "
    + "UNION "
    + "SELECT TITLE, "
    + "    ERESOURCE.ERESOURCE_ID, "
    + "    SCORE(1)   * 2       AS SCORE "
    + "      FROM ERESOURCE "
    + "      WHERE CONTAINS(TITLE,?,1) > 0 "
    + "UNION "
    + "SELECT TITLE, "
    + "    ERESOURCE.ERESOURCE_ID, "
    + "    SCORE(1)          AS SCORE "
    + "      FROM ERESOURCE "
    + "      WHERE CONTAINS(TEXT,?,1) > 0 "
    + ") "
    + "SELECT FOUND.ERESOURCE_ID, "
    + "  TITLE, "
    + "  SCORE, "
    + "  NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE  "
    + "  FROM FOUND, TYPE  "
    + "  WHERE FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
    + "AND TYPE = ? "
    + "ORDER BY SCORE DESC, "
    + "  SORT_TITLE ";

    @Override
    protected String getSearchSQL() {
        return SEARCH;
    }

    @Override
    protected String getSearchTypeSQL() {
        return SEARCH_TYPE;
    }

    @Override
    protected Collection<String> searchStringToParams(final String term) {
        Collection<String> params = new LinkedList<String>();
        String likeSearchString = this.queryNormalizer.normalizeForLike(term);
        String titleSearchString = this.queryNormalizer.normalizeForWildcardContains(term);
        params.add(likeSearchString);
        params.add(titleSearchString);
        params.add(this.queryNormalizer.normalizeForContains(term));
        return params;
    }
}

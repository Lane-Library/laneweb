package edu.stanford.irt.laneweb.eresources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.eresources.impl.EresourceImpl;
import edu.stanford.irt.eresources.impl.LinkImpl;
import edu.stanford.irt.eresources.impl.QueryTranslator;
import edu.stanford.irt.eresources.impl.VersionImpl;
import edu.stanford.irt.laneweb.JdbcUtils;

public class HistoryCollectionManager implements CollectionManager {

    private static final String BROWSE =
        "SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "UNION SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String BROWSE_ALPHA =
        "SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "AND NLSSORT(SUBSTR(TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') = NLSSORT(?,'NLS_SORT=GENERIC_BASELETTER') "
        + "UNION SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "AND NLSSORT(SUBSTR(PREFERRED_TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') = NLSSORT(?,'NLS_SORT=GENERIC_BASELETTER') "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String BROWSE_NONALPHA =
        "SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "AND (NLSSORT(SUBSTR(TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') < NLSSORT('A','NLS_SORT=GENERIC_BASELETTER') "
        + "OR NLSSORT(SUBSTR(TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') > NLSSORT('z','NLS_SORT=GENERIC_BASELETTER')) "
        + "UNION SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "AND (NLSSORT(SUBSTR(PREFERRED_TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') < NLSSORT('A','NLS_SORT=GENERIC_BASELETTER') "
        + "OR NLSSORT(SUBSTR(PREFERRED_TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') > NLSSORT('z','NLS_SORT=GENERIC_BASELETTER')) "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String CORE =
        "SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.CORE = 'Y'AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "UNION SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.CORE = 'Y'AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String COUNT =
        "WITH FOUND AS (SELECT H_ERESOURCE.ERESOURCE_ID, H_TYPE.TYPE, H_SUBSET.SUBSET FROM H_ERESOURCE, H_TYPE, H_SUBSET "
        + "WHERE CONTAINS(H_ERESOURCE.TEXT,?) > 0 "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_SUBSET.ERESOURCE_ID(+)) "
        + "SELECT 'all' AS GENRE, COUNT(DISTINCT ERESOURCE_ID) AS HITS FROM FOUND";

    private static final String COUNT_SUBSET_UNION = " UNION SELECT ? AS GENRE, COUNT(DISTINCT ERESOURCE_ID) AS HITS FROM FOUND WHERE SUBSET = ?";

    private static final String COUNT_TYPE_UNION = " UNION SELECT ? AS GENRE, COUNT(DISTINCT ERESOURCE_ID) AS HITS FROM FOUND WHERE TYPE = ?";

    private static final String MESH =
        "SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_MESH, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_MESH.ERESOURCE_ID "
        + "AND H_MESH.TERM = ? "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "UNION SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_MESH, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_MESH.ERESOURCE_ID "
        + "AND H_MESH.TERM = ? "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String MESH_CORE =
        "SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_MESH, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_MESH.ERESOURCE_ID "
        + "AND H_MESH.TERM = ? "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "AND H_ERESOURCE.CORE = 'Y' "
        + "UNION SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_MESH, H_TYPE "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_MESH.ERESOURCE_ID "
        + "AND H_MESH.TERM = ? "
        + "AND H_ERESOURCE.ERESOURCE_ID = H_TYPE.ERESOURCE_ID "
        + "AND H_TYPE.TYPE = ? "
        + "AND H_ERESOURCE.CORE = 'Y' "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH =
        "WITH FOUND AS ( "
        + "SELECT TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, SCORE(1) * 3 AS SCORE_TEXT, "
        + "CONTAINS(TITLE,?) * 3 AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE = 'Y' "
        + "UNION "
        + "SELECT PREFERRED_TITLE AS TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, SCORE(1) * 3 AS SCORE_TEXT, "
        + "CONTAINS(PREFERRED_TITLE,?) * 3 AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE = 'Y' "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "UNION "
        + "SELECT TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
        + "CONTAINS(TITLE,?) AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE IS NULL "
        + "UNION "
        + "SELECT PREFERRED_TITLE AS TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, "
        + "SCORE(1) AS SCORE_TEXT, CONTAINS(PREFERRED_TITLE,?) AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE IS NULL "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + ") "
        + "SELECT FOUND.ERESOURCE_ID, FOUND.RECORD_TYPE, FOUND.RECORD_ID, H_VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM FOUND, H_VERSION, H_LINK, H_TYPE, H_SUBSET "
        + "WHERE FOUND.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND FOUND.ERESOURCE_ID = H_TYPE.ERESOURCE_ID(+) "
        + "AND H_VERSION.VERSION_ID = H_SUBSET.VERSION_ID(+) "
        + "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH_SUBSET =
        "WITH FOUND AS ( "
        + "SELECT TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, SCORE(1) * 3 AS SCORE_TEXT, "
        + "CONTAINS(TITLE,?) * 3 AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE = 'Y' "
        + "UNION "
        + "SELECT PREFERRED_TITLE AS TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, SCORE(1) * 3 AS SCORE_TEXT, "
        + "CONTAINS(PREFERRED_TITLE,?) * 3 AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE = 'Y' "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "UNION "
        + "SELECT TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
        + "CONTAINS(TITLE,?) AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE IS NULL "
        + "UNION "
        + "SELECT PREFERRED_TITLE AS TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, "
        + "SCORE(1) AS SCORE_TEXT, CONTAINS(PREFERRED_TITLE,?) AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE IS NULL "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + ") "
        + "SELECT FOUND.ERESOURCE_ID, FOUND.RECORD_TYPE, FOUND.RECORD_ID, H_VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM FOUND, H_VERSION, H_LINK, H_TYPE, H_SUBSET "
        + "WHERE FOUND.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND FOUND.ERESOURCE_ID = H_TYPE.ERESOURCE_ID(+) "
        + "AND H_VERSION.VERSION_ID = H_SUBSET.VERSION_ID(+) "
        + "AND SUBSET = ? "
        + "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH_TYPE =
        "WITH FOUND AS ( "
        + "SELECT TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, SCORE(1) * 3 AS SCORE_TEXT, "
        + "CONTAINS(TITLE,?) * 3 AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE = 'Y' "
        + "UNION "
        + "SELECT PREFERRED_TITLE AS TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, SCORE(1) * 3 AS SCORE_TEXT, "
        + "CONTAINS(PREFERRED_TITLE,?) * 3 AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE = 'Y' "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "UNION "
        + "SELECT TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
        + "CONTAINS(TITLE,?) AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE IS NULL "
        + "UNION "
        + "SELECT PREFERRED_TITLE AS TITLE, H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, "
        + "SCORE(1) AS SCORE_TEXT, CONTAINS(PREFERRED_TITLE,?) AS SCORE_TITLE "
        + "FROM H_ERESOURCE "
        + "WHERE CONTAINS(TEXT,?,1) > 0 "
        + "AND CORE IS NULL "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + ") "
        + "SELECT FOUND.ERESOURCE_ID, FOUND.RECORD_TYPE, FOUND.RECORD_ID, H_VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM FOUND, H_VERSION, H_LINK, H_TYPE, H_SUBSET "
        + "WHERE FOUND.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND FOUND.ERESOURCE_ID = H_TYPE.ERESOURCE_ID(+) "
        + "AND H_VERSION.VERSION_ID = H_SUBSET.VERSION_ID(+) "
        + "AND TYPE = ? "
        + "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SUBSET =
        "SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_SUBSET "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_VERSION.VERSION_ID = H_SUBSET.VERSION_ID "
        + "AND H_SUBSET.SUBSET = ? "
        + "UNION SELECT H_ERESOURCE.ERESOURCE_ID, H_ERESOURCE.RECORD_TYPE, H_ERESOURCE.RECORD_ID, H_VERSION.VERSION_ID, H_LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER, "
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, "
        + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
        + "FROM H_ERESOURCE, H_VERSION, H_LINK, H_SUBSET "
        + "WHERE H_ERESOURCE.ERESOURCE_ID = H_VERSION.ERESOURCE_ID "
        + "AND H_VERSION.VERSION_ID = H_LINK.VERSION_ID "
        + "AND H_VERSION.VERSION_ID = H_SUBSET.VERSION_ID "
        + "AND H_SUBSET.SUBSET = ? "
        + "AND PREFERRED_TITLE IS NOT NULL "
        + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private DataSource dataSource;

    public Collection<Eresource> getCore(final String type) {
        Collection<String> params = new LinkedList<String>();
        params.add(type);
        params.add(type);
        return doGet(CORE, params);
    }

    public Collection<Eresource> getMesh(final String type, final String mesh) {
        Collection<String> params = new LinkedList<String>();
        params.add(mesh);
        params.add(type);
        params.add(mesh);
        params.add(type);
        return doGet(MESH, params);
    }

    public Collection<Eresource> getMeshCore(final String type, final String mesh) {
        Collection<String> params = new LinkedList<String>();
        params.add(mesh);
        params.add(type);
        params.add(mesh);
        params.add(type);
        return doGet(MESH_CORE, params);
    }

    public Collection<Eresource> getSubset(final String subset) {
        Collection<String> params = new LinkedList<String>();
        params.add(subset);
        params.add(subset);
        return doGet(SUBSET, params);
    }

    public Collection<Eresource> getType(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        Collection<String> params = new LinkedList<String>();
        params.add(type);
        params.add(type);
        return doGet(BROWSE, params);
    }

    public Collection<Eresource> getType(final String type, final char alpha) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        Collection<String> params = new LinkedList<String>();
        String sql = null;
        if ('#' == alpha) {
            sql = BROWSE_NONALPHA;
            params.add(type);
            params.add(type);
        } else {
            sql = BROWSE_ALPHA;
            String alphaString = new String(new char[] { alpha });
            params.add(type);
            params.add(alphaString);
            params.add(type);
            params.add(alphaString);
        }
        return doGet(sql, params);
    }

    public Collection<Eresource> search(final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 8; i++) {
            params.add(translatedQuery);
        }
        return doGetSearch(SEARCH, params, query);
    }

    public Map<String, Integer> searchCount(final Set<String> types, final Set<String> subsets, final String query) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        StringBuffer stringBuffer = new StringBuffer(COUNT);
        for (int i = 0; i < types.size(); i++) {
            stringBuffer.append(COUNT_TYPE_UNION);
        }
        for (int i = 0; i < subsets.size(); i++) {
            stringBuffer.append(COUNT_SUBSET_UNION);
        }
        String sql = stringBuffer.toString();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, new QueryTranslator().translate(query));
            for (String type : types) {
                stmt.setString(index++, type);
                stmt.setString(index++, type);
            }
            for (String subset : subsets) {
                stmt.setString(index++, subset);
                stmt.setString(index++, subset);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString(1), Integer.valueOf(rs.getInt(2)));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
        return result;
    }

    public Collection<Eresource> searchSubset(final String subset, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 8; i++) {
            params.add(translatedQuery);
        }
        params.add(subset);
        return doGetSearch(SEARCH_SUBSET, params, query);
    }

    public Collection<Eresource> searchType(final String type, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 8; i++) {
            params.add(translatedQuery);
        }
        params.add(type);
        return doGetSearch(SEARCH_TYPE, params, query);
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

    private LinkedList<Eresource> doGet(final String sql, final Collection<String> params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            for (String param : params) {
                stmt.setString(index++, param);
            }
            rs = stmt.executeQuery();
            return parseResultSet(rs);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    private Collection<Eresource> doGetSearch(final String sql, final Collection<String> params, final String query) {
        LinkedList<Eresource> result = doGet(sql, params);
        LinkedList<Eresource> titleMatches = new LinkedList<Eresource>();
        String normQuery = query.replaceAll("\\W", "");
        int i = 0;
        for (ListIterator<Eresource> it = result.listIterator(); it.hasNext() && (i < 20); i++) {
            Eresource eresource = it.next();
            String normTitle = eresource.getTitle().replaceAll("\\W", "");
            if (normQuery.equalsIgnoreCase(normTitle)) {
                titleMatches.add(eresource);
                it.remove();
            }
        }
        i = 0;
        for (Eresource eresource : titleMatches) {
            result.add(i++, eresource);
        }
        return result;
    }

    private LinkedList<Eresource> parseResultSet(final ResultSet rs) throws SQLException {
        LinkedList<Eresource> eresources = new LinkedList<Eresource>();
        Eresource eresource = null;
        Version version = null;
        Link link;
        int currentEresourceId = -1;
        int currentVersionId = -1;
        int currentLinkId = -1;
        while (rs.next()) {
            int rowEresourceId = rs.getInt("ERESOURCE_ID");
            int recordId = rs.getInt("RECORD_ID");
            String recordType = rs.getString("RECORD_TYPE");
            if (rowEresourceId != currentEresourceId) {
                eresource = new EresourceImpl();
                eresource.setId(rowEresourceId);
                eresource.setRecordId(recordId);
                eresource.setRecordType(recordType);
                String title = rs.getString("TITLE");
                eresource.setTitle(title);
                eresources.add(eresource);
                currentEresourceId = rowEresourceId;
            }
            int rowVersionId = rs.getInt("VERSION_ID");
            if (rowVersionId != currentVersionId) {
                version = new VersionImpl();
                eresource.addVersion(version);
                version.setPublisher(rs.getString("PUBLISHER"));
                version.setSummaryHoldings(rs.getString("HOLDINGS"));
                version.setDates(rs.getString("DATES"));
                version.setDescription(rs.getString("DESCRIPTION"));
                if ("F".equals(rs.getString("PROXY"))) {
                    version.setProxy(false);
                }
                currentVersionId = rowVersionId;
            }
            int rowLinkId = rs.getInt("LINK_ID");
            if (rowLinkId != currentLinkId) {
                link = new LinkImpl();
                version.addLink(link);
                link.setUrl(rs.getString("URL"));
                link.setLabel(rs.getString("LABEL"));
                link.setInstruction(rs.getString("INSTRUCTION"));
                currentLinkId = rowLinkId;
            }
        }
        return eresources;
    }
}

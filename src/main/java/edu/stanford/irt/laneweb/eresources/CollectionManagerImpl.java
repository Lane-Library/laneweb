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
import edu.stanford.irt.eresources.impl.EresourceImpl;
import edu.stanford.irt.eresources.impl.LinkImpl;
import edu.stanford.irt.eresources.impl.QueryTranslator;
import edu.stanford.irt.eresources.impl.VersionImpl;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class CollectionManagerImpl implements CollectionManager {

    private static final String BROWSE =
            "SELECT ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM ERESOURCE, VERSION, LINK, TYPE, DESCRIPTION "
                    + "WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND TYPE.TYPE = ? "
                    + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String BROWSE_ALPHA =
            "SELECT ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM ERESOURCE, VERSION, LINK, TYPE, DESCRIPTION "
                    + "WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND TYPE.TYPE = ? "
                    + "AND NLSSORT(SUBSTR(TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') = NLSSORT(?,'NLS_SORT=GENERIC_BASELETTER') "
                    + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String BROWSE_NONALPHA =
            "SELECT ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM ERESOURCE, VERSION, LINK, TYPE, DESCRIPTION "
                    + "WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND TYPE.TYPE = ? "
                    + "AND (NLSSORT(SUBSTR(TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') < NLSSORT('A','NLS_SORT=GENERIC_BASELETTER') "
                    + "OR NLSSORT(SUBSTR(TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') > NLSSORT('z','NLS_SORT=GENERIC_BASELETTER')) "
                    + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String CORE =
            "SELECT ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM ERESOURCE, VERSION, LINK, TYPE, DESCRIPTION "
                    + "WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND ERESOURCE.CORE = 'Y' "
                    + "AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND TYPE.TYPE = ? "
                    + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String COUNT =
            "WITH FOUND AS (SELECT ERESOURCE.ERESOURCE_ID, TYPE.TYPE, SUBSET.SUBSET FROM ERESOURCE, TYPE, SUBSET "
                    + "WHERE CONTAINS(ERESOURCE.TEXT,?) > 0 "
                    + "AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = SUBSET.ERESOURCE_ID(+)) "
                    + "SELECT 'all' AS GENRE, COUNT(DISTINCT ERESOURCE_ID) AS HITS FROM FOUND";

    private static final String COUNT_SUBSET_UNION =
            " UNION SELECT ? AS GENRE, COUNT(DISTINCT ERESOURCE_ID) AS HITS FROM FOUND WHERE SUBSET = ?";

    private static final String COUNT_TYPE_UNION =
            " UNION SELECT ? AS GENRE, COUNT(DISTINCT ERESOURCE_ID) AS HITS FROM FOUND WHERE TYPE = ?";

    private static final String MESH =
            "SELECT ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM ERESOURCE, VERSION, LINK, MESH, TYPE, DESCRIPTION "
                    + "WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND ERESOURCE.ERESOURCE_ID = MESH.ERESOURCE_ID "
                    + "AND MESH.TERM = ? "
                    + "AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
                    + "AND TYPE.TYPE = ? "
                    + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String MESH_CORE =
            "SELECT ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM ERESOURCE, VERSION, LINK, MESH, TYPE, DESCRIPTION "
                    + "WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND ERESOURCE.ERESOURCE_ID = MESH.ERESOURCE_ID "
                    + "AND MESH.TERM = ? "
                    + "AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
                    + "AND TYPE.TYPE = ? "
                    + "AND ERESOURCE.CORE = 'Y' "
                    + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH =
            "WITH FOUND AS ( "
                    + "SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, SCORE(1) * 3 AS SCORE_TEXT, "
                    + "CONTAINS(TITLE,?) * 3 AS SCORE_TITLE "
                    + "FROM ERESOURCE "
                    + "WHERE CONTAINS(TEXT,?,1) > 0 "
                    + "AND CORE = 'Y' "
                    + "UNION "
                    + "SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
                    + "CONTAINS(TITLE,?) AS SCORE_TITLE "
                    + "FROM ERESOURCE "
                    + "WHERE CONTAINS(TEXT,?,1) > 0 "
                    + "AND CORE IS NULL "
                    + ") "
                    + "SELECT FOUND.ERESOURCE_ID, FOUND.RECORD_TYPE, FOUND.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES,   VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM FOUND, VERSION, LINK, TYPE, SUBSET, DESCRIPTION "
                    + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND FOUND.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
                    + "AND VERSION.VERSION_ID = SUBSET.VERSION_ID(+) "
                    + "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH_SUBSET =
            "WITH FOUND AS ( "
                    + "SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, SCORE(1) * 3 AS SCORE_TEXT, "
                    + "CONTAINS(TITLE,?) * 3 AS SCORE_TITLE "
                    + "FROM ERESOURCE "
                    + "WHERE CONTAINS(TEXT,?,1) > 0 "
                    + "AND CORE = 'Y' "
                    + "UNION "
                    + "SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
                    + "CONTAINS(TITLE,?) AS SCORE_TITLE "
                    + "FROM ERESOURCE "
                    + "WHERE CONTAINS(TEXT,?,1) > 0 "
                    + "AND CORE IS NULL "
                    + ") "
                    + "SELECT FOUND.ERESOURCE_ID, FOUND.RECORD_TYPE, FOUND.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES,   VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM FOUND, VERSION, LINK, TYPE, SUBSET, DESCRIPTION "
                    + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND FOUND.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
                    + "AND VERSION.VERSION_ID = SUBSET.VERSION_ID(+) "
                    + "AND SUBSET = ? "
                    + "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH_TYPE =
            "WITH FOUND AS ( "
                    + "SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, SCORE(1) * 3 AS SCORE_TEXT, "
                    + "CONTAINS(TITLE,?) * 3 AS SCORE_TITLE "
                    + "FROM ERESOURCE "
                    + "WHERE CONTAINS(TEXT,?,1) > 0 "
                    + "AND CORE = 'Y' "
                    + "UNION "
                    + "SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
                    + "CONTAINS(TITLE,?) AS SCORE_TITLE "
                    + "FROM ERESOURCE "
                    + "WHERE CONTAINS(TEXT,?,1) > 0 "
                    + "AND CORE IS NULL "
                    + ") "
                    + "SELECT FOUND.ERESOURCE_ID, FOUND.RECORD_TYPE, FOUND.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES,   VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM FOUND, VERSION, LINK, TYPE, SUBSET, DESCRIPTION "
                    + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND FOUND.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
                    + "AND VERSION.VERSION_ID = SUBSET.VERSION_ID(+) "
                    + "AND TYPE = ? "
                    + "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SUBSET =
            "SELECT ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_TYPE, ERESOURCE.RECORD_ID, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM ERESOURCE, VERSION, LINK, SUBSET, DESCRIPTION "
                    + "WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND VERSION.VERSION_ID = SUBSET.VERSION_ID "
                    + "AND ERESOURCE.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND SUBSET.SUBSET = ? "
                    + "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    protected DataSource dataSource;

    public Collection<Eresource> getCore(final String type) {
        Collection<String> params = new LinkedList<String>();
        params.add(type);
        return doGet(CORE, params, false);
    }

    public Collection<Eresource> getMesh(final String type, final String mesh) {
        Collection<String> params = new LinkedList<String>();
        params.add(mesh);
        params.add(type);
        return doGet(MESH, params, false);
    }

    public Collection<Eresource> getMeshCore(final String type, final String mesh) {
        Collection<String> params = new LinkedList<String>();
        params.add(mesh);
        params.add(type);
        return doGet(MESH_CORE, params, false);
    }

    public Collection<Eresource> getSubset(final String subset) {
        Collection<String> params = new LinkedList<String>();
        params.add(subset);
        return doGet(SUBSET, params, false);
    }

    public Collection<Eresource> getType(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        Collection<String> params = new LinkedList<String>();
        params.add(type);
        return doGet(BROWSE, params, false);
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
        } else {
            sql = BROWSE_ALPHA;
            String alphaString = new String(new char[] { alpha });
            params.add(type);
            params.add(alphaString);
        }
        return doGet(sql, params, false);
    }

    public Collection<Eresource> search(final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 4; i++) {
            params.add(translatedQuery);
        }
        return doGetSearch(SEARCH, params, query);
    }

    public Map<String, Integer> searchCount(final Set<String> types, final Set<String> subsets, final String query) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        StringBuilder sb = new StringBuilder(COUNT);
        for (int i = 0; i < types.size(); i++) {
            sb.append(COUNT_TYPE_UNION);
        }
        for (int i = 0; i < subsets.size(); i++) {
            sb.append(COUNT_SUBSET_UNION);
        }
        String sql = sb.toString();
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
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    public Collection<Eresource> searchSubset(final String subset, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 4; i++) {
            params.add(translatedQuery);
        }
        params.add(subset);
        return doGetSearch(SEARCH_SUBSET, params, query);
    }

    public Collection<Eresource> searchType(final String type, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 4; i++) {
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

    private LinkedList<Eresource> doGet(final String sql, final Collection<String> params, final boolean scores) {
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
            return parseResultSet(rs, scores);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    private Collection<Eresource> doGetSearch(final String sql, final Collection<String> params, final String query) {
        LinkedList<Eresource> result = doGet(sql, params, true);
        LinkedList<EresourceImpl> titleMatches = new LinkedList<EresourceImpl>();
        String normQuery = query.replaceAll("\\W", "");
        int i = 0;
        for (ListIterator<Eresource> it = result.listIterator(); it.hasNext() && (i < 20); i++) {
            Eresource eresource = it.next();
            String normTitle = eresource.getTitle().replaceAll("\\W", "");
            if (normQuery.equalsIgnoreCase(normTitle)) {
                titleMatches.add((EresourceImpl) eresource);
                it.remove();
            }
        }
        i = 0;
        for (EresourceImpl eresource : titleMatches) {
            eresource.setScore(eresource.getScore() * 10);
            result.add(i++, eresource);
        }
        return result;
    }

    private LinkedList<Eresource> parseResultSet(final ResultSet rs, final boolean scored) throws SQLException {
        LinkedList<Eresource> eresources = new LinkedList<Eresource>();
        EresourceImpl eresource = null;
        VersionImpl version = null;
        LinkImpl link;
        int currentEresourceId = -1;
        int currentVersionId = -1;
        int currentLinkId = -1;
        String currentTitle = null;
        while (rs.next()) {
            int rowEresourceId = rs.getInt("ERESOURCE_ID");
            int recordId = rs.getInt("RECORD_ID");
            String recordType = rs.getString("RECORD_TYPE");
            String rowTitle = rs.getString("TITLE");
            if ((rowEresourceId != currentEresourceId) || !rowTitle.equals(currentTitle)) {
                currentTitle = rowTitle;
                eresource = new EresourceImpl();
                eresource.setId(rowEresourceId);
                eresource.setRecordId(recordId);
                eresource.setRecordType(recordType);
                eresource.setTitle(currentTitle);
                if (scored) {
                    eresource.setScore((rs.getInt("SCORE_TITLE") + rs.getInt("SCORE_TEXT")) / 2);
                }
                eresource.setDescription(rs.getString("E_DESCRIPTION"));
                eresources.add(eresource);
                currentEresourceId = rowEresourceId;
                currentVersionId = -1;
                currentLinkId = -1;
            }
            int rowVersionId = rs.getInt("VERSION_ID");
            if (rowVersionId != currentVersionId) {
                version = new VersionImpl();
                eresource.addVersion(version);
                version.setPublisher(rs.getString("PUBLISHER"));
                version.setSummaryHoldings(rs.getString("HOLDINGS"));
                version.setDates(rs.getString("DATES"));
                version.setDescription(rs.getString("V_DESCRIPTION"));
                currentVersionId = rowVersionId;
                currentLinkId = -1;
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

package edu.stanford.irt.laneweb.eresources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
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
    
    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

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
                    + "SELECT TITLE, ERESOURCE_ID, RECORD_TYPE, RECORD_ID, CORE, SCORE(1) AS SCORE_TEXT, CONTAINS(TITLE,'{cardiovascular} & {research}') AS SCORE_TITLE "
                    + "FROM ERESOURCE "
                    + "WHERE CONTAINS(TEXT,'{cardiovascular} & {research}',1) > 0 "
                    + ") "
                    + "SELECT FOUND.ERESOURCE_ID, RECORD_TYPE, RECORD_ID, CORE, YEAR, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "SCORE_TITLE, SCORE_TEXT "
                    + "FROM FOUND, VERSION, LINK, DESCRIPTION, PUBLICATION_YEAR "
                    + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND FOUND.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND FOUND.ERESOURCE_ID = PUBLICATION_YEAR.ERESOURCE_ID(+) "
                    + "ORDER BY FOUND.ERESOURCE_ID, VERSION_ID, LINK_ID";

//    private static final String SEARCH =
//            "WITH FOUND AS ( "
//                    + "SELECT TITLE, ERESOURCE_ID, RECORD_TYPE, RECORD_ID, CORE, SCORE(1) AS SCORE_TEXT, CONTAINS(TITLE,?) AS SCORE_TITLE "
//                    + "FROM ERESOURCE "
//                    + "WHERE CONTAINS(TEXT,?,1) > 0"
//                    + ") "
//                    + "SELECT FOUND.ERESOURCE_ID, RECORD_TYPE, RECORD_ID, CORE, YEAR, VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER, "
//                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
//                    + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
//                    + "FROM FOUND, VERSION, LINK, TYPE, SUBSET, DESCRIPTION, PUBLICATION_YEAR "
//                    + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID "
//                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
//                    + "AND FOUND.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
//                    + "AND FOUND.ERESOURCE_ID = PUBLICATION_YEAR.ERESOURCE_ID(+) "
//                    + "AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
//                    + "AND VERSION.VERSION_ID = SUBSET.VERSION_ID(+) "
//                    + "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH_SUBSET =
            "WITH FOUND AS ( "
                    + "SELECT TITLE, ERESOURCE_ID, RECORD_TYPE, RECORD_ID, CORE, SCORE(1) AS SCORE_TEXT, CONTAINS(TITLE,?) AS SCORE_TITLE "
                    + "FROM ERESOURCE "
                    + "WHERE CONTAINS(TEXT,?,1) > 0"
                    + ") "
                    + "SELECT FOUND.ERESOURCE_ID, RECORD_TYPE, RECORD_ID, CORE, YEAR, VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES, VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM FOUND, VERSION, LINK, TYPE, SUBSET, DESCRIPTION, PUBLICATION_YEAR "
                    + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND FOUND.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND FOUND.ERESOURCE_ID = PUBLICATION_YEAR.ERESOURCE_ID(+) "
                    + "AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
                    + "AND VERSION.VERSION_ID = SUBSET.VERSION_ID(+) "
                    + "AND SUBSET = ? "
                    + "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH_TYPE =
            "WITH FOUND AS ( "
                    + "SELECT TITLE, ERESOURCE_ID, RECORD_TYPE, RECORD_ID, CORE, SCORE(1) AS SCORE_TEXT, CONTAINS(TITLE,?) AS SCORE_TITLE "
                    + "FROM ERESOURCE "
                    + "WHERE CONTAINS(TEXT,?,1) > 0"
                    + ") "
                    + "SELECT FOUND.ERESOURCE_ID, RECORD_TYPE, RECORD_ID, CORE, YEAR, VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER, "
                    + "HOLDINGS, DATES,   VERSION.DESCRIPTION AS V_DESCRIPTION, DESCRIPTION.DESCRIPTION AS E_DESCRIPTION, LABEL, URL, INSTRUCTION, "
                    + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
                    + "FROM FOUND, VERSION, LINK, TYPE, SUBSET, DESCRIPTION, PUBLICATION_YEAR "
                    + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID "
                    + "AND VERSION.VERSION_ID = LINK.VERSION_ID "
                    + "AND FOUND.ERESOURCE_ID = DESCRIPTION.ERESOURCE_ID(+) "
                    + "AND FOUND.ERESOURCE_ID = PUBLICATION_YEAR.ERESOURCE_ID(+) "
                    + "AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
                    + "AND VERSION.VERSION_ID = SUBSET.VERSION_ID(+) "
                    + "AND TYPE = ? "
                    + "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SUBSET =
            "SELECT ERESOURCE.ERESOURCE_ID, RECORD_TYPE, RECORD_ID, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER, "
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
        return doGet(CORE, params, null);
    }

    public Collection<Eresource> getMesh(final String type, final String mesh) {
        Collection<String> params = new LinkedList<String>();
        params.add(mesh);
        params.add(type);
        return doGet(MESH, params, null);
    }

    public Collection<Eresource> getMeshCore(final String type, final String mesh) {
        Collection<String> params = new LinkedList<String>();
        params.add(mesh);
        params.add(type);
        return doGet(MESH_CORE, params, null);
    }

    public Collection<Eresource> getSubset(final String subset) {
        Collection<String> params = new LinkedList<String>();
        params.add(subset);
        return doGet(SUBSET, params, null);
    }

    public Collection<Eresource> getType(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        Collection<String> params = new LinkedList<String>();
        params.add(type);
        return doGet(BROWSE, params, null);
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
        return doGet(sql, params, null);
    }

    public Collection<Eresource> search(final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        params.add(translatedQuery);
        params.add(translatedQuery);
        return doGet(SEARCH, params, query);
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
        params.add(translatedQuery);
        params.add(translatedQuery);
        params.add(subset);
        return doGet(SEARCH_SUBSET, params, query);
    }

    public Collection<Eresource> searchType(final String type, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        params.add(translatedQuery);
        params.add(translatedQuery);
        params.add(type);
        return doGet(SEARCH_TYPE, params, query);
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

    private LinkedList<Eresource> doGet(final String sql, final Collection<String> params, final String query) {
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
            return parseResultSet(rs, query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    private LinkedList<Eresource> parseResultSet(final ResultSet rs, final String query) throws SQLException {
        LinkedList<Eresource> eresources = new LinkedList<Eresource>();
        EresourceImpl eresource = null;
        VersionImpl version = null;
        LinkImpl link;
        int currentEresourceId = -1;
        int currentVersionId = -1;
        int currentLinkId = -1;
        while (rs.next()) {
            int rowEresourceId = rs.getInt("ERESOURCE_ID");
            if (rowEresourceId != currentEresourceId) {
                eresource = new EresourceImpl();
                eresource.setId(rowEresourceId);
                eresource.setRecordId(rs.getInt("RECORD_ID"));
                eresource.setRecordType(rs.getString("RECORD_TYPE"));
                eresource.setTitle(rs.getString("TITLE"));
                if (query != null) {
                    if (query.equalsIgnoreCase(eresource.getTitle())) {
                        eresource.setScore(Integer.MAX_VALUE);
                    } else {
                        //core material weighted * 3
                        int coreFactor = "Y".equals(rs.getString("CORE")) ? 3 : 1;
                        //weighted oracle text scores for title and text averaged
                        int scoreFactor = ((rs.getInt("SCORE_TITLE") * coreFactor) + (rs.getInt("SCORE_TEXT") * coreFactor)) / 2;
                        int year = rs.getInt("YEAR");
                        //yearFactor can change score from -10 to 10 points
                        int yearFactor = year == 0 ? 0 : Math.max(-10, 10 - (THIS_YEAR - year));
                        eresource.setScore(scoreFactor + yearFactor);
                    }
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

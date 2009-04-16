package edu.stanford.irt.laneweb.eresources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.eresources.impl.QueryTranslator;
import edu.stanford.irt.eresources.impl.VersionImpl;
import edu.stanford.irt.laneweb.JdbcUtils;

public class BassettCollectionManager {

    private static final String GET_BASSETT_BY_REGION = "SELECT ERESOURCE.ERESOURCE_ID, VERSION.VERSION_ID, LINK_ID, TYPE,  TITLE, PUBLISHER, HOLDINGS, "
            + "DATES, VERSION.DESCRIPTION AS VERSION_DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, B.BASSETT_ID, B.BASSETT_NUMBER, B.IMAGE, B.DIAGRAM, B.LATIN_LEGEND, B.ENGLISH_LEGEND, B.DESCRIPTION AS BASSETT_DESCRIPTION, BR.REGION, BR.SUB_REGION "
            + "FROM ERESOURCE, VERSION, LINK, TYPE,  BASSETT B, BASSETT_REGION BR WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID   AND ERESOURCE.RECORD_ID = B.RECORD_ID "
            + "AND B.BASSETT_ID = BR.BASSETT_ID AND VERSION.VERSION_ID = LINK.VERSION_ID AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
            + "AND TYPE = 'bassett'  AND BR.REGION = ? ORDER BY B.BASSETT_NUMBER ";

    private static final String GET_BASSETT_BY_SUB_REGION = "SELECT ERESOURCE.ERESOURCE_ID, VERSION.VERSION_ID, LINK_ID, TYPE,  TITLE, PUBLISHER, HOLDINGS, "
            + "DATES, VERSION.DESCRIPTION AS VERSION_DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, B.BASSETT_ID, B.BASSETT_NUMBER, B.IMAGE, B.DIAGRAM, B.LATIN_LEGEND, B.ENGLISH_LEGEND, B.DESCRIPTION AS BASSETT_DESCRIPTION, BR.REGION, BR.SUB_REGION "
            + "FROM ERESOURCE, VERSION, LINK, TYPE,  BASSETT B, BASSETT_REGION BR WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID   AND ERESOURCE.RECORD_ID = B.RECORD_ID "
            + "AND B.BASSETT_ID = BR.BASSETT_ID AND VERSION.VERSION_ID = LINK.VERSION_ID AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
            + "AND TYPE = 'bassett'  AND BR.REGION = ? AND BR.SUB_REGION = ?  ORDER BY B.BASSETT_NUMBER";

    private static final String SEARCH_BASSETT = "WITH FOUND AS ( SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
            + "CONTAINS(TITLE,?) AS SCORE_TITLE FROM ERESOURCE  WHERE CONTAINS(TEXT,?,1) > 0) "
            + "SELECT FOUND.ERESOURCE_ID, VERSION.VERSION_ID, LINK_ID, TYPE,  TITLE, PUBLISHER, HOLDINGS, "
            + "DATES, VERSION.DESCRIPTION AS VERSION_DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, B.BASSETT_ID, B.BASSETT_NUMBER, B.IMAGE, B.DIAGRAM, B.LATIN_LEGEND, B.ENGLISH_LEGEND, B.DESCRIPTION AS BASSETT_DESCRIPTION, BR.REGION, BR.SUB_REGION,"
            + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
            + "FROM FOUND, VERSION, LINK, TYPE, BASSETT B, BASSETT_REGION BR "
            + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID   AND FOUND.RECORD_ID = B.RECORD_ID "
            + "AND B.BASSETT_ID = BR.BASSETT_ID AND VERSION.VERSION_ID = LINK.VERSION_ID "
            + "AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
            + "AND TYPE = 'bassett'  ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH_BASSETT_BY_REGION = "WITH FOUND AS ( SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
            + "CONTAINS(TITLE,?) AS SCORE_TITLE FROM ERESOURCE  WHERE CONTAINS(TEXT,?,1) > 0) "
            + "SELECT FOUND.ERESOURCE_ID, VERSION.VERSION_ID, LINK_ID, TYPE, TITLE, PUBLISHER, HOLDINGS, "
            + "DATES, VERSION.DESCRIPTION AS VERSION_DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, B.BASSETT_ID, B.BASSETT_NUMBER, B.IMAGE, B.DIAGRAM, B.LATIN_LEGEND, BR.REGION, BR.SUB_REGION,"
            + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
            + "FROM FOUND, VERSION, LINK, TYPE,  BASSETT B, BASSETT_REGION BR WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID   AND FOUND.RECORD_ID = B.RECORD_ID "
            + "AND B.BASSETT_ID = BR.BASSETT_ID AND VERSION.VERSION_ID = LINK.VERSION_ID AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
            + "AND TYPE = 'bassett' AND BR.REGION = ? ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH_BASSETT_BY_SUB_REGION = "WITH FOUND AS ( SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
            + "CONTAINS(TITLE,?) AS SCORE_TITLE FROM ERESOURCE  WHERE CONTAINS(TEXT,?,1) > 0) "
            + "SELECT FOUND.ERESOURCE_ID, VERSION.VERSION_ID, LINK_ID, TYPE, TITLE, PUBLISHER, HOLDINGS, "
            + "DATES, VERSION.DESCRIPTION AS VERSION_DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, B.BASSETT_ID, B.BASSETT_NUMBER, B.IMAGE, B.DIAGRAM, B.LATIN_LEGEND, BR.REGION, BR.SUB_REGION,"
            + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE "
            + "FROM FOUND, VERSION, LINK, TYPE, BASSETT B, BASSETT_REGION BR "
            + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID   AND FOUND.RECORD_ID = B.RECORD_ID "
            + "AND B.BASSETT_ID = BR.BASSETT_ID AND VERSION.VERSION_ID = LINK.VERSION_ID "
            + "AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+) "
            + "AND TYPE = 'bassett'  AND BR.REGION = ? AND BR.SUB_REGION = ? ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SEARCH_BY_BASSETT_NUMBER = "SELECT ERESOURCE.ERESOURCE_ID, VERSION.VERSION_ID, LINK_ID, TYPE,  TITLE, PUBLISHER, HOLDINGS, "
            + "DATES, VERSION.DESCRIPTION AS VERSION_DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION, B.BASSETT_ID, B.BASSETT_NUMBER, B.IMAGE, B.DIAGRAM, B.LATIN_LEGEND, B.ENGLISH_LEGEND, B.DESCRIPTION AS BASSETT_DESCRIPTION, BR.REGION, BR.SUB_REGION "
            + "FROM ERESOURCE, VERSION, LINK, TYPE,  BASSETT B, BASSETT_REGION BR WHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID   AND ERESOURCE.RECORD_ID = B.RECORD_ID "
            + "AND B.BASSETT_ID = BR.BASSETT_ID AND VERSION.VERSION_ID = LINK.VERSION_ID AND ERESOURCE.ERESOURCE_ID = TYPE.ERESOURCE_ID "
            + "AND TYPE = 'bassett'  AND B.BASSETT_NUMBER = ?";

    private static final String SEARCH_COUNT = "WITH FOUND AS ( SELECT TITLE, ERESOURCE.ERESOURCE_ID, ERESOURCE.RECORD_ID, SCORE(1) AS SCORE_TEXT, "
            + "CONTAINS(TITLE,?) AS SCORE_TITLE FROM ERESOURCE  WHERE CONTAINS(TEXT,?,1) > 0) "
            + "SELECT  br.region, br.sub_region, count(b.bassett_number) as SUB_REGION_COUNT "
            + "FROM FOUND f,  BASSETT b , BASSETT_REGION br "
            + "where f.record_id = b.record_id  and br.bassett_id = b.bassett_id group by  br.region, br.sub_region "
            + "union all "
            + "SELECT  br.region, '0' as SUB_REGION, count(distinct(b.bassett_number)) as SUB_REGION_COUNT "
            + "FROM FOUND f,  BASSETT b , BASSETT_REGION br "
            + "where f.record_id = b.record_id  and br.bassett_id = b.bassett_id group by  br.region "
            + "order by 1,2";

    private DataSource dataSource;

    public Collection<Eresource> getById(final String bassettNumber) {
        Collection<String> params = new LinkedList<String>();
        params.add(bassettNumber);
        return doGet(SEARCH_BY_BASSETT_NUMBER, params, true);
    }

    public Collection<Eresource> getCore(final String type) {
        throw new UnsupportedOperationException();
    }

    public Collection<Eresource> getMesh(final String type, final String mesh) {
        throw new UnsupportedOperationException();
    }

    public Collection<Eresource> getMeshCore(final String type, final String mesh) {
        throw new UnsupportedOperationException();
    }

    public Collection<Eresource> getSubset(final String region) {
        Collection<String> params = new LinkedList<String>();
        if (region.indexOf("--") > -1) {
            String[] splittedRegion = region.split("--");
            params.add(splittedRegion[0]);
            params.add(splittedRegion[1]);
            return doGet(GET_BASSETT_BY_SUB_REGION, params);
        } else {
            params.add(region);
            return doGet(GET_BASSETT_BY_REGION, params);
        }
    }

    public Collection<Eresource> getType(final String type) {
        throw new UnsupportedOperationException();
    }

    public Collection<Eresource> getType(final String type, final char alpha) {
        throw new UnsupportedOperationException();
    }

    public Collection<Eresource> search(final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 2; i++) {
            params.add(translatedQuery);
        }
        return doGetSearch(SEARCH_BASSETT, params, query);
    }

    public Map<String, Integer> searchCount(final Set<String> types, final Set<String> subsets, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, Integer> result = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(SEARCH_COUNT);
            stmt.setString(1, translatedQuery);
            stmt.setString(2, translatedQuery);
            rs = stmt.executeQuery();
            result = parseCountResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
        return result;
    }

    public Collection<Eresource> searchSubset(final String region, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 2; i++) {
            params.add(translatedQuery);
        }
        if (region.contains("--")) {
            String[] splittedRegion = region.split("--");
            params.add(splittedRegion[0]);
            params.add(splittedRegion[1]);
            return doGetSearch(SEARCH_BASSETT_BY_SUB_REGION, params, query);
        } else {
            params.add(region);
            return doGetSearch(SEARCH_BASSETT_BY_REGION, params, query);
        }
    }

    public Collection<Eresource> searchType(final String type, final String query) {
        throw new UnsupportedOperationException();
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

    private LinkedList<Eresource> doGet(final String sql, final Collection<String> params) {
        return doGet(sql, params, false);
    }

    private LinkedList<Eresource> doGet(final String sql, final Collection<String> params, final boolean withLegend) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        LinkedList<Eresource> result = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            for (String param : params) {
                stmt.setString(index++, param);
            }
            rs = stmt.executeQuery();
            result = parseResultSet(rs, withLegend);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
        return result;
    }

    private Collection<Eresource> doGetSearch(final String sql, final Collection<String> params, final String query) {
        LinkedList<Eresource> result = doGet(sql, params);
        LinkedList<Eresource> titleMatches = new LinkedList<Eresource>();
        int i = 0;
        for (ListIterator<Eresource> it = result.listIterator(); it.hasNext() && (i < 20); i++) {
            Eresource eresource = it.next();
            if (query.equalsIgnoreCase(eresource.getTitle())) {
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

    private Map<String, Integer> parseCountResultSet(final ResultSet rs) throws SQLException {
        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        while (rs.next()) {
            int count = rs.getInt("SUB_REGION_COUNT");
            String region = rs.getString("REGION");
            String subRegion = rs.getString("SUB_REGION");
            if ("0".equals(subRegion)) {
                result.put(region, count);
            } else if (subRegion != null) {
                result.put(region.concat("--".concat(subRegion)), count);
            }
        }
        return result;
    }

    private LinkedList<Eresource> parseResultSet(final ResultSet rs, final boolean fullResult) throws SQLException {
        LinkedList<Eresource> eresources = new LinkedList<Eresource>();
        BassettEresource eresource = null;
        Version version = null;
        int currentEresourceId = -1;
        String currentTitle = null;
        while (rs.next()) {
            int rowEresourceId = rs.getInt("ERESOURCE_ID");
            String rowTitle = rs.getString("TITLE");
            if (rowEresourceId != currentEresourceId) {
                currentTitle = rowTitle;
                eresource = new BassettEresource();
                eresource.setId(rowEresourceId);
                eresource.setTitle(currentTitle);
                eresource.setImage(rs.getString("IMAGE"));
                eresource.setLatinLegend(rs.getString("LATIN_LEGEND"));
                eresource.setBassettNumber(rs.getString("BASSETT_NUMBER"));
                eresource.setDiagram(rs.getString("DIAGRAM"));
                if (fullResult) {
                    eresource.setEngishLegend(rs.getString("ENGLISH_LEGEND"));
                    eresource.setDescription(rs.getString("BASSETT_DESCRIPTION"));
                }
                currentEresourceId = rowEresourceId;
                version = new VersionImpl();
                eresource.addVersion(version);
                version.setPublisher(rs.getString("PUBLISHER"));
                version.setSummaryHoldings(rs.getString("HOLDINGS"));
                version.setDates(rs.getString("DATES"));
                version.setDescription(rs.getString("VERSION_DESCRIPTION"));
                if ("F".equals(rs.getString("PROXY"))) {
                    version.setProxy(false);
                }
                eresources.add(eresource);
            }
            if (rs.getString("SUB_REGION") != null) {
                eresource.addRegion(rs.getString("REGION").concat("--").concat(rs.getString("SUB_REGION")));
            } else {
                eresource.addRegion(rs.getString("REGION"));
            }
        }
        return eresources;
    }
}
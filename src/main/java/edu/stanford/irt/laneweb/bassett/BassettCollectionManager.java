package edu.stanford.irt.laneweb.bassett;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.eresources.impl.QueryTranslator;
import edu.stanford.irt.eresources.impl.VersionImpl;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class BassettCollectionManager {

    private static final String GET_BASSETT_BY_REGION = "browse.region";

    private static final String GET_BASSETT_BY_SUB_REGION = "browse.subregion";

    private static final String SEARCH_BASSETT = "search";

    private static final String SEARCH_BASSETT_BY_REGION = "search.region";

    private static final String SEARCH_BASSETT_BY_SUB_REGION = "search.subregion";

    private static final String SEARCH_BY_BASSETT_NUMBER = "search.number";

    private static final String SEARCH_COUNT = "search.count";

    private DataSource dataSource;
    
    private Properties sqlStatements;

    public BassettCollectionManager(final DataSource dataSource, final Properties sqlStatements) {
        this.dataSource = dataSource;
        this.sqlStatements = sqlStatements;
    }

    public Collection<BassettEresource> getById(final String bassettNumber) {
        Collection<String> params = new LinkedList<String>();
        params.add(bassettNumber);
        return doGet(SEARCH_BY_BASSETT_NUMBER, params, true);
    }

    public Collection<BassettEresource> getRegion(final String region) {
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

    public Collection<BassettEresource> search(final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 2; i++) {
            params.add(translatedQuery);
        }
        return doGetSearch(SEARCH_BASSETT, params, query);
    }

    public Map<String, Integer> searchCount(final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, Integer> result = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(this.sqlStatements.getProperty(SEARCH_COUNT));
            stmt.setString(1, translatedQuery);
            stmt.setString(2, translatedQuery);
            rs = stmt.executeQuery();
            result = parseCountResultSet(rs);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
        return result;
    }

    public Collection<BassettEresource> searchRegion(final String region, final String query) {
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

    private List<BassettEresource> doGet(final String sqlKey, final Collection<String> params) {
        return doGet(sqlKey, params, false);
    }

    private List<BassettEresource> doGet(final String sqlKey, final Collection<String> params, final boolean withLegend) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<BassettEresource> result = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(this.sqlStatements.getProperty(sqlKey));
            int index = 1;
            for (String param : params) {
                stmt.setString(index++, param);
            }
            rs = stmt.executeQuery();
            result = parseResultSet(rs, withLegend);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
        return result;
    }

    private Collection<BassettEresource> doGetSearch(final String sqlKey, final Collection<String> params, final String query) {
        List<BassettEresource> result = doGet(sqlKey, params);
        List<BassettEresource> titleMatches = new LinkedList<BassettEresource>();
        int i = 0;
        for (ListIterator<BassettEresource> it = result.listIterator(); it.hasNext() && (i < 20); i++) {
            BassettEresource eresource = it.next();
            if (query.equalsIgnoreCase(eresource.getTitle())) {
                titleMatches.add(eresource);
                it.remove();
            }
        }
        i = 0;
        for (BassettEresource eresource : titleMatches) {
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

    private List<BassettEresource> parseResultSet(final ResultSet rs, final boolean fullResult) throws SQLException {
        List<BassettEresource> eresources = new LinkedList<BassettEresource>();
        BassettEresource eresource = null;
        VersionImpl version = null;
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
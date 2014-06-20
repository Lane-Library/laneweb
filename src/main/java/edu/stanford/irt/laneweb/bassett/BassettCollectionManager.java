package edu.stanford.irt.laneweb.bassett;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.eresources.QueryTranslator;
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

    public List<BassettImage> getById(final String bassettNumber) {
        List<String> params = new LinkedList<String>();
        params.add(bassettNumber);
        return doGet(SEARCH_BY_BASSETT_NUMBER, params, true);
    }

    public List<BassettImage> getRegion(final String region) {
        List<String> params = new LinkedList<String>();
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

    public List<BassettImage> search(final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        List<String> params = new LinkedList<String>();
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

    public List<BassettImage> searchRegion(final String region, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        List<String> params = new LinkedList<String>();
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

    private List<BassettImage> doGet(final String sqlKey, final List<String> params) {
        return doGet(sqlKey, params, false);
    }

    private List<BassettImage> doGet(final String sqlKey, final List<String> params, final boolean withLegend) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<BassettImage> result = null;
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

    private List<BassettImage> doGetSearch(final String sqlKey, final List<String> params,
            final String query) {
        List<BassettImage> result = doGet(sqlKey, params);
        List<BassettImage> titleMatches = new LinkedList<BassettImage>();
        int i = 0;
        for (ListIterator<BassettImage> it = result.listIterator(); it.hasNext() && (i < 20); i++) {
            BassettImage image = it.next();
            if (query.equalsIgnoreCase(image.getTitle())) {
                titleMatches.add(image);
                it.remove();
            }
        }
        i = 0;
        for (BassettImage image : titleMatches) {
            result.add(i++, image);
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

    private List<BassettImage> parseResultSet(final ResultSet rs, final boolean fullResult) throws SQLException {
        List<BassettImage> images = new LinkedList<BassettImage>();
        BassettImage image = null;
        int currentImageId = -1;
        String currentTitle = null;
        while (rs.next()) {
            int rowImageId = rs.getInt("ERESOURCE_ID");
            String rowTitle = rs.getString("TITLE");
            if (rowImageId != currentImageId) {
                currentTitle = rowTitle;
                String description = fullResult ? rs.getString("BASSETT_DESCRIPTION") : null;
                image = new BassettImage(description, currentTitle);
                image.setImage(rs.getString("IMAGE"));
                image.setLatinLegend(rs.getString("LATIN_LEGEND"));
                image.setBassettNumber(rs.getString("BASSETT_NUMBER"));
                image.setDiagram(rs.getString("DIAGRAM"));
                if (fullResult) {
                    image.setEngishLegend(rs.getString("ENGLISH_LEGEND"));
                }
                currentImageId = rowImageId;
                images.add(image);
            }
            String subregion = rs.getString("SUB_REGION");
            if (subregion != null) {
                image.addRegion(rs.getString("REGION").concat("--").concat(subregion));
            } else {
                image.addRegion(rs.getString("REGION"));
            }
        }
        return images;
    }
}
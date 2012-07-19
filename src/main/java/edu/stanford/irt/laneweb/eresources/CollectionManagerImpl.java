package edu.stanford.irt.laneweb.eresources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.impl.EresourceImpl;
import edu.stanford.irt.eresources.impl.LinkImpl;
import edu.stanford.irt.eresources.impl.QueryTranslator;
import edu.stanford.irt.eresources.impl.VersionImpl;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class CollectionManagerImpl implements CollectionManager {
    
    private static final String BROWSE = "eresources.browse";

    private static final String BROWSE_ALPHA = "eresources.browse.alpha";

    private static final String BROWSE_NONALPHA = "eresources.browse.nonalpha";

    private static final String BROWSE_CORE = "eresources.browse.core";

    private static final String COUNT = "eresources.search.count.0";

    private static final String COUNT_TYPE_UNION = "eresources.search.count.1";

    private static final String BROWSE_MESH ="eresources.browse.mesh";

    private static final String SEARCH = "eresources.search";

    private static final String SEARCH_TYPE = "eresources.search.type";

    private static final String BROWSE_SUBSET = "eresources.browse.subset";

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    
    private Properties sqlStatements;

    protected DataSource dataSource;
    
    public CollectionManagerImpl(final DataSource dataSource, final Properties sqlStatements) {
        this.dataSource = dataSource;
        this.sqlStatements = sqlStatements;
    }

    public Collection<Eresource> getCore(final String type) {
        Collection<String> params = new LinkedList<String>();
        params.add(type);
        return doGet(BROWSE_CORE, params, null);
    }

    public Collection<Eresource> getMesh(final String type, final String mesh) {
        Collection<String> params = new LinkedList<String>();
        params.add(mesh);
        params.add(type);
        return doGet(BROWSE_MESH, params, null);
    }

    public Collection<Eresource> getMeshCore(final String type, final String mesh) {
        throw new UnsupportedOperationException();
    }

    public Collection<Eresource> getSubset(final String subset) {
        Collection<String> params = new LinkedList<String>();
        params.add(subset);
        return doGet(BROWSE_SUBSET, params, null);
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

    // TODO: remove these when upgrading to 1.8
    public Map<String, Integer> searchCount(final Set<String> types, final Set<String> subsets, final String query) {
        return searchCount(types, query);
    }

    public Map<String, Integer> searchCount(final Set<String> types, final String query) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        StringBuilder sb = new StringBuilder(this.sqlStatements.getProperty(COUNT));
        String countTypeUnion = this.sqlStatements.getProperty(COUNT_TYPE_UNION);
        for (int i = 0; i < types.size(); i++) {
            sb.append(countTypeUnion);
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
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString(1), Integer.valueOf(rs.getInt(2)));
            }
            return result;
        } catch (SQLException e) {
            throw new LanewebException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    public Collection<Eresource> searchSubset(final String subset, final String query) {
        throw new UnsupportedOperationException();
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

    private List<Eresource> doGet(final String sqlKey, final Collection<String> params, final String query) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(this.sqlStatements.getProperty(sqlKey));
            int index = 1;
            for (String param : params) {
                stmt.setString(index++, param);
            }
            rs = stmt.executeQuery();
            return parseResultSet(rs, query);
        } catch (SQLException e) {
            throw new LanewebException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    private List<Eresource> parseResultSet(final ResultSet rs, final String query) throws SQLException {
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
                if (query != null) {
                    if (query.equalsIgnoreCase(currentTitle)) {
                        eresource.setScore(Integer.MAX_VALUE);
                    } else {
                        // core material weighted * 3
                        int coreFactor = "Y".equals(rs.getString("BROWSE_CORE")) ? 3 : 1;
                        // weighted oracle text scores for title and text
                        // averaged
                        int scoreFactor = ((rs.getInt("SCORE_TITLE") * coreFactor) + (rs.getInt("SCORE_TEXT") * coreFactor)) / 2;
                        int year = rs.getInt("YEAR");
                        // subtract number of years difference from current year
                        // yearFactor can change score from -10 to 10 points
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

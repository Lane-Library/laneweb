package edu.stanford.irt.laneweb.eresources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public abstract class AbstractCollectionManager implements CollectionManager {

    protected static final String SEARCH = "search";

    protected static final String SEARCH_TYPE = "search.type";

    private static final String BROWSE = "browse";

    private static final String BROWSE_ALPHA = "browse.alpha";

    private static final String BROWSE_CORE = "browse.core";

    private static final String BROWSE_MESH = "browse.mesh";

    private static final String BROWSE_NONALPHA = "browse.nonalpha";

    private static final String BROWSE_SUBSET = "browse.subset";

    private static final String COUNT = "search.count.0";

    private static final String COUNT_TYPE_UNION = "search.count.1";

    private static final int MAX_QUERY_LENGTH = 200;

    private DataSource dataSource;

    private Properties sqlStatements;

    public AbstractCollectionManager(final DataSource dataSource, final Properties sqlStatements) {
        this.dataSource = dataSource;
        this.sqlStatements = sqlStatements;
    }

    @Override
    public List<Eresource> getCore(final String type) {
        Collection<String> params = new LinkedList<String>();
        params.add(type);
        return doGet(BROWSE_CORE, params, null);
    }

    @Override
    public List<Eresource> getMesh(final String type, final String mesh) {
        Collection<String> params = new LinkedList<String>();
        params.add(mesh);
        params.add(type);
        return doGet(BROWSE_MESH, params, null);
    }

    @Override
    public List<Eresource> getSubset(final String subset) {
        Collection<String> params = new LinkedList<String>();
        params.add(subset);
        return doGet(BROWSE_SUBSET, params, null);
    }

    @Override
    public List<Eresource> getType(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        Collection<String> params = new LinkedList<String>();
        params.add(type);
        return doGet(BROWSE, params, null);
    }

    @Override
    public List<Eresource> getType(final String type, final char alpha) {
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

    @Override
    public List<Eresource> search(final String query) {
        if (query == null || query.isEmpty() || query.length() >= MAX_QUERY_LENGTH) {
            return Collections.emptyList();
        }
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        params.add(translatedQuery);
        params.add(translatedQuery);
        return doGet(SEARCH, params, query);
    }

    @Override
    public Map<String, Integer> searchCount(final Set<String> types, final String query) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        if (query.length() <= MAX_QUERY_LENGTH) {
            StringBuilder sb = new StringBuilder(this.sqlStatements.getProperty(COUNT));
            String countTypeUnion = this.sqlStatements.getProperty(COUNT_TYPE_UNION);
            for (int i = 0; i < types.size(); i++) {
                sb.append(' ').append(countTypeUnion);
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
            } catch (SQLException e) {
                throw new LanewebException(e);
            } finally {
                JdbcUtils.closeResultSet(rs);
                JdbcUtils.closeStatement(stmt);
                JdbcUtils.closeConnection(conn);
            }
        }
        return result;
    }

    public List<Eresource> searchSubset(final String subset, final String query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Eresource> searchType(final String type, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        params.add(translatedQuery);
        params.add(translatedQuery);
        params.add(type);
        return doGet(SEARCH_TYPE, params, query);
    }

    protected List<Eresource> doGet(final String sqlKey, final Collection<String> params, final String query) {
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
            StringBuilder sb = new StringBuilder("sqlKey: ").append(sqlKey).append(", params: ").append(params)
                    .append(", query: ").append(query);
            throw new LanewebException(sb.toString(), e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    protected Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    protected String getSQL(final String key) {
        return this.sqlStatements.getProperty(key);
    }

    protected abstract List<Eresource> parseResultSet(ResultSet rs, String query) throws SQLException;
}
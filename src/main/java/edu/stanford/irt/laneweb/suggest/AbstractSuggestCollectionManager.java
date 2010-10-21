package edu.stanford.irt.laneweb.suggest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.impl.EresourceImpl;
import edu.stanford.irt.laneweb.JdbcUtils;
import edu.stanford.irt.laneweb.eresources.CollectionManagerImpl;
import edu.stanford.irt.suggest.QueryNormalizer;

public abstract class AbstractSuggestCollectionManager extends CollectionManagerImpl {

    @Override
    public Collection<Eresource> search(String query) {
        Collection<String> params = searchStringToParams(query);
        return doSearch(getSearchSQL(), params);
    }

    @Override
    public Collection<Eresource> searchType(String type, String query) {
        Collection<String> params = searchStringToParams(query);
        params.add(type);
        return doSearch(getSearchTypeSQL(), params);
    }
    
    protected QueryNormalizer queryNormalizer = new QueryNormalizer();
    
    protected abstract String getSearchSQL();
    
    protected abstract String getSearchTypeSQL();

    private Collection<Eresource> doSearch(final String sql, final Collection<String> params) {
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
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    private Collection<Eresource> parseResultSet(final ResultSet rs) throws SQLException {
        Collection<Eresource> suggestions = new LinkedList<Eresource>();
        EresourceImpl eresource = null;
        int currentEresourceId = 0;
        String currentTitle = null;
        while (rs.next()) {
            int rowEresourceId = rs.getInt("ERESOURCE_ID");
            String rowTitle = rs.getString("TITLE");
            if (rowEresourceId != currentEresourceId || !rowTitle.equals(currentTitle)) {
                currentTitle = rowTitle;
                currentEresourceId = rowEresourceId;
                eresource = new EresourceImpl();
                eresource.setId(currentEresourceId);
                eresource.setTitle(currentTitle);
                suggestions.add(eresource);
            }
        }
        return suggestions;
    }
    
    protected abstract Collection<String> searchStringToParams(String query);
}
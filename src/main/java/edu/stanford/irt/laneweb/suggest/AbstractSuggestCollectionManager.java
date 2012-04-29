package edu.stanford.irt.laneweb.suggest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.impl.EresourceImpl;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.CollectionManagerImpl;
import edu.stanford.irt.laneweb.util.JdbcUtils;
import edu.stanford.irt.suggest.QueryNormalizer;

public abstract class AbstractSuggestCollectionManager extends CollectionManagerImpl {

    protected QueryNormalizer queryNormalizer = new QueryNormalizer();

    @Override
    public Collection<Eresource> search(final String query) {
        Collection<String> params = searchStringToParams(query);
        return doSearch(getSearchSQL(), params);
    }

    @Override
    public Collection<Eresource> searchType(final String type, final String query) {
        Collection<String> params = searchStringToParams(query);
        params.add(type);
        return doSearch(getSearchTypeSQL(), params);
    }

    protected abstract String getSearchSQL();

    protected abstract String getSearchTypeSQL();

    protected abstract Collection<String> searchStringToParams(String query);

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
            throw new LanewebException(e.getMessage() + "\nparams=" + params.toString());
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
}
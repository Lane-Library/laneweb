package edu.stanford.irt.laneweb.suggest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.eresources.AbstractCollectionManager;
import edu.stanford.irt.laneweb.eresources.Eresource;

public abstract class AbstractSuggestCollectionManager extends AbstractCollectionManager {

    public AbstractSuggestCollectionManager(final DataSource dataSource, final Properties sqlStatements) {
        super(dataSource, sqlStatements);
    }

    @Override
    public List<Eresource> search(final String query) {
        Collection<String> params = searchStringToParams(query);
        return doGet(SEARCH, params, query);
    }

    @Override
    public List<Eresource> searchType(final String type, final String query) {
        Collection<String> params = searchStringToParams(query);
        params.add(type);
        return doGet(SEARCH_TYPE, params, query);
    }

    @Override
    protected List<Eresource> parseResultSet(final ResultSet rs, final String query) throws SQLException {
        List<Eresource> suggestions = new LinkedList<Eresource>();
        Eresource eresource = null;
        int currentEresourceId = 0;
        String currentTitle = null;
        while (rs.next()) {
            int rowEresourceId = rs.getInt("ERESOURCE_ID");
            String rowTitle = rs.getString("TITLE");
            if (rowEresourceId != currentEresourceId || !rowTitle.equals(currentTitle)) {
                currentTitle = rowTitle;
                currentEresourceId = rowEresourceId;
                eresource = new Eresource(null, currentEresourceId, 0, null, 0, currentTitle);
                suggestions.add(eresource);
            }
        }
        return suggestions;
    }

    // TODO: refactor this as a strategy
    protected abstract Collection<String> searchStringToParams(String query);
}
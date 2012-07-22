package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.suggest.QueryNormalizer;

public class HistoryCollectionManager extends AbstractSuggestCollectionManager {

    private QueryNormalizer queryNormalizer = new QueryNormalizer();

    public HistoryCollectionManager(DataSource dataSource, Properties sqlStatements) {
        super(dataSource, sqlStatements);
    }

    @Override
    protected Collection<String> searchStringToParams(final String query) {
        Collection<String> params = new LinkedList<String>();
        String normalizedQuery = this.queryNormalizer.normalizeForContains(query);
        params.add(normalizedQuery);
        params.add(normalizedQuery);
        return params;
    }
}

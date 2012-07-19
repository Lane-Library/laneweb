package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import javax.sql.DataSource;

public class EresourceCollectionManager extends AbstractSuggestCollectionManager {
  
    public EresourceCollectionManager(DataSource dataSource, Properties sqlStatements) {
        super(dataSource, sqlStatements);
    }

    @Override
    protected Collection<String> searchStringToParams(final String term) {
        Collection<String> params = new LinkedList<String>();
        String likeSearchString = this.queryNormalizer.normalizeForLike(term);
        String titleSearchString = this.queryNormalizer.normalizeForWildcardContains(term);
        params.add(likeSearchString);
        params.add(titleSearchString);
        params.add(this.queryNormalizer.normalizeForContains(term));
        return params;
    }
}

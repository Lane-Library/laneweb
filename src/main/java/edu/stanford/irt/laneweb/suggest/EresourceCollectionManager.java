package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.suggest.QueryNormalizer;

public class EresourceCollectionManager extends AbstractSuggestCollectionManager {
    
    private Logger log;

    public EresourceCollectionManager(DataSource dataSource, Properties sqlStatement, Logger log) {
        super(dataSource, sqlStatement);
        this.log = log;
    }

    private QueryNormalizer queryNormalizer = new QueryNormalizer();

    @Override
    public List<Eresource> search(String query) {
        long time = System.currentTimeMillis();
        List<Eresource> result = super.search(query);
        time = System.currentTimeMillis() - time;
        this.log.info(new StringBuilder(Long.toString(time)).append(" ms search(").append(query).append(')').toString());
        return result;
    }

    @Override
    public List<Eresource> searchType(String type, String query) {
        long time = System.currentTimeMillis();
        List<Eresource> result = super.searchType(type, query);
        time = System.currentTimeMillis() - time;
        this.log.info(new StringBuilder(Long.toString(time)).append(" ms searchType(").append(type).append(", ").append(query).append(')').toString());
        return result;
    }

    public EresourceCollectionManager(final DataSource dataSource, final Properties sqlStatements) {
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

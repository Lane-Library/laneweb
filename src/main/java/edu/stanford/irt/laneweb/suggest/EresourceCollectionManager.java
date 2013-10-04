package edu.stanford.irt.laneweb.suggest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.util.JdbcUtils;
import edu.stanford.irt.suggest.QueryNormalizer;

public class EresourceCollectionManager extends AbstractSuggestCollectionManager {

    private DataSource dataSource;

    private Logger log;

    private QueryNormalizer queryNormalizer = new QueryNormalizer();

    private Properties sqlStatements;

    public EresourceCollectionManager(final DataSource dataSource, final Properties sqlStatements, final Logger log) {
        super(dataSource, sqlStatements);
        this.dataSource = dataSource;
        this.sqlStatements = sqlStatements;
        this.log = log;
    }

    @Override
    public List<Eresource> search(final String query) {
        long time = System.currentTimeMillis();
        List<Eresource> result = super.search(query);
        time = System.currentTimeMillis() - time;
        this.log.info(new StringBuilder(Long.toString(time)).append(" ms search(").append(query).append(')').toString());
        return result;
    }

    @Override
    public List<Eresource> searchType(final String type, final String query) {
        long time = System.currentTimeMillis();
        List<Eresource> result = super.searchType(type, query);
        time = System.currentTimeMillis() - time;
        this.log.info(new StringBuilder(Long.toString(time)).append(" ms searchType(").append(type).append(", ")
                .append(query).append(')').toString());
        return result;
    }

    @Override
    protected List<Eresource> doGet(final String sqlKey, final Collection<String> params, final String query) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            long time = System.currentTimeMillis();
            conn = this.dataSource.getConnection();
            time = System.currentTimeMillis() - time;
            this.log.info(new StringBuilder(Long.toString(time)).append(" ms getConnection()").toString());
            stmt = conn.prepareStatement(this.sqlStatements.getProperty(sqlKey));
            int index = 1;
            for (String param : params) {
                stmt.setString(index++, param);
            }
            time = System.currentTimeMillis();
            rs = stmt.executeQuery();
            time = System.currentTimeMillis() - time;
            this.log.info(new StringBuilder(Long.toString(time)).append(" ms executeQuery()").toString());
            time = System.currentTimeMillis();
            List<Eresource> list = parseResultSet(rs, query);
            time = System.currentTimeMillis() - time;
            this.log.info(new StringBuilder(Long.toString(time)).append(" ms parseResultSet()").toString());
            return list;
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

package edu.stanford.irt.laneweb.voyager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class VoyagerMeshSuggest {

    private static final String DISEASE_LIMIT =
        " intersect "
        + "select distinct bib_id from cifdb.bib_index where "
        + "((index_code = '655H' and normal_heading = 'TOPIC DISEASE')"
        + " or "
        + "(index_code = '6502' and (normal_heading = 'DISEASE OR SYNDROME' or normal_heading = 'MENTAL OR BEHAVIORAL DYSFUNCTION')))";

    private static final String INTERVENTION_LIMIT =
        " intersect "
        + "(select distinct bib_id from cifdb.bib_index where index_code = '655H' and (normal_heading = 'TOPIC SUBSTANCE')"
        + " union "
        + "select distinct bib_id from cifdb.bib_index where index_code = '6502'"
        + " and "
        + "(normal_heading = 'THERAPEUTIC OR PREVENTIVE PROCEDURE' or normal_heading = 'MEDICAL DEVICE'))";

    private static final int MIN_QUERY_LENGTH = 3;

    private static final String SQL_1 =
        "select distinct display_heading from cifdb.bib_index"
        + " where index_code = '2451' and rownum <= 100 and bib_id in"
        + " (select distinct bib_id from cifdb.bib_index where index_code = '6502' and (normal_heading like '% ";

    private static final String SQL_2 = "%' or normal_heading like '";

    private static final String SQL_3 =
        "%')"
        + "  intersect "
        + " select distinct bib_id from cifdb.bib_index where index_code = '655H' and normal_heading = 'MESH'";

    private static final String SQL_4 = ") order by display_heading";

    private DataSource dataSource;

    private Logger logger = Logger.getLogger(VoyagerMeshSuggest.class);

    public TreeSet<String> getMesh(final String query, final String limit) {
        if (null == query) {
            throw new IllegalArgumentException("null query");
        }
        if (MIN_QUERY_LENGTH > query.length()) {
            throw new IllegalArgumentException("query too short");
        }
        TreeSet<String> meshSet = new TreeSet<String>();
        String filteredQuery = filterQuery(query);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            StringBuffer sb = new StringBuffer(SQL_1).append(filteredQuery).append(SQL_2).append(filteredQuery).append(SQL_3);
            if ("p".equalsIgnoreCase(limit) || "d".equalsIgnoreCase(limit)) { // patient or disease
                sb.append(DISEASE_LIMIT);
            } else if ("i".equalsIgnoreCase(limit)) { // intervention
                sb.append(INTERVENTION_LIMIT);
            }
            sb.append(SQL_4);
            String sql = sb.toString();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(sql);
            }
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                meshSet.add(rs.getString(1));
            }
            return meshSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // ?
                }
            }
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // ?
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ?
                }
            }
        }
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

    /**
     * filter query string: strip ' and " and ' characters, convert to upper
     * case; non alphanumerics to blanks
     * 
     * @param query
     * @return String filtered query string
     */
    String filterQuery(final String query) {
        String q = query.toUpperCase();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < q.length(); i++) {
            char c = q.charAt(i);
            if (Character.isLetter(c) || Character.isDigit(c)) {
                sb.append(c);
            } else if (('\'' != c) && ('"' != c) && (',' != c)) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }
}

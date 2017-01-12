package edu.stanford.irt.laneweb.proxy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class DatabaseProxyHostSet  extends HashSet<String> {

    private static final Logger log = LoggerFactory.getLogger(DatabaseProxyHostSet.class);

    private static final long serialVersionUID = 1L;

    private static final String SQL =
            "SELECT DISTINCT URL_HOST AS HOST " +
            "FROM LMLDB.ELINK_INDEX " +
            "WHERE LINK_SUBTYPE         IN ('HTTP','HTTPS') " +
            "AND ELINK_INDEX.RECORD_TYPE = 'A' " +
            "AND URL_HOST NOT LIKE '%.stanford.edu' " +
            "UNION " +
            "SELECT DISTINCT URL_HOST AS HOST " +
            "FROM LMLDB.ELINK_INDEX, " +
            "  LMLDB.MFHD_MASTER, " +
            "  LMLDB.BIB_MFHD, " +
            "  LMLDB.BIB_MASTER " +
            "WHERE ELINK_INDEX.RECORD_ID    = MFHD_MASTER.MFHD_ID " +
            "AND MFHD_MASTER.MFHD_ID = BIB_MFHD.MFHD_ID " +
            "AND BIB_MFHD.BIB_ID = BIB_MASTER.BIB_ID " +
            "AND LINK_SUBTYPE              IN ('HTTP','HTTPS') " +
            "AND ELINK_INDEX.RECORD_TYPE    = 'M' " +
            "AND ELINK_INDEX.RECORD_ID NOT IN " +
            "  (SELECT MFHD_ID " +
            "  FROM LMLDB.MFHD_DATA " +
            "  WHERE LOWER(RECORD_SEGMENT) LIKE '%, noproxy%' " +
            "  ) " +
            "AND URL_HOST NOT LIKE '%.stanford.edu' " +
            "AND MFHD_MASTER.SUPPRESS_IN_OPAC != 'Y' " +
            "AND BIB_MASTER.SUPPRESS_IN_OPAC != 'Y'";

    public DatabaseProxyHostSet(final DataSource dataSource) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        if (log.isInfoEnabled()) {
            log.info("retrieving new proxy host set from the voyager catalog");
        }
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                add(rs.getString(1));
            }
            add("bodoni.stanford.edu");
            add("library.stanford.edu");
            add("searchworks.stanford.edu");
        } catch (SQLException e) {
            throw new LanewebException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }
}
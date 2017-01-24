package edu.stanford.irt.laneweb.proxy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.util.JdbcUtils;

public class ProxyHostSource {

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

    private DataSource dataSource;

    public ProxyHostSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Set<String> getHosts() throws SQLException {
        Set<String> hosts = new HashSet<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                hosts.add(rs.getString(1));
            }
            hosts.add("bodoni.stanford.edu");
            hosts.add("library.stanford.edu");
            hosts.add("searchworks.stanford.edu");
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
        return hosts;
    }
}
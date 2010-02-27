package edu.stanford.irt.laneweb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * shamelessly stolen from org.springframework.jdbc.support.JdbcUtils
 * 
 * @author ceyates
 */
public abstract class JdbcUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcUtils.class);

    /**
     * Close the given JDBC Connection and ignore any thrown exception. This is useful for typical finally blocks in
     * manual JDBC code.
     * 
     * @param con
     *            the JDBC Connection to close (may be <code>null</code>)
     */
    public static void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                LOG.debug("Could not close JDBC Connection", ex);
            } catch (Throwable ex) {
                // We don't trust the JDBC driver: It might throw
                // RuntimeException or Error.
                LOG.debug("Unexpected exception on closing JDBC Connection", ex);
            }
        }
    }

    /**
     * Close the given JDBC ResultSet and ignore any thrown exception. This is useful for typical finally blocks in
     * manual JDBC code.
     * 
     * @param rs
     *            the JDBC ResultSet to close (may be <code>null</code>)
     */
    public static void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                LOG.trace("Could not close JDBC ResultSet", ex);
            } catch (Throwable ex) {
                // We don't trust the JDBC driver: It might throw
                // RuntimeException or Error.
                LOG.trace("Unexpected exception on closing JDBC ResultSet", ex);
            }
        }
    }

    /**
     * Close the given JDBC Statement and ignore any thrown exception. This is useful for typical finally blocks in
     * manual JDBC code.
     * 
     * @param stmt
     *            the JDBC Statement to close (may be <code>null</code>)
     */
    public static void closeStatement(final Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                LOG.trace("Could not close JDBC Statement", ex);
            } catch (Throwable ex) {
                // We don't trust the JDBC driver: It might throw
                // RuntimeException or Error.
                LOG.trace("Unexpected exception on closing JDBC Statement", ex);
            }
        }
    }
}

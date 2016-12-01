package edu.stanford.irt.laneweb.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * shamelessly stolen from org.springframework.jdbc.support.JdbcUtils
 */
public class JdbcUtils {

    private JdbcUtils() {
        // default empty constructor
    }

    /**
     * Close the given JDBC Connection and ignore any thrown SQLException. This is useful for typical finally blocks in
     * manual JDBC code.
     *
     * @param con
     *            the JDBC Connection to close (may be <code>null</code>)
     */
    public static void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new LanewebException(e);
            }
        }
    }

    /**
     * Close the given JDBC ResultSet and ignore any thrown SQLException. This is useful for typical finally blocks in
     * manual JDBC code.
     *
     * @param rs
     *            the JDBC ResultSet to close (may be <code>null</code>)
     */
    public static void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new LanewebException(e);
            }
        }
    }

    /**
     * Close the given JDBC Statement and ignore any thrown SQLException. This is useful for typical finally blocks in
     * manual JDBC code.
     *
     * @param stmt
     *            the JDBC Statement to close (may be <code>null</code>)
     */
    public static void closeStatement(final Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new LanewebException(e);
            }
        }
    }
}

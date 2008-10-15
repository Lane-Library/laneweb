package edu.stanford.irt.laneweb.voyager;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;

import edu.stanford.irt.directory.LDAPPerson;

public class VoyagerLoginImpl extends AbstractLogEnabled implements VoyagerLogin, Serviceable, ThreadSafe {

    private static final String ERROR_URL = "/voyagerError.html";

    private static final String BASE_URL = "http://lmldb-test.stanford.edu/cgi-bin/Pwebrecon.cgi?";

    private static final String CLEAR_SESSION_SQL = "DELETE FROM LMLDB.WOPAC_PID_PATRON_KEYS WHERE PATRON_KEY = ? OR PID = ?";

    private static final String CREATE_SESSION_SQL = "INSERT INTO LMLDB.WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES (?,?)";

    private DataSource dataSource;

    public String getVoyagerURL(final LDAPPerson person, final String pid, final String queryString) {
        if ((null == pid) || !pid.matches("[\\w0-9-_]+")) {
            getLogger().error("bad pid: " + pid);
            return ERROR_URL;
        }
        if (null == person) {
            getLogger().error("null person");
            return ERROR_URL;
        }
        String univId = person.getUnivId();
        if ((null == univId) || (univId.length() == 0)) {
            getLogger().error("pad univId: " + univId);
            return ERROR_URL;
        }

        univId = "0" + univId; // voyager data prepends 0

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(CLEAR_SESSION_SQL);
            stmt.setString(1, univId);
            stmt.setString(2, pid);
            stmt.executeUpdate();
            stmt = conn.prepareStatement(CREATE_SESSION_SQL);
            stmt.setString(1, univId);
            stmt.setString(2, pid);
            stmt.executeUpdate();
            return BASE_URL.concat(queryString).concat("&authenticate=Y");
        } catch (SQLException e) {
            getLogger().error(e.getMessage(), e);
            return ERROR_URL;
        } finally {
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
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

    public void service(final ServiceManager manager) throws ServiceException {
        ServiceSelector selector = (ServiceSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
        final DataSourceComponent dsc = (DataSourceComponent) selector.select("voyager");
        setDataSource(new DataSource() {

            public Connection getConnection() throws SQLException {
                return dsc.getConnection();
            }

            public Connection getConnection(final String username, final String password) throws SQLException {
                throw new UnsupportedOperationException();
            }

            public PrintWriter getLogWriter() throws SQLException {
                throw new UnsupportedOperationException();
            }

            public int getLoginTimeout() throws SQLException {
                throw new UnsupportedOperationException();
            }

            public void setLogWriter(final PrintWriter out) throws SQLException {
                throw new UnsupportedOperationException();
            }

            public void setLoginTimeout(final int seconds) throws SQLException {
                throw new UnsupportedOperationException();
            }
        });
        manager.release(selector);
    }

}

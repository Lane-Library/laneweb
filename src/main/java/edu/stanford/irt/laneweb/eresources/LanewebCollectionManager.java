package edu.stanford.irt.laneweb.eresources;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;

import edu.stanford.irt.eresources.impl.CollectionManagerImpl;

public class LanewebCollectionManager extends CollectionManagerImpl implements
        ThreadSafe, Serviceable {

    public void service(ServiceManager manager) throws ServiceException {
        ServiceSelector selector = (ServiceSelector) manager
                .lookup(DataSourceComponent.ROLE + "Selector");
        final DataSourceComponent dataSourceComponent = (DataSourceComponent) selector
        .select("eresources");
        super.setDataSource(new DataSource() {
            
            private PrintWriter logWriter;

            public Connection getConnection() throws SQLException {
                return dataSourceComponent.getConnection();
            }

            public Connection getConnection(String username, String password)
                    throws SQLException {
                return dataSourceComponent.getConnection();
            }

            public PrintWriter getLogWriter() throws SQLException {
                return this.logWriter;
            }

            public int getLoginTimeout() throws SQLException {
                throw new UnsupportedOperationException("sorry, can't do that");
            }

            public void setLogWriter(PrintWriter out) throws SQLException {
                this.logWriter = out;
            }

            public void setLoginTimeout(int seconds) throws SQLException {
                throw new UnsupportedOperationException("sorry, can't do that");
                
            }});
        manager.release(selector);
    }

}

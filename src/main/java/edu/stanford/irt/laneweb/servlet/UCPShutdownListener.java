package edu.stanford.irt.laneweb.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.stanford.irt.laneweb.LanewebException;
import oracle.ucp.UniversalConnectionPoolException;
import oracle.ucp.admin.UniversalConnectionPoolManager;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;

public class UCPShutdownListener implements ServletContextListener {

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        try {
            UniversalConnectionPoolManager manager = UniversalConnectionPoolManagerImpl
                    .getUniversalConnectionPoolManager();
            String[] poolNames = manager.getConnectionPoolNames();
            if (poolNames != null) {
                for (String poolName : poolNames) {
                    manager.destroyConnectionPool(poolName);
                }
            }
        } catch (UniversalConnectionPoolException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        // do nothing
    }
}
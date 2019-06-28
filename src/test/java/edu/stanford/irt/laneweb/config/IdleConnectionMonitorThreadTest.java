package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IdleConnectionMonitorThreadTest {

    private HttpClientConnectionManager connectionManager;

    private IdleConnectionMonitorThread monitor;

    @Before
    public void setUp() throws Exception {
        this.connectionManager = mock(HttpClientConnectionManager.class);
        this.monitor = new IdleConnectionMonitorThread(this.connectionManager);
    }

    @Test
    public final void testGettersSetters() {
        assertEquals(30_000, this.monitor.getIdleConnectionTime());
        assertEquals(5_000, this.monitor.getPollingTime());
        this.monitor.setIdleConnectionTime(10_000);
        this.monitor.setPollingTime(15_000);
        assertEquals(10_000, this.monitor.getIdleConnectionTime());
        assertEquals(15_000, this.monitor.getPollingTime());
        assertFalse(this.monitor.isShutdown());
    }

    @Test
    public final void testRun() throws Exception {
        this.monitor.setPollingTime(100);
        this.connectionManager.closeExpiredConnections();
        expectLastCall().atLeastOnce();
        this.connectionManager.closeIdleConnections(30_000, TimeUnit.MILLISECONDS);
        expectLastCall().atLeastOnce();
        replay(this.connectionManager);
        this.monitor.start();
        TimeUnit.MILLISECONDS.sleep(250);
        verify(this.connectionManager);
        this.monitor.shutdown();
        assertTrue(this.monitor.isShutdown());
    }

    @Test
    // run last so don't interrupt other tests
    public final void testRunWithException() throws Exception {
        this.monitor.setPollingTime(10);
        Thread.currentThread().interrupt();
        this.monitor.run();
        assertTrue(this.monitor.isShutdown());
    }
}

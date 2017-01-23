package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class ProxyHostManagerTest {

    private ScheduledExecutorService executor;

    private ProxyHostSource hostSource;

    private ProxyHostManager manager;

    @Before
    public void setUp() throws Exception {
        this.hostSource = createMock(ProxyHostSource.class);
        this.executor = createMock(ScheduledExecutorService.class);
        expect(this.executor.scheduleAtFixedRate(isA(Runnable.class), eq(0L), eq(120L), eq(TimeUnit.MINUTES)))
                .andReturn(null);
        replay(this.executor);
        this.manager = new ProxyHostManager(this.hostSource, this.executor);
        verify(this.executor);
        reset(this.executor);
    }

    @Test
    public void testConstructorStartsExecutor() {
        assertNotNull(new ProxyHostManager(this.hostSource, new ScheduledThreadPoolExecutor(1) {

            @Override
            public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay,
                    final long period, final TimeUnit unit) {
                try {
                    expect(ProxyHostManagerTest.this.hostSource.getHosts()).andReturn(Collections.emptySet());
                    replay(ProxyHostManagerTest.this.hostSource);
                    command.run();
                    verify(ProxyHostManagerTest.this.hostSource);
                } catch (SQLException e) {
                }
                return null;
            }
        }));
    }

    @Test
    public void testConstructorStartsExecutorSQLException() {
        assertNotNull(new ProxyHostManager(this.hostSource, new ScheduledThreadPoolExecutor(1) {

            @Override
            public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay,
                    final long period, final TimeUnit unit) {
                try {
                    expect(ProxyHostManagerTest.this.hostSource.getHosts()).andThrow(new SQLException());
                    replay(ProxyHostManagerTest.this.hostSource);
                    command.run();
                } catch (SQLException e) {
                }
                return null;
            }
        }));
    }

    @Test
    public void testDestroy() {
        expect(this.executor.shutdownNow()).andReturn(null);
        replay(this.executor);
        this.manager.destroy();
        verify(this.executor);
    }

    @Test
    public void testIsProxyableHost() {
        assertTrue(this.manager.isProxyableHost("www.uptodate.com"));
    }

    @Test
    public void testIsProxyableHostNull() {
        assertFalse(this.manager.isProxyableHost(null));
    }

    @Test
    public void testIsProxyableLinkHost() {
        assertTrue(this.manager.isProxyableLink("http://www.uptodate.com/index.html"));
    }

    @Test
    public void testIsProxyableLinkMalformed() {
        assertFalse(this.manager.isProxyableLink("\u0000:foo"));
    }

    @Test
    public void testIsProxyableLinkNull() {
        assertFalse(this.manager.isProxyableLink(null));
    }
}
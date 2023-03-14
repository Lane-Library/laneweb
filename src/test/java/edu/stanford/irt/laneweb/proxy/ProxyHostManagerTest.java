package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class ProxyHostManagerTest {

    private ScheduledExecutorService executor;

    private ProxyHostManager manager;

    private ProxyServersService service;

    @Before
    public void setUp() throws Exception {
        this.service = mock(ProxyServersService.class);
        this.executor = mock(ScheduledExecutorService.class);
        expect(this.executor.scheduleAtFixedRate(isA(Runnable.class), eq(0L), eq(120L), eq(TimeUnit.MINUTES)))
                .andReturn(null);
        replay(this.executor);
        this.manager = new ProxyHostManager(this.service, this.executor);
        verify(this.executor);
        reset(this.executor);
    }

    @Test
    public void testConstructorStartsExecutor() {
        assertNotNull(new ProxyHostManager(this.service, new ScheduledThreadPoolExecutor(1) {

            @Override
            public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay,
                    final long period, final TimeUnit unit) {
                expect(ProxyHostManagerTest.this.service.getHosts()).andReturn(Collections.emptySet());
                replay(ProxyHostManagerTest.this.service);
                command.run();
                verify(ProxyHostManagerTest.this.service);
                return null;
            }
        }));
    }

    @Test
    public void testConstructorStartsExecutorSQLException() {
        assertNotNull(new ProxyHostManager(this.service, new ScheduledThreadPoolExecutor(1) {

            @Override
            public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay,
                    final long period, final TimeUnit unit) {
                expect(ProxyHostManagerTest.this.service.getHosts()).andThrow(new LanewebException(""));
                replay(ProxyHostManagerTest.this.service);
                command.run();
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
    public void testIsProxyableBadURI() {
        assertTrue(this.manager.isProxyableLink(
                "https://publications.aap.org/en-us/kb/Documents/Red Book Online/Access Red Book Online From Anywhere.pdf"));
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

    @Test
    public void testIsProxyableNoTrailingSlash() {
        assertTrue(this.manager.isProxyableLink("http://www.uptodate.com"));
    }
}
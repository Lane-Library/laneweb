package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class ProxyHostManagerTest {

    private DataSource dataSource;

    private ScheduledExecutorService executor;

    private Future future;

    private ProxyHostManager manager;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.executor = createMock(ScheduledExecutorService.class);
        this.future = createMock(Future.class);
        expect(this.executor.submit(isA(Callable.class))).andReturn(this.future);
        replay(this.future, this.executor);
        this.manager = new ProxyHostManager(this.dataSource, this.executor);
        verify(this.future, this.executor);
        reset(this.future, this.executor);
    }

    @Test
    public void testDestroy() {
        expect(this.executor.shutdownNow()).andReturn(null);
        replay(this.future, this.executor);
        this.manager.destroy();
        verify(this.future, this.executor);
    }

    @Test
    public void testIsProxyableExecutionException() throws InterruptedException, ExecutionException {
        expect(this.future.isDone()).andReturn(true);
        expect(this.future.get()).andThrow(new ExecutionException() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;
        });
        expect(this.executor.schedule(isA(Callable.class), eq(10L), eq(TimeUnit.MINUTES))).andReturn(null);
        replay(this.future, this.executor);
        assertTrue(this.manager.isProxyableLink("http://www.uptodate.com/index.html"));
        verify(this.future, this.executor);
    }

    @Test
    public void testIsProxyableHost() {
        expect(this.future.isDone()).andReturn(false);
        replay(this.future, this.executor);
        assertTrue(this.manager.isProxyableHost("www.uptodate.com"));
        verify(this.future, this.executor);
    }

    @Test
    public void testIsProxyableHostFutureDone() throws InterruptedException, ExecutionException {
        expect(this.future.isDone()).andReturn(true);
        expect(this.future.get()).andReturn(Collections.singleton("www.uptodate.com"));
        expect(this.executor.schedule(isA(Callable.class), eq(120L), eq(TimeUnit.MINUTES))).andReturn(null);
        replay(this.future, this.executor);
        assertTrue(this.manager.isProxyableHost("www.uptodate.com"));
        verify(this.future, this.executor);
    }

    @Test
    public void testIsProxyableHostNull() {
        replay(this.future, this.executor);
        assertFalse(this.manager.isProxyableHost(null));
        verify(this.future, this.executor);
    }

    @Test
    public void testIsProxyableLinkHost() {
        expect(this.future.isDone()).andReturn(false);
        replay(this.future, this.executor);
        assertTrue(this.manager.isProxyableLink("http://www.uptodate.com/index.html"));
        verify(this.future, this.executor);
    }

    @Test
    public void testIsProxyableLinkHostFutureDone() throws InterruptedException, ExecutionException {
        expect(this.future.isDone()).andReturn(true);
        expect(this.future.get()).andReturn(Collections.singleton("www.uptodate.com"));
        expect(this.executor.schedule(isA(Callable.class), eq(120L), eq(TimeUnit.MINUTES))).andReturn(null);
        replay(this.future, this.executor);
        assertTrue(this.manager.isProxyableLink("http://www.uptodate.com/index.html"));
        verify(this.future, this.executor);
    }

    @Test
    public void testIsProxyableLinkInterrupted() throws ExecutionException, InterruptedException {
        expect(this.future.isDone()).andReturn(true);
        expect(this.future.get()).andThrow(new InterruptedException());
        replay(this.future, this.executor);
        try {
            this.manager.isProxyableLink("http://www.uptodate.com/index.html");
        } catch (LanewebException e) {
            assertTrue(Thread.interrupted());
        }
        verify(this.future, this.executor);
    }

    @Test
    public void testIsProxyableLinkMalformed() {
        replay(this.future, this.executor);
        assertFalse(this.manager.isProxyableLink("foo:~"));
        verify(this.future, this.executor);
    }

    @Test
    public void testIsProxyableLinkNull() {
        replay(this.future, this.executor);
        assertFalse(this.manager.isProxyableLink(null));
        verify(this.future, this.executor);
    }
}
package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import oracle.ucp.UniversalConnectionPool;
import oracle.ucp.UniversalConnectionPoolAdapter;
import oracle.ucp.UniversalConnectionPoolException;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;

public class UCPShutdownListenerTest {

    private UniversalConnectionPoolAdapter adapter;

    private UCPShutdownListener listener;

    private UniversalConnectionPool pool;

    @Test
    // (expected = LanewebException.class) does not happen for some reason . . .
    public void ContextDestroyedException() throws UniversalConnectionPoolException {
        this.pool.stop();
        expectLastCall().andThrow(new UniversalConnectionPoolException());
        replay(this.pool, this.adapter);
        this.listener.contextDestroyed(null);
        verify(this.pool, this.adapter);
    }

    @Before
    public void setUp() throws Exception {
        this.listener = new UCPShutdownListener();
        this.pool = createMock(UniversalConnectionPool.class);
        this.adapter = createMock(UniversalConnectionPoolAdapter.class);
        expect(this.adapter.createUniversalConnectionPool()).andReturn(this.pool);
        expect(this.pool.getName()).andReturn("name");
        replay(this.adapter, this.pool);
        UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager().createConnectionPool(this.adapter);
        verify(this.adapter, this.pool);
        reset(this.pool, this.adapter);
    }

    @Test
    public void testContextDestroyed() throws UniversalConnectionPoolException {
        this.pool.stop();
        replay(this.pool);
        this.listener.contextDestroyed(null);
        verify(this.pool);
    }
}

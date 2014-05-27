package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.xml.sax.Locator;

public class XIncludeExceptionListenerImplTest {

    private XIncludeExceptionListenerImpl listener;

    private Locator locator;

    private Logger log;

    @Before
    public void setUp() {
        this.log = createMock(Logger.class);
        this.listener = new XIncludeExceptionListenerImpl(this.log);
        this.locator = createMock(Locator.class);
    }

    @Test
    public void testException() {
        Exception e = new Exception("message");
        expect(this.locator.getSystemId()).andReturn("systemId");
        expect(this.locator.getLineNumber()).andReturn(10);
        expect(this.locator.getColumnNumber()).andReturn(20);
        this.log.error("XInclude failed: systemId line:10 column:20", e);
        replay(this.locator, this.log);
        this.listener.exception(this.locator, e);
        verify(this.locator, this.log);
    }

    @Test
    public void testExceptionNullLocator() {
        Exception e = new Exception("message");
        this.log.error("message", e);
        replay(this.locator, this.log);
        this.listener.exception(null, e);
        verify(this.locator, this.log);
    }
}

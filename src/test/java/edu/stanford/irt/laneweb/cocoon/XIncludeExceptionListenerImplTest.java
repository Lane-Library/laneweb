package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Locator;

public class XIncludeExceptionListenerImplTest {

    private XIncludeExceptionListenerImpl listener;

    private Locator locator;

    @BeforeEach
    public void setUp() {
        this.listener = new XIncludeExceptionListenerImpl();
        this.locator = mock(Locator.class);
    }

    @Test
    public void testException() {
        Exception e = new Exception("message");
        expect(this.locator.getSystemId()).andReturn("systemId");
        expect(this.locator.getLineNumber()).andReturn(10);
        expect(this.locator.getColumnNumber()).andReturn(20);
        replay(this.locator);
        this.listener.exception(this.locator, e);
        verify(this.locator);
    }

    @Test
    public void testExceptionNullLocator() {
        Exception e = new Exception("message");
        replay(this.locator);
        this.listener.exception(null, e);
        verify(this.locator);
    }
}

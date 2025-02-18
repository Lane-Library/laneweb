package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class TransformerErrorListenerTest {

    private TransformerException exception;

    private TransformerErrorListener listener;

    private SourceLocator locator;

    @BeforeEach
    public void setUp() {
        this.listener = new TransformerErrorListener();
        this.exception = mock(TransformerException.class);
        this.locator = mock(SourceLocator.class);
    }

    @Test
    public void testError() {
        expect(this.exception.getMessage()).andReturn("message");
        expect(this.exception.getLocator()).andReturn(this.locator);
        expect(this.locator.getPublicId()).andReturn("publicid");
        expect(this.locator.getSystemId()).andReturn("systemid");
        expect(this.locator.getLineNumber()).andReturn(0);
        expect(this.locator.getColumnNumber()).andReturn(0);
        replay(this.exception, this.locator);
        try {
            this.listener.error(this.exception);
        } catch (LanewebException e) {
        }
        verify(this.exception, this.locator);
    }

    @Test
    public void testFatalError() {
        expect(this.exception.getMessage()).andReturn("message");
        expect(this.exception.getLocator()).andReturn(this.locator);
        expect(this.locator.getPublicId()).andReturn("publicid");
        expect(this.locator.getSystemId()).andReturn("systemid");
        expect(this.locator.getLineNumber()).andReturn(0);
        expect(this.locator.getColumnNumber()).andReturn(0);
        replay(this.exception, this.locator);
        try {
            this.listener.fatalError(this.exception);
        } catch (LanewebException e) {
        }
        verify(this.exception, this.locator);
    }

    @Test
    public void testWarning() {
        expect(this.exception.getMessage()).andReturn("message");
        expect(this.exception.getLocator()).andReturn(this.locator);
        expect(this.locator.getPublicId()).andReturn("publicid");
        expect(this.locator.getSystemId()).andReturn("systemid");
        expect(this.locator.getLineNumber()).andReturn(0);
        expect(this.locator.getColumnNumber()).andReturn(0);
        replay(this.exception, this.locator);
        try {
            this.listener.warning(this.exception);
        } catch (LanewebException e) {
        }
        verify(this.exception, this.locator);
    }

    @Test
    public void testWarningNoLocator() {
        expect(this.exception.getMessage()).andReturn("message");
        expect(this.exception.getLocator()).andReturn(null);
        replay(this.exception);
        try {
            this.listener.warning(this.exception);
        } catch (LanewebException e) {
        }
        verify(this.exception);
    }
}

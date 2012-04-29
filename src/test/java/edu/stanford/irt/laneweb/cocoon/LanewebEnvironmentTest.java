package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class LanewebEnvironmentTest {

    private LanewebEnvironment environment;

    private Map<String, Object> model;

    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        this.model = new HashMap<String, Object>();
        this.outputStream = createMock(OutputStream.class);
        this.environment = new LanewebEnvironment(this.model, this.outputStream, false);
    }

    @Test
    public void testCommitResponse() throws IOException {
        try {
            this.environment.commitResponse();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testFinishingProcessing() {
        try {
            this.environment.finishingProcessing();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testGetAction() {
        try {
            this.environment.getAction();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testGetAttribute() {
        assertNull(this.environment.getAttribute(null));
    }

    @Test
    public void testGetAttributeNames() {
        try {
            this.environment.getAttributeNames();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testGetContentType() {
        try {
            this.environment.getContentType();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testGetObjectModel() {
        assertEquals(this.model, this.environment.getObjectModel());
    }

    @Test
    public void testGetOutputStream() throws IOException {
        assertEquals(this.outputStream, this.environment.getOutputStream(0));
    }

    @Test
    public void testGetURI() {
        assertNull(this.environment.getURI());
    }

    @Test
    public void testGetURIPrefix() {
        assertNull(this.environment.getURIPrefix());
    }

    @Test
    public void testGetView() {
        assertNull(this.environment.getView());
    }

    @Test
    public void testIsExternal() {
        assertFalse(this.environment.isExternal());
    }

    @Test
    public void testIsInternalRedirect() {
        assertFalse(this.environment.isInternalRedirect());
    }

    @Test
    public void testIsResponseModified() {
        try {
            this.environment.isResponseModified(0);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testRedirect() throws IOException {
        try {
            this.environment.redirect(null, false, false);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testRemoveAttribute() {
        try {
            this.environment.removeAttribute(null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetAttribute() {
        try {
            this.environment.setAttribute(null, null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetContentLength() {
        try {
            this.environment.setContentLength(0);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetContentType() {
        try {
            this.environment.setContentType(null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetHttpServletRequest() {
        try {
            this.environment.setHttpServletRequest(null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetHttpServletResponse() {
        try {
            this.environment.setHttpServletResponse(null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetResponseIsNotModified() {
        try {
            this.environment.setResponseIsNotModified();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetServletContext() {
        try {
            this.environment.setServletContext(null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetStatus() {
        this.environment.setStatus(0);
    }

    @Test
    public void testSetURI() {
        try {
            this.environment.setURI(null, null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testStartingProcessing() {
        try {
            this.environment.startingProcessing();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testTryResetResponse() throws IOException {
        try {
            this.environment.tryResetResponse();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }
}

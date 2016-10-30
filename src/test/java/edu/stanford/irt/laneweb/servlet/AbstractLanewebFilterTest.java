package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class AbstractLanewebFilterTest {

    private static class TestAbstractLanewebFilter extends AbstractLanewebFilter {

        private boolean internalDoFilterCalled;

        @Override
        protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
                final FilterChain chain) {
            this.internalDoFilterCalled = true;
        }
    }

    private FilterChain chain;

    private TestAbstractLanewebFilter filter;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.filter = new TestAbstractLanewebFilter();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.chain = createMock(FilterChain.class);
    }

    @Test
    public void testDoFilter() throws IOException, ServletException {
        this.filter.init(null);
        this.filter.doFilter(this.request, this.response, this.chain);
        this.filter.destroy();
        assertTrue(this.filter.internalDoFilterCalled);
    }
}

package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.mock;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    public void setUp() throws Exception {
        this.filter = new TestAbstractLanewebFilter();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.chain = mock(FilterChain.class);
    }

    @Test
    public void testDoFilter() throws IOException, ServletException {
        this.filter.init(null);
        this.filter.doFilter(this.request, this.response, this.chain);
        this.filter.destroy();
        assertTrue(this.filter.internalDoFilterCalled);
    }
}

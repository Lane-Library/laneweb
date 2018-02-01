package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

public class NOOPFilterTest {

    private FilterChain chain;

    private NOOPFilter filter;

    @Before
    public void setUp() {
        this.filter = new NOOPFilter();
        this.chain = mock(FilterChain.class);
    }

    @Test
    public void testInternalDoFilter() throws IOException, ServletException {
        this.chain.doFilter(null, null);
        replay(this.chain);
        this.filter.internalDoFilter(null, null, this.chain);
        verify(this.chain);
    }
}

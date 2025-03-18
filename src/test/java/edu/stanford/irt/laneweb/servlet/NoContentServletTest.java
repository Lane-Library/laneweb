package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NoContentServletTest {

    private NoContentServlet filter;

    private HttpServletResponse response;

    @BeforeEach
    public void setUp() throws Exception {
        this.filter = new NoContentServlet();
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    public void testService() throws IOException, ServletException {
        this.response.sendError(204);
        replay(this.response);
        this.filter.service(null, this.response);
        verify(this.response);
    }
}

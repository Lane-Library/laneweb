package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class NotFoundServletTest {

    private NotFoundServlet filter;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.filter = new NotFoundServlet();
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testService() throws IOException, ServletException {
        this.response.sendError(404);
        replay(this.response);
        this.filter.service(null, this.response);
        verify(this.response);
    }
}

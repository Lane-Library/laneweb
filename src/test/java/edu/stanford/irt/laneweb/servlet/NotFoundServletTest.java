package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

class NotFoundServletTest {

    private HttpServletResponse response;

    private NotFoundServlet servlet;

    @BeforeEach
    void setUp() {
        this.servlet = new NotFoundServlet();
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    void testService() throws IOException, ServletException {
        this.response.sendError(404);
        replay(this.response);
        this.servlet.service(null, this.response);
        verify(this.response);
    }
}

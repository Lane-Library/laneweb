package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.servlet.mvc.LaneCrmController.URLConnectionFactory;

public class LaneCrmControllerTest {

    private HttpsURLConnection connection;

    private URLConnectionFactory connectionFactory;

    private LaneCrmController controller;

    @Before
    public void setUp() {
        this.connectionFactory = createMock(URLConnectionFactory.class);
        this.controller = new LaneCrmController("acquisitions", "askus", this.connectionFactory);
        this.connection = createMock(HttpsURLConnection.class);
    }

    @Test
    public void testFormSubmitLaneaskus() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Model model = createMock(Model.class);
        expect(model.asMap()).andReturn(Collections.singletonMap("foo", "bar"));
        expect(this.connectionFactory.getConnection("askus")).andReturn(this.connection);
        this.connection.setDoOutput(true);
        expect(this.connection.getOutputStream()).andReturn(baos);
        expect(this.connection.getResponseCode()).andReturn(200);
        replay(model, this.connectionFactory, this.connection);
        assertEquals("redirect:/index.html", this.controller.formSubmitLaneaskus(model, null));
        assertEquals("foo=bar&id=", new String(baos.toByteArray()));
        verify(model, this.connectionFactory, this.connection);
    }

    @Test
    public void testFormSubmitLanelibacqs() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Model model = createMock(Model.class);
        expect(model.asMap()).andReturn(Collections.singletonMap("foo", "bar"));
        expect(this.connectionFactory.getConnection("acquisitions")).andReturn(this.connection);
        this.connection.setDoOutput(true);
        expect(this.connection.getOutputStream()).andReturn(baos);
        expect(this.connection.getResponseCode()).andReturn(200);
        replay(model, this.connectionFactory, this.connection);
        assertEquals("redirect:/index.html", this.controller.formSubmitLanelibacqs(model, null));
        assertEquals("foo=bar&id=", new String(baos.toByteArray()));
        verify(model, this.connectionFactory, this.connection);
    }
}

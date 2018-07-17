package edu.stanford.irt.laneweb.crm;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.crm.HTTPCRMService.URLConnectionFactory;

public class HTTPCRMServiceTest {

    private HttpsURLConnection connection;

    private URLConnectionFactory connectionFactory;

    private HTTPCRMService service;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.connectionFactory = mock(URLConnectionFactory.class);
        this.uri = new URI("/");
        this.service = new HTTPCRMService(this.uri, this.connectionFactory);
        this.connection = mock(HttpsURLConnection.class);
    }

    @Test
    public void testJsonSubmitLanelibacqs() throws IOException {
        expect(this.connectionFactory.getConnection(this.uri)).andReturn(this.connection);
        this.connection.setDoOutput(true);
        expect(this.connection.getOutputStream()).andReturn(new ByteArrayOutputStream());
        expect(this.connection.getResponseCode()).andReturn(200);
        replay(this.connectionFactory, this.connection);
        assertEquals(200, this.service.submitRequest(Collections.singletonMap("foo", "bar")));
        verify(this.connectionFactory, this.connection);
    }
}

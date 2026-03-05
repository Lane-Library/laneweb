package edu.stanford.irt.laneweb.resource;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.rest.RESTService;

public class BannerResourceOutageServiceTest {

    private static final URI BASE_URI = URI.create("https://lanestanford.libanswers.com/systems/posts");

    private RESTService restService;

    private BannerResourceOutageService service;

    @BeforeEach
    public void setUp() {
        this.restService = mock(RESTService.class);
        this.service = new BannerResourceOutageService(BASE_URI, this.restService);
    }

    @Test
    public void testGetHtmlResourceOutages() throws Exception {
        String html = "<div class=\"outages\">ok</div>";
        String json = "{\"main\":\"" + html.replace("\"", "\\\"") + "\"}";

        URI expected = new URI(BASE_URI.toString() + "?m=statusposts");
        expect(this.restService.getObject(eq(expected), same(String.class))).andReturn(json);
        replay(this.restService);

        ByteArrayInputStream inputStream = this.service.getHtmlResourceOutages();
        assertNotNull(inputStream);
        assertEquals(html, new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));

        verify(this.restService);
    }

    @Test
    public void testGetHtmlResourceOutagesMissingMainReturnsEmptyStream() throws Exception {
        String json = "{\"other\":\"value\"}";

        URI expected = new URI(BASE_URI.toString() + "?m=statusposts");
        expect(this.restService.getObject(eq(expected), same(String.class))).andReturn(json);
        replay(this.restService);

        ByteArrayInputStream inputStream = this.service.getHtmlResourceOutages();
        assertNotNull(inputStream);
        assertEquals("", new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));

        verify(this.restService);
    }

    @Test
    public void testGetHtmlResourceOutagesNullJsonReturnsEmptyStream() throws Exception {
        String json = "null";

        URI expected = new URI(BASE_URI.toString() + "?m=statusposts");
        expect(this.restService.getObject(eq(expected), same(String.class))).andReturn(json);
        replay(this.restService);
        ByteArrayInputStream inputStream = this.service.getHtmlResourceOutages();
        assertNotNull(inputStream);
        assertEquals("", new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));

        verify(this.restService);
    }

    @Test
    public void testGetHtmlResourceOutagesInvalidJsonThrows() throws Exception {
        String json = "{not valid json";

        URI expected = new URI(BASE_URI.toString() + "?m=statusposts");
        expect(this.restService.getObject(eq(expected), same(String.class))).andReturn(json);
        replay(this.restService);

        assertThrows(RuntimeException.class, () -> this.service.getHtmlResourceOutages());

        verify(this.restService);
    }

}

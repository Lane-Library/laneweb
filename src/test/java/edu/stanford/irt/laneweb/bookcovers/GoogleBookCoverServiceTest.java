package edu.stanford.irt.laneweb.bookcovers;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.util.ObjectParser;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volume.VolumeInfo;
import com.google.api.services.books.model.Volume.VolumeInfo.ImageLinks;
import com.google.api.services.books.model.Volumes;

import edu.stanford.irt.laneweb.LanewebException;

public class GoogleBookCoverServiceTest {

    private HttpTransport httpTransport;

    private ISBNService isbnService;

    private ObjectParser objectParser;

    private LowLevelHttpRequest request;

    private LowLevelHttpResponse response;

    private GoogleBookCoverService service;

    private Volumes volumes;

    @Before
    public void setUp() {
        this.isbnService = createMock(ISBNService.class);
        this.request = createMock(LowLevelHttpRequest.class);
        this.httpTransport = new HttpTransport() {

            @Override
            protected LowLevelHttpRequest buildRequest(final String method, final String url) throws IOException {
                return GoogleBookCoverServiceTest.this.request;
            }
        };
        this.objectParser = createMock(ObjectParser.class);
        this.service = new GoogleBookCoverService(this.isbnService, this.httpTransport, this.objectParser, "apiKey",
                new HashMap<Integer, Optional<String>>());
        this.response = createMock(LowLevelHttpResponse.class);
        this.volumes = new Volumes().setTotalItems(1).setItems(Collections.singletonList(new Volume()
                .setVolumeInfo(new VolumeInfo().setImageLinks(new ImageLinks().setSmallThumbnail("thumbnail")))));
    }

    @Test
    public void testGetThumbnailURLs() throws IOException {
        expect(this.isbnService.getISBNs(isA(List.class))).andReturn(Collections.singletonMap(Integer.valueOf(1),
                Arrays.asList(new String[] { "9780679732761", "0738531367" })));
        this.request.addHeader("Accept-Encoding", "gzip");
        this.request.addHeader("User-Agent", "Google-HTTP-Java-Client/1.21.0 (gzip)");
        this.request.setTimeout(20000, 20000);
        expect(this.request.execute()).andReturn(this.response);
        expect(this.response.getContentEncoding()).andReturn("foo");
        expect(this.response.getStatusCode()).andReturn(200);
        expect(this.response.getReasonPhrase()).andReturn("OK");
        expect(this.response.getHeaderCount()).andReturn(0);
        expect(this.response.getContentType()).andReturn("application/json");
        expect(this.response.getContent()).andReturn(getClass().getResourceAsStream("response.txt"));
        this.response.disconnect();
        expect(this.objectParser.parseAndClose(isA(InputStream.class), eq(StandardCharsets.UTF_8), eq(Volumes.class)))
                .andReturn(this.volumes);
        expectLastCall().times(2);
        replay(this.isbnService, this.request, this.objectParser, this.response);
        Map<Integer, String> thumbnailURLS = this.service.getBookCoverURLs(Collections.singletonList(1));
        assertEquals("thumbnail", thumbnailURLS.get(Integer.valueOf(1)));
        verify(this.isbnService, this.request, this.objectParser, this.response);
    }

    @Test
    public void testGetThumbnailURLsEmptyBatch() throws IOException {
        expect(this.isbnService.getISBNs(isA(List.class)))
                .andReturn(Collections.singletonMap(Integer.valueOf(1), Collections.emptyList()));
        replay(this.isbnService, this.request, this.objectParser, this.response);
        Map<Integer, String> result = this.service.getBookCoverURLs(Collections.singletonList(1));
        assertTrue(result.containsKey(1));
        assertNull(result.get(1));
        verify(this.isbnService, this.request, this.objectParser, this.response);
    }

    @Test
    public void testGetThumbnailURLsEmptyList() throws IOException {
        replay(this.isbnService, this.request, this.objectParser, this.response);
        assertTrue(this.service.getBookCoverURLs(Collections.emptyList()).isEmpty());
        verify(this.isbnService, this.request, this.objectParser, this.response);
    }

    @Test
    public void testGetThumbnailURLsThrows() throws IOException {
        expect(this.isbnService.getISBNs(isA(List.class))).andReturn(Collections.singletonMap(Integer.valueOf(1),
                Arrays.asList(new String[] { "9780679732761", "0738531367" })));
        this.request.addHeader("Accept-Encoding", "gzip");
        this.request.addHeader("User-Agent", "Google-HTTP-Java-Client/1.21.0 (gzip)");
        this.request.setTimeout(20000, 20000);
        expect(this.request.execute()).andThrow(new IOException());
        replay(this.isbnService, this.request, this.objectParser, this.response);
        try {
            this.service.getBookCoverURLs(Collections.singletonList(1));
        } catch (LanewebException e) {
        }
        verify(this.isbnService, this.request, this.objectParser, this.response);
    }
}

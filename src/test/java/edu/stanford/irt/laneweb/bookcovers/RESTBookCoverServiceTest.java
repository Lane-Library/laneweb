package edu.stanford.irt.laneweb.bookcovers;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;
import edu.stanford.irt.status.ApplicationStatus;

public class RESTBookCoverServiceTest {

    private RESTService restService;

    private RESTBookCoverService service;

    private ApplicationStatus status;

    private URI uri;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.service = new RESTBookCoverService(this.uri, this.restService);
        this.status = mock(ApplicationStatus.class);
    }

    @Test
    public void testGetBookCoverURLs() throws RestClientException, URISyntaxException {
        expect(this.restService.getObject(eq(new URI("/4/bookcovers?resourceIds=bib-12")), isA(TypeReference.class)))
                .andReturn(Collections.emptyMap());
        replay(this.restService);
        assertSame(Collections.emptyMap(), this.service.getBookCoverURLs(Collections.singletonList("bib-12")));
        verify(this.restService);
    }

    @Test
    public void testGetStatus() throws RestClientException, URISyntaxException {
        expect(this.restService.getObject(eq(new URI("/status.json")), same(ApplicationStatus.class)))
                .andReturn(this.status);
        replay(this.restService);
        assertSame(this.status, this.service.getStatus());
        verify(this.restService);
    }
}

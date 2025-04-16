package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;
import edu.stanford.irt.status.ApplicationStatus;

public class RESTBookmarkServiceTest {

    private RESTBookmarkService bookmarkService;

    private RESTService restService;

    private URI uri;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.bookmarkService = new RESTBookmarkService(this.uri, this.restService);
    }

    @Test
    public void testGetLinks() {
        expect(this.restService.getObject(eq(this.uri.resolve("userid")), isA(TypeReference.class)))
                .andReturn(Collections.emptyList());
        replay(this.restService);
        assertSame(Collections.emptyList(), this.bookmarkService.getLinks("userid"));
        verify(this.restService);
    }

    @Test()
    public void testGetLinksThrows() {
        expect(this.restService.getObject(eq(this.uri.resolve("userid")), isA(TypeReference.class)))
                .andThrow(new RESTException(new IOException()));
        replay(this.restService);
        assertThrows(BookmarkException.class, () -> {
            this.bookmarkService.getLinks("userid");
        });

    }

    @Test
    public void testGetRowCount() {
        expect(this.restService.getObject(this.uri.resolve("rowcount"), Integer.class)).andReturn(Integer.valueOf(1));
        replay(this.restService);
        assertEquals(1, this.bookmarkService.getRowCount());
        verify(this.restService);
    }

    @Test()
    public void testGetRowCountThrows() {
        expect(this.restService.getObject(this.uri.resolve("rowcount"), Integer.class))
                .andThrow(new RESTException(new IOException()));
        replay(this.restService);
        assertThrows(BookmarkException.class, () -> {
            this.bookmarkService.getRowCount();
        });
    }

    @Test
    public void testGetStatus() {
        ApplicationStatus status = mock(ApplicationStatus.class);
        expect(this.restService.getObject(this.uri.resolve("status.json"), ApplicationStatus.class)).andReturn(status);
        replay(this.restService);
        assertSame(status, this.bookmarkService.getStatus());
        verify(this.restService);
    }

    @Test
    public void testSaveLinks() {
        this.restService.putObject(this.uri.resolve("userid"), Collections.emptyList());
        replay(this.restService);
        this.bookmarkService.saveLinks("userid", Collections.emptyList());
        verify(this.restService);
    }

    @Test()
    public void testSaveLinksThrows() {
        this.restService.putObject(this.uri.resolve("userid"), Collections.emptyList());
        expectLastCall().andThrow(new RESTException(new IOException()));
        replay(this.restService);
        assertThrows(BookmarkException.class, () -> {
            this.bookmarkService.saveLinks("userid", Collections.emptyList());
        });
    }
}

package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.status.ApplicationStatus;

public class StanfordDomainStrippingBookmarkServiceTest {

    private StanfordDomainStrippingBookmarkService service;

    private BookmarkService wrappedService;

    @Before
    public void setUp() throws Exception {
        this.wrappedService = mock(BookmarkService.class);
        this.service = new StanfordDomainStrippingBookmarkService(this.wrappedService);
    }

    @Test
    public void testGetLinks() {
        expect(this.wrappedService.getLinks("id@domain")).andReturn(null);
        replay(this.wrappedService);
        assertNull(this.service.getLinks("id@domain"));
        verify(this.wrappedService);
    }

    @Test
    public void testGetLinksStanfordDomain() {
        expect(this.wrappedService.getLinks("id")).andReturn(null);
        replay(this.wrappedService);
        assertNull(this.service.getLinks("id@stanford.edu"));
        verify(this.wrappedService);
    }

    @Test
    public void testGetRowCount() {
        expect(this.wrappedService.getRowCount()).andReturn(0);
        replay(this.wrappedService);
        assertEquals(0, this.service.getRowCount());
        verify(this.wrappedService);
    }

    @Test
    public void testGetStatus() {
        ApplicationStatus status = mock(ApplicationStatus.class);
        expect(this.wrappedService.getStatus()).andReturn(status);
        replay(this.wrappedService);
        assertSame(status, this.service.getStatus());
        verify(this.wrappedService);
    }

    @Test
    public void testSaveLinks() {
        this.wrappedService.saveLinks("id@domain", null);
        replay(this.wrappedService);
        this.service.saveLinks("id@domain", null);
        verify(this.wrappedService);
    }

    @Test
    public void testSaveLinksStanfordDomain() {
        this.wrappedService.saveLinks("id", null);
        replay(this.wrappedService);
        this.service.saveLinks("id@stanford.edu", null);
        verify(this.wrappedService);
    }
}

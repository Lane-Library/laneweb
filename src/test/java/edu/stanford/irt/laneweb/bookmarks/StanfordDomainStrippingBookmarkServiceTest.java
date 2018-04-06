package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class StanfordDomainStrippingBookmarkServiceTest {

    private StanfordDomainStrippingBookmarkService bookmarkDAO;

    private BookmarkService wrappedDAO;

    @Before
    public void setUp() throws Exception {
        this.wrappedDAO = mock(BookmarkService.class);
        this.bookmarkDAO = new StanfordDomainStrippingBookmarkService(this.wrappedDAO);
    }

    @Test
    public void testGetLinks() {
        expect(this.wrappedDAO.getLinks("id@domain")).andReturn(null);
        replay(this.wrappedDAO);
        assertNull(this.bookmarkDAO.getLinks("id@domain"));
        verify(this.wrappedDAO);
    }

    @Test
    public void testGetLinksStanfordDomain() {
        expect(this.wrappedDAO.getLinks("id")).andReturn(null);
        replay(this.wrappedDAO);
        assertNull(this.bookmarkDAO.getLinks("id@stanford.edu"));
        verify(this.wrappedDAO);
    }

    @Test
    public void testGetRowCount() {
        expect(this.wrappedDAO.getRowCount()).andReturn(0);
        replay(this.wrappedDAO);
        assertEquals(0, this.bookmarkDAO.getRowCount());
        verify(this.wrappedDAO);
    }

    @Test
    public void testSaveLinks() {
        this.wrappedDAO.saveLinks("id@domain", null);
        replay(this.wrappedDAO);
        this.bookmarkDAO.saveLinks("id@domain", null);
        verify(this.wrappedDAO);
    }

    @Test
    public void testSaveLinksStanfordDomain() {
        this.wrappedDAO.saveLinks("id", null);
        replay(this.wrappedDAO);
        this.bookmarkDAO.saveLinks("id@stanford.edu", null);
        verify(this.wrappedDAO);
    }
}

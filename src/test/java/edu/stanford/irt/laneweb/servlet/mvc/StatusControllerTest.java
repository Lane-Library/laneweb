package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookcovers.BookCoverService;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public class StatusControllerTest {

    private ApplicationStatus applicationStatus;

    private BookCoverService bookCoverService;

    private BookmarkService bookmarkService;

    private StatusController controller;

    private StatusService statusService;

    @Before
    public void setUp() {
        this.statusService = strictMock(StatusService.class);
        this.bookCoverService = strictMock(BookCoverService.class);
        this.bookmarkService = strictMock(BookmarkService.class);
        this.controller = new StatusController(this.statusService, this.bookCoverService, this.bookmarkService);
        this.applicationStatus = strictMock(ApplicationStatus.class);
    }

    @Test
    public void testGetStatusJson() {
        expect(this.bookCoverService.getStatus()).andReturn(this.applicationStatus);
        expect(this.bookmarkService.getStatus()).andReturn(this.applicationStatus);
        expect(this.statusService.getApplicationStatus(Arrays.asList(this.applicationStatus, this.applicationStatus)))
                .andReturn(this.applicationStatus);
        replay(this.statusService, this.bookCoverService, this.bookmarkService);
        assertSame(this.applicationStatus, this.controller.getStatusJson());
        verify(this.statusService, this.bookCoverService, this.bookmarkService);
    }

    @Test
    public void testGetStatusTxt() {
        expect(this.bookmarkService.getStatus()).andReturn(this.applicationStatus);
        expect(this.bookCoverService.getStatus()).andReturn(this.applicationStatus);
        expect(this.statusService.getApplicationStatus(Arrays.asList(this.applicationStatus, this.applicationStatus)))
                .andReturn(this.applicationStatus);
        replay(this.statusService, this.bookCoverService, this.bookmarkService);
        assertEquals(this.applicationStatus.toString(), this.controller.getStatusTxt());
        verify(this.statusService, this.bookCoverService, this.bookmarkService);
    }
}

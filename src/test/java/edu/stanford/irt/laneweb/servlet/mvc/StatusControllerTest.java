package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookcovers.BookCoverService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public class StatusControllerTest {

    private ApplicationStatus applicationStatus;

    private BookCoverService bookCoverService;

    private StatusController controller;

    private StatusService statusService;

    @Before
    public void setUp() {
        this.statusService = strictMock(StatusService.class);
        this.bookCoverService = strictMock(BookCoverService.class);
        this.controller = new StatusController(this.statusService, this.bookCoverService);
        this.applicationStatus = strictMock(ApplicationStatus.class);
    }

    @Test
    public void testGetStatusJson() {
        expect(this.bookCoverService.getStatus()).andReturn(this.applicationStatus);
        expect(this.statusService.getApplicationStatus(Collections.singletonList(this.applicationStatus)))
                .andReturn(this.applicationStatus);
        replay(this.statusService, this.bookCoverService);
        assertSame(this.applicationStatus, this.controller.getStatusJson());
        verify(this.statusService, this.bookCoverService);
    }

    @Test
    public void testGetStatusTxt() {
        expect(this.bookCoverService.getStatus()).andReturn(this.applicationStatus);
        expect(this.statusService.getApplicationStatus(Collections.singletonList(this.applicationStatus)))
                .andReturn(this.applicationStatus);
        replay(this.statusService, this.bookCoverService);
        assertEquals(this.applicationStatus.toString(), this.controller.getStatusTxt());
        verify(this.statusService, this.bookCoverService);
    }
}

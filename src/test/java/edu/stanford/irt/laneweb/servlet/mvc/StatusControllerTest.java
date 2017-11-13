package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.status.ApplicationStatus;
import edu.stanford.irt.laneweb.status.StatusService;

public class StatusControllerTest {

    private ApplicationStatus applicationStatus;

    private StatusController controller;

    private StatusService statusService;

    @Before
    public void setUp() {
        this.statusService = strictMock(StatusService.class);
        this.controller = new StatusController(this.statusService);
        this.applicationStatus = strictMock(ApplicationStatus.class);
    }

    @Test
    public void testGetStatusJson() {
        expect(this.statusService.getApplicationStatus()).andReturn(this.applicationStatus);
        replay(this.statusService);
        assertSame(this.applicationStatus, this.controller.getStatusJson());
        verify(this.statusService);
    }

    @Test
    public void testGetStatusTxt() {
        expect(this.statusService.getApplicationStatus()).andReturn(this.applicationStatus);
        replay(this.statusService);
        assertEquals(this.applicationStatus.toString(), this.controller.getStatusTxt());
        verify(this.statusService);
    }
}

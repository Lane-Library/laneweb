package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.status.LanewebStatusService;
import edu.stanford.irt.status.ApplicationStatus;

public class StatusControllerTest {

    private ApplicationStatus applicationStatus;

    private StatusController controller;

    private LanewebStatusService statusService;

    @BeforeEach
    public void setUp() {
        this.statusService = strictMock(LanewebStatusService.class);
        this.controller = new StatusController(this.statusService);
        this.applicationStatus = strictMock(ApplicationStatus.class);
    }

    @Test
    public void testGetStatusJson() {
        expect(this.statusService.getStatus()).andReturn(Collections.singletonList(this.applicationStatus));
        replay(this.statusService);
        assertEquals(Collections.singletonList(this.applicationStatus), this.controller.getStatusJson());
        verify(this.statusService);
    }

    @Test
    public void testGetStatusTxt() {
        expect(this.statusService.getStatus()).andReturn(Collections.singletonList(this.applicationStatus));
        replay(this.statusService);
        assertEquals(Collections.singletonList(this.applicationStatus).get(0).toString(),
                this.controller.getStatusTxt());
        verify(this.statusService);
    }
}

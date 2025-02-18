package edu.stanford.irt.laneweb.status;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusService;

public class LanewebStatusServiceTest {

    private ApplicationStatus applicationStatus;

    private LanewebStatusService service;

    private StatusService statusService;

    @BeforeEach
    public void setUp() {
        this.statusService = mock(StatusService.class);
        this.service = new LanewebStatusService(Collections.singletonList(this.statusService));
        this.applicationStatus = mock(ApplicationStatus.class);
    }

    @Test
    public void testGetStatus() {
        expect(this.statusService.getStatus()).andReturn(this.applicationStatus);
        replay(this.statusService);
        assertSame(this.applicationStatus, this.service.getStatus().get(0));
        verify(this.statusService);
    }

    @Test
    public void testGetStatusThrows() {
        expect(this.statusService.getStatus()).andThrow(new RESTException(new IOException("oopsie")));
        replay(this.statusService);
        ApplicationStatus status = this.service.getStatus().get(0);
        assertEquals("unknown", status.getHost());
        assertSame(Status.ERROR, status.getItems().get(0).getStatus());
        assertEquals(this.statusService.getClass().getName(), status.getName());
        assertEquals(-1, status.getPid());
        assertEquals("unknown", status.getVersion());
        verify(this.statusService);
    }
}

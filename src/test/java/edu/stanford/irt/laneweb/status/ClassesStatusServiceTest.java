package edu.stanford.irt.laneweb.status;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.status.ApplicationStatus;

public class ClassesStatusServiceTest {

    private ApplicationStatus applicationStatus;

    private RESTService restService;

    private ClassesStatusService service;

    private URI statusUri;

    @BeforeEach
    public void setUp() throws Exception {
        this.statusUri = new URI("/");
        this.restService = mock(RESTService.class);
        this.service = new ClassesStatusService(this.statusUri, this.restService);
        this.applicationStatus = mock(ApplicationStatus.class);
    }

    @Test
    public void testGetStatus() throws Exception {
        expect(this.restService.getObject(eq(new URI("/status.json")), same(ApplicationStatus.class)))
                .andReturn(this.applicationStatus);
        replay(this.restService);
        assertSame(this.applicationStatus, this.service.getStatus());
        verify(this.restService);
    }
}

package edu.stanford.irt.laneweb.catalog;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.status.ApplicationStatus;

public class CatalogStatusServiceTest {

    private BasicAuthRESTService restService;

    private CatalogStatusService service;

    private ApplicationStatus status;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        this.restService = mock(BasicAuthRESTService.class);
        this.service = new CatalogStatusService(new URI("/"), this.restService);
    }

    @Test
    public void testGetStatus() throws RESTException, URISyntaxException {
        expect(this.restService.getObject(new URI("/status.json"), ApplicationStatus.class)).andReturn(this.status);
        replay(this.restService);
        assertSame(this.status, this.service.getStatus());
        verify(this.restService);
    }

    @Test
    public void testGetStatusThrows() throws RESTException, URISyntaxException {
        expect(this.restService.getObject(new URI("/status.json"), ApplicationStatus.class))
                .andThrow(new RESTException(new IOException("oopsie")));
        replay(this.restService);
        assertThrows(RESTException.class, () -> {
            this.service.getStatus();
        });
    }
}

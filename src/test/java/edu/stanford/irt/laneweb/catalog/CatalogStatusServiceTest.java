package edu.stanford.irt.laneweb.catalog;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.status.ApplicationStatus;

public class CatalogStatusServiceTest {

    private BasicAuthRESTService restService;

    private CatalogStatusService service;

    private ApplicationStatus status;

    @Before
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

    @Test(expected = RESTException.class)
    public void testGetStatusThrows() throws RESTException, URISyntaxException {
        expect(this.restService.getObject(new URI("/status.json"), ApplicationStatus.class))
                .andThrow(new RESTException(new IOException("oopsie")));
        replay(this.restService);
        this.service.getStatus();
    }
}

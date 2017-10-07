package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.libraryhours.LibraryHoursService;

public class BindingConfigurationTest {

    private BindingConfiguration configuration;

    private ServletContext servletContext;

    @Before
    public void setUp() throws MalformedURLException {
        this.servletContext = createMock(ServletContext.class);
        this.configuration = new BindingConfiguration();
    }

    @Test
    public void testBasePathDataBinder() {
        assertNotNull(this.configuration.basePathDataBinder(this.servletContext));
    }

    @Test
    public void testBaseProxyUrlDataBinder() {
        assertNotNull(this.configuration.baseProxyUrlDataBinder());
    }

    @Test
    public void testContentBaseDataBinder() throws URISyntaxException {
        assertNotNull(this.configuration.contentBaseDataBinder(new URI("file:/")));
    }

    @Test
    public void testDataBinder() throws URISyntaxException {
        assertNotNull(this.configuration.dataBinder(null, null, null, null, null, null, null, null, null, null));
    }

    @Test
    public void testDisasterModeDataBinder() {
        assertNotNull(this.configuration.disasterModeDataBinder(null));
    }

    @Test
    public void testEmridDataBinder() {
        assertNotNull(this.configuration.emridDataBinder());
    }

    @Test
    public void testModelDataBinder() {
        assertNotNull(this.configuration.modelDataBinder(null));
    }

    @Test
    public void testProxyLinks() {
        assertNotNull(this.configuration.proxyLinks());
    }

    @Test
    public void testRemoteProxyIPDataBinder() {
        assertNotNull(this.configuration.remoteProxyIPDataBinder());
    }

    @Test
    public void testTemplateChooser() {
        assertNotNull(this.configuration.templateChooser());
    }

    @Test
    public void testTemplateDataBinder() {
        assertNotNull(this.configuration.templateDataBinder());
    }

    @Test
    public void testTicketDataBinder() {
        assertNotNull(this.configuration.ticketDataBinder(null));
    }

    @Test
    public void testTodaysHours() {
        LibraryHoursService hoursService = createMock(LibraryHoursService.class);
        assertNotNull(this.configuration.todaysHours(hoursService));
    }

    @Test
    public void testTodaysHoursDataBinder() {
        assertNotNull(this.configuration.todaysHoursDataBinder(null));
    }

    @Test
    public void testUserCookieCodec() {
        assertNotNull(this.configuration.userCookieCodec(""));
    }

    @Test
    public void testUserDataBinder() {
        assertNotNull(this.configuration.userDataBinder(null, null));
    }

    @Test
    public void testUserDataDataBinder() {
        assertNotNull(this.configuration.userDataDataBinder(null, null, null, null, null, null));
    }

    @Test
    public void testVersionDataBinder() {
        assertNotNull(this.configuration.versionDataBinder(null));
    }
}

package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

public class BindingConfigurationTest {

    private BindingConfiguration configuration;

    private ServletContext servletContext;

    @Before
    public void setUp() throws MalformedURLException {
        this.servletContext = createMock(ServletContext.class);
        this.configuration = new BindingConfiguration("userCookieKey", new URL("file:/"), "version", this.servletContext);
    }

    @Test
    public void testBasePathDataBinder() {
        assertNotNull(this.configuration.basePathDataBinder());
    }

    @Test
    public void testBaseProxyUrlDataBinder() {
        assertNotNull(this.configuration.baseProxyUrlDataBinder());
    }

    @Test
    public void testContentBaseDataBinder() {
        assertNotNull(this.configuration.contentBaseDataBinder());
    }

    @Test
    public void testDataBinder() {
        expect(this.servletContext.getContextPath()).andReturn("/path");
        replay(this.servletContext);
        assertNotNull(this.configuration.dataBinder());
        verify(this.servletContext);
    }

    @Test
    public void testDebugDataBinder() {
        assertNotNull(this.configuration.debugDataBinder());
    }

    @Test
    public void testDisasterModeDataBinder() {
        assertNotNull(this.configuration.disasterModeDataBinder());
    }

    @Test
    public void testEmridDataBinder() {
        assertNotNull(this.configuration.emridDataBinder());
    }

    @Test
    public void testLdapDataBinder() {
        assertNotNull(this.configuration.ldapDataBinder());
    }

    @Test
    public void testLiveChatScheduleDataBinder() {
        assertNotNull(this.configuration.liveChatScheduleDataBinder());
    }

    @Test
    public void testModelDataBinder() {
        assertNotNull(this.configuration.modelDataBinder());
    }

    @Test
    public void testParameterMapDataBinder() {
        assertNotNull(this.configuration.parameterMapDataBinder());
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
    public void testRequestHeaderDataBinder() {
        assertNotNull(this.configuration.requestHeaderDataBinder());
    }

    @Test
    public void testRequestMethodDataBinder() {
        assertNotNull(this.configuration.requestMethodDataBinder());
    }

    @Test
    public void testRequestParameterDataBinder() {
        assertNotNull(this.configuration.requestParameterDataBinder());
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
        assertNotNull(this.configuration.ticketDataBinder());
    }

    @Test
    public void testTodaysHours() {
        assertNotNull(this.configuration.todaysHours());
    }

    @Test
    public void testTodaysHoursDataBinder() {
        assertNotNull(this.configuration.todaysHoursDataBinder());
    }

    @Test
    public void testUserCookieCodec() {
        assertNotNull(this.configuration.userCookieCodec());
    }

    @Test
    public void testUserCookieDataBinder() {
        assertNotNull(this.configuration.userCookieDataBinder());
    }

    @Test
    public void testUserDataBinder() {
        assertNotNull(this.configuration.userDataBinder());
    }

    @Test
    public void testUserDataDataBinder() {
        assertNotNull(this.configuration.userDataDataBinder());
    }

    @Test
    public void testVersionDataBinder() {
        assertNotNull(this.configuration.versionDataBinder());
    }
}

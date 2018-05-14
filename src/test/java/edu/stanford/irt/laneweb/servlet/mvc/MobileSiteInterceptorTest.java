/**
 *
 */
package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author ryanmax
 */
public class MobileSiteInterceptorTest {

    private Map<String, String> desktopRedirects = Collections.emptyMap();

    private Device device;

    private MobileSiteInterceptor interceptor;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private SitePreferenceHandler sitePreferenceHandler;

    @Before
    public void setUp() {
        this.desktopRedirects = new HashMap<>();
        this.desktopRedirects.put("/index.html", "/m/index.html");
        this.desktopRedirects.put("/biomed-resources/eb.html", "/m/book.html");
        this.desktopRedirects.put("/biomed-resources/ej.html", "/m/ej.html");
        this.device = mock(Device.class);
        this.response = new MockHttpServletResponse();
        this.sitePreferenceHandler = mock(StandardSitePreferenceHandler.class);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler, this.desktopRedirects);
    }

    @Test
    public void testDesktopDeviceDesktopSiteMobilePreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setServletPath("/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/m/index.html", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void testDesktopDeviceDesktopSiteNoPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setServletPath("/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(null);
        replay(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void testDesktopDeviceDesktopSiteNormalPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setServletPath("/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.NORMAL);
        replay(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void testDesktopDeviceMobileSiteMobilePreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/m/index.html");
        this.request.setServletPath("/m/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void testDesktopDeviceMobileSiteNormalPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/m/index.html");
        this.request.setServletPath("/m/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.NORMAL);
        replay(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/help/m.html", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void testMobileDeviceDesktopSiteEbooksNoPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/biomed-resources/eb.html");
        this.request.setServletPath("/biomed-resources/eb.html");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(null);
        replay(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/m/book.html", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void testMobileDeviceDesktopSiteEjournalsNoPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/biomed-resources/ej.html");
        this.request.setServletPath("/biomed-resources/ej.html");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(null);
        replay(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/m/ej.html", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void testMobileDeviceDesktopSiteMobilePreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setServletPath("/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/m/index.html", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void testMobileDeviceDesktopSiteNoPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setServletPath("/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(null);
        replay(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/m/index.html", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void testMobileDeviceDesktopSiteNormalPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setServletPath("/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.NORMAL);
        replay(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void testMobileDeviceMobileSiteMobilePreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/m/index.html");
        this.request.setServletPath("/m/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void testMobileDeviceMobileSiteNormalPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/m/index.html");
        this.request.setServletPath("/m/index.html");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.NORMAL);
        replay(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/help/m.html", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void testNonHtmlRequest() throws Exception {
        this.device = null;
        this.request = new MockHttpServletRequest("GET", "/path/foo.gif");
        this.request.setServletPath("/path/foo.gif");
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void testSecureRequest() throws Exception {
        this.device = null;
        this.request = new MockHttpServletRequest("GET", "/secure/foo.html");
        this.request.setServletPath("/secure/foo.html");
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response))
                .andReturn(SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }
}

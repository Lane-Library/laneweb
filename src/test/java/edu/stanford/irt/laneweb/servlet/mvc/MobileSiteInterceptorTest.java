/**
 * 
 */
package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import edu.stanford.irt.laneweb.model.Model;

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

    private Device device;

    private MobileSiteInterceptor interceptor;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private SitePreferenceHandler sitePreferenceHandler;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.device = createMock(Device.class);
        this.response = new MockHttpServletResponse();
        this.sitePreferenceHandler = createMock(StandardSitePreferenceHandler.class);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
    }
    
    @Test
    public void desktopDeviceMobileSiteMobilePreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/m/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void desktopDeviceMobileSiteNormalPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/m/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.NORMAL);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/help/m.html", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void desktopDeviceDesktopSiteMobilePreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/m/", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void desktopDeviceDesktopSiteNoPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(null);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void desktopDeviceDesktopSiteNormalPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.FALSE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.NORMAL);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void mobileDeviceMobileSiteMobilePreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/m/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void mobileDeviceMobileSiteNormalPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/m/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.NORMAL);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/help/m.html", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void mobileDeviceDesktopSiteMobilePreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/m/", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void mobileDeviceDesktopSiteNoPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(null);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        assertEquals("/m/", this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void mobileDeviceDesktopSiteNormalPreference() throws Exception {
        this.request = new MockHttpServletRequest("GET", "/index.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.device.isMobile()).andReturn(Boolean.TRUE);
        replay(this.device);
        this.request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, this.device);
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.NORMAL);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
        verify(this.device);
    }

    @Test
    public void nonHtmlRequest() throws Exception {
        this.device = null;
        this.request = new MockHttpServletRequest("GET", "/path/foo.gif");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }

    @Test
    public void secureRequest() throws Exception {
        this.device = null;
        this.request = new MockHttpServletRequest("GET", "/secure/foo.html");
        this.request.setAttribute(Model.BASE_PATH, "");
        expect(this.sitePreferenceHandler.handleSitePreference(this.request, this.response)).andReturn(
                SitePreference.MOBILE);
        replay(this.sitePreferenceHandler);
        this.interceptor = new MobileSiteInterceptor(this.sitePreferenceHandler);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        assertNull(this.response.getRedirectedUrl());
        verify(this.sitePreferenceHandler);
    }
    
}

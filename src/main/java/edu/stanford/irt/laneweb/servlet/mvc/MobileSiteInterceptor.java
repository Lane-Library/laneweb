package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor to switch between mobile and desktop sites. Based on Keith Donald's
 * org.springframework.mobile.device.switcher.SiteSwitcherHandlerInterceptor
 *
 * @author ryanmax
 */
public class MobileSiteInterceptor extends HandlerInterceptorAdapter {

    private static final String MOBILE_HELP_PATH = "/help/m.html";

    private static final String MOBILE_PATH = "/m/";

    private Map<String, String> desktopRedirectMap = Collections.emptyMap();

    private final SitePreferenceHandler sitePreferenceHandler;

    /**
     * Creates a new Interceptor with a StandardSitePreferenceHandler
     * 
     * @param desktopRedirects
     *            a Map of redirects
     */
    public MobileSiteInterceptor(final Map<String, String> desktopRedirects) {
        this(new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()), desktopRedirects);
    }

    /**
     * Creates a new site switcher.
     *
     * @param sitePreferenceHandler
     *            the handler for the user site preference
     * @param desktopRedirects
     *            a Map of redirects
     */
    public MobileSiteInterceptor(final SitePreferenceHandler sitePreferenceHandler,
            final Map<String, String> desktopRedirects) {
        this.sitePreferenceHandler = sitePreferenceHandler;
        this.desktopRedirectMap = desktopRedirects;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws IOException {
        SitePreference sitePreference = this.sitePreferenceHandler.handleSitePreference(request, response);
        String requestURI = request.getRequestURI();
        String basePath = request.getContextPath();
        if (requestURI.indexOf(MOBILE_PATH) > -1) {
            if (sitePreference == SitePreference.NORMAL) {
                response.sendRedirect(basePath + MOBILE_HELP_PATH);
                return false;
            }
        } else {
            for (Entry<String, String> entry : this.desktopRedirectMap.entrySet()) {
                if (requestURI.equals(basePath + entry.getKey())) {
                    Device device = DeviceUtils.getRequiredCurrentDevice(request);
                    if (sitePreference == SitePreference.MOBILE || device.isMobile() && sitePreference == null) {
                        response.sendRedirect(basePath + entry.getValue());
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
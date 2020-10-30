package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to switch between mobile and desktop sites. Based on Keith Donald's
 * org.springframework.mobile.device.switcher.SiteSwitcherHandlerInterceptor
 *
 * @author ryanmax
 */
public class MobileSiteInterceptor implements HandlerInterceptor {

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
        String servletPath = request.getServletPath();
        String basePath = request.getContextPath();
        if (servletPath.indexOf(MOBILE_PATH) > -1) {
            if (sitePreference == SitePreference.NORMAL) {
                response.sendRedirect(basePath + MOBILE_HELP_PATH);
                return false;
            }
        } else {
            if (this.desktopRedirectMap.containsKey(servletPath)) {
                Device device = DeviceUtils.getRequiredCurrentDevice(request);
                if (sitePreference == SitePreference.MOBILE || (device.isMobile() && sitePreference == null)) {
                    response.sendRedirect(basePath + this.desktopRedirectMap.get(servletPath));
                    return false;
                }
            }
        }
        return true;
    }
}

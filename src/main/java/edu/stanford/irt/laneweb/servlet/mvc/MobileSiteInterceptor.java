package edu.stanford.irt.laneweb.servlet.mvc;

import edu.stanford.irt.laneweb.model.Model;

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

    public static MobileSiteInterceptor slashM() {
        return new MobileSiteInterceptor(new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
    }

    private final SitePreferenceHandler sitePreferenceHandler;

    /**
     * Creates a new site switcher.
     * 
     * @param sitePreferenceHandler
     *            the handler for the user site preference
     */
    public MobileSiteInterceptor(final SitePreferenceHandler sitePreferenceHandler) {
        this.sitePreferenceHandler = sitePreferenceHandler;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        SitePreference sitePreference = this.sitePreferenceHandler.handleSitePreference(request, response);
        String requestURI = request.getRequestURI();
        String basePath = (String) request.getAttribute(Model.BASE_PATH);
        if (requestURI.endsWith(".html")) {
            if (requestURI.indexOf(MOBILE_PATH) > -1) {
                if (sitePreference == SitePreference.NORMAL) {
                    response.sendRedirect(basePath + MOBILE_HELP_PATH);
                    return false;
                }
            } else {
                Device device = DeviceUtils.getRequiredCurrentDevice(request);
                if (sitePreference == SitePreference.MOBILE || device.isMobile() && sitePreference == null) {
                    response.sendRedirect(basePath + MOBILE_PATH);
                    return false;
                }
            }
        }
        return true;
    }
}
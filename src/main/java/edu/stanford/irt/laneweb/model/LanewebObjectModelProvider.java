package edu.stanford.irt.laneweb.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.processing.ProcessInfoProvider;

import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;

/**
 * 
 * @author ceyates
 * $Id$
 */
public class LanewebObjectModelProvider implements ObjectModelProvider {

    private ProcessInfoProvider processInfoProvider;

    private ProxyLinks proxyLinks;

    private TemplateChooser templateChooser;

    private UserDao userDao;

    public LanewebObjectModelProvider(final ProcessInfoProvider pip, final UserDao userDao,
            final ProxyLinks proxyLinks, final TemplateChooser templateChooser) {
        this.processInfoProvider = pip;
        this.userDao = userDao;
        this.proxyLinks = proxyLinks;
        this.templateChooser = templateChooser;
    }

    @SuppressWarnings("unchecked")
    public Object getObject() {
        Map objectModel = this.processInfoProvider.getObjectModel();
        Map<String, Object> model = new HashMap<String, Object>();
        HttpServletRequest request = (HttpServletRequest) objectModel.get("httprequest");
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute(LanewebConstants.USER);
        if (null == user) {
            user = new User();
            session.setAttribute(LanewebConstants.USER, user);
        }
        this.userDao.getUserData(user, request);
        if (user.getSunetId() != null) {
            model.put("sunetid", user.getSunetId());
        }
        if (user.getIPGroup() != null) {
            model.put("ipgroup", user.getIPGroup());
        }
        if (user.getTicket() != null) {
            model.put("ticket", user.getTicket());
        }
        if (user.getEmrId() != null) {
            model.put("emrid", user.getEmrId());
        }
        if (user.getName() != null) {
            model.put("name", user.getName());
        }
        if (user.getUnivId() != null) {
            model.put("univid", user.getUnivId());
        }
        if (user.getAffiliation() != null) {
            model.put("affiliation", user.getAffiliation());
        }
        model.put("proxy-links", this.proxyLinks.proxyLinks(user, request));
        org.apache.cocoon.environment.Context context = ObjectModelHelper.getContext(objectModel);
        model.put("live-base", context.getAttribute("laneweb.context.live-base"));
        model.put("stage-base", context.getAttribute("laneweb.context.stage-base"));
        model.put("medblog-base", context.getAttribute("laneweb.context.medblog-base"));
        model.put("version", context.getAttribute("laneweb.context.version"));
        model.putAll(request.getParameterMap());
        if (request.getQueryString() != null) {
            model.put("query-string", request.getQueryString());
        }
        model.put("base-path", request.getContextPath());
        model.put("request-uri", request.getRequestURI());
        model.put("remote-host", request.getRemoteHost());
        if (request.getHeader("referer") != null) {
            model.put("referer", request.getHeader("referer"));
        }
        String query = request.getParameter("q");
        if (null != query) {
            try {
                model.put("url-encoded-query", URLEncoder.encode(query, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (LanewebConstants.LANE_COOKIE_NAME.equals(cookie.getName())) {
                    model.put("user-cookie", cookie.getValue());
                    break;
                }
            }
        }
        model.put("template", this.templateChooser.chooseTemplate(request));
        return model;
    }
}

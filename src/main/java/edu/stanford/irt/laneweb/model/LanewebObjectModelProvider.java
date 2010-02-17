package edu.stanford.irt.laneweb.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
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
            model.put(LanewebObjectModel.SUNETID, user.getSunetId());
        }
        if (user.getIPGroup() != null) {
            model.put(LanewebObjectModel.IPGROUP, user.getIPGroup());
        }
        if (user.getTicket() != null) {
            model.put(LanewebObjectModel.TICKET, user.getTicket());
        }
        if (user.getEmrId() != null) {
            model.put(LanewebObjectModel.EMRID, user.getEmrId());
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
        model.put(LanewebObjectModel.PROXY_LINKS, this.proxyLinks.proxyLinks(user, request));
        org.apache.cocoon.environment.Context context = ObjectModelHelper.getContext(objectModel);
        model.put("live-base", context.getAttribute("laneweb.context.live-base"));
        model.put("stage-base", context.getAttribute("laneweb.context.stage-base"));
        model.put("medblog-base", context.getAttribute("laneweb.context.medblog-base"));
        model.put("version", context.getAttribute("laneweb.context.version"));
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String name = (String) params.nextElement();
            if ("q".equals(name)) {
                String query = request.getParameter(name);
                model.put(LanewebObjectModel.QUERY, query);
                try {
                    model.put("url-encoded-query", URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else if ("t".equals(name)) {
                model.put(LanewebObjectModel.TYPE, request.getParameter(name));
            } else if ("s".equals(name)) {
                model.put(LanewebObjectModel.SUBSET, request.getParameter(name));
            } else if ("a".equals(name)) {
                model.put(LanewebObjectModel.ALPHA, request.getParameter(name).substring(0,1));
            } else if ("f".equals(name)) {
                model.put("facet", request.getParameter(name));
            } else if ("l".equals(name)) {
                model.put(LanewebObjectModel.LIMIT, request.getParameter(name));
            } else if ("bn".equals(name)) {
                model.put(LanewebObjectModel.BASSETT_NUMBER, request.getParameter(name));
            } else if ("r".equals(name)) {
                model.put("resources", request.getParameterValues(name));
            } else if ("e".equals(name)) {
                model.put("engines", request.getParameterValues(name));
            } else {
                model.put(name, request.getParameter(name));
            }
        }
        if (request.getQueryString() != null) {
            model.put("query-string", request.getQueryString());
        }
        model.put(LanewebObjectModel.BASE_PATH, request.getContextPath());
        model.put("request-uri", request.getRequestURI());
        model.put("remote-host", request.getRemoteHost());
        if (request.getHeader("referer") != null) {
            model.put("referer", request.getHeader("referer"));
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

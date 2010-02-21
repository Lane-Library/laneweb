package edu.stanford.irt.laneweb.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.processing.ProcessInfoProvider;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.ldap.LDAPData;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.proxy.Ticket;

/**
 * 
 * @author ceyates
 * $Id$
 */
public class LanewebObjectModelProvider implements ObjectModelProvider {

    private ProcessInfoProvider processInfoProvider;

    private ProxyLinks proxyLinks;

    private TemplateChooser templateChooser;

    private LDAPDataAccess lDAPDataAccess;

    private String ezproxyKey;

    public LanewebObjectModelProvider(final ProcessInfoProvider pip, final LDAPDataAccess lDAPDataAccess,
            final ProxyLinks proxyLinks, final TemplateChooser templateChooser, final String ezproxyKey) {
        this.processInfoProvider = pip;
        this.lDAPDataAccess = lDAPDataAccess;
        this.proxyLinks = proxyLinks;
        this.templateChooser = templateChooser;
        this.ezproxyKey = ezproxyKey;
    }

    @SuppressWarnings("unchecked")
    public Object getObject() {
        Map objectModel = this.processInfoProvider.getObjectModel();
        Map<String, Object> model = new HashMap<String, Object>();
        HttpServletRequest request = (HttpServletRequest) objectModel.get("httprequest");
        HttpSession session = request.getSession(true);
        //get the sunet id if set in SunetIdFilter and add it to the session and model
        String sunetid = (String) request.getAttribute(LanewebObjectModel.SUNETID);
        if (sunetid != null) {
            model.put(LanewebObjectModel.SUNETID, sunetid);
            session.setAttribute(LanewebObjectModel.SUNETID, sunetid);
        }
        Boolean proxyLinks = Boolean.parseBoolean(request.getParameter(LanewebObjectModel.PROXY_LINKS));
        if (proxyLinks == null) {
            proxyLinks = (Boolean) session.getAttribute(LanewebObjectModel.PROXY_LINKS);
            if (proxyLinks == null) {
                proxyLinks = this.proxyLinks.proxyLinks(request);
                session.setAttribute(LanewebObjectModel.PROXY_LINKS, proxyLinks);
            }
        }
        model.put(LanewebObjectModel.PROXY_LINKS, proxyLinks);
        IPGroup ipGroup = (IPGroup) session.getAttribute(LanewebObjectModel.IPGROUP);
        if (ipGroup == null) {
            ipGroup = IPGroup.getGroupForIP(request.getRemoteAddr());
            session.setAttribute(LanewebObjectModel.IPGROUP, ipGroup);
        }
        model.put(LanewebObjectModel.IPGROUP, ipGroup);
        String emrid = (String) session.getAttribute(LanewebObjectModel.EMRID);
        if (emrid == null) {
            emrid = request.getParameter(LanewebObjectModel.EMRID);
            if (emrid != null) {
                session.setAttribute(LanewebObjectModel.EMRID, emrid);
                model.put(LanewebObjectModel.EMRID, emrid);
            }
        }
        if (proxyLinks && sunetid != null && (ipGroup != IPGroup.SHC && ipGroup != IPGroup.LPCH)) {
            Ticket ticket = (Ticket) session.getAttribute(LanewebObjectModel.TICKET);
            if (!ticket.isValid()) {
                ticket = new Ticket(sunetid, this.ezproxyKey);
            }
            model.put(LanewebObjectModel.TICKET, ticket);
        }
        LDAPData lDAPData = (LDAPData) session.getAttribute(LanewebConstants.USER);
        if (null == lDAPData) {
            lDAPData = new LDAPData();
            session.setAttribute(LanewebConstants.USER, lDAPData);
        }
        this.lDAPDataAccess.getUserData(lDAPData, request);
        if (lDAPData.getName() != null) {
            model.put("name", lDAPData.getName());
        }
        if (lDAPData.getUnivId() != null) {
            model.put("univid", lDAPData.getUnivId());
        }
        if (lDAPData.getAffiliation() != null) {
            model.put("affiliation", lDAPData.getAffiliation());
        }
        model.put(LanewebObjectModel.PROXY_LINKS, this.proxyLinks.proxyLinks(request));
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
            } else if ("m".equals(name)) {
                model.put(LanewebObjectModel.MESH, request.getParameter(name));
            } else if ("f".equals(name)) {
                model.put(LanewebObjectModel.FACETS, request.getParameter(name));
            } else if ("l".equals(name)) {
                model.put(LanewebObjectModel.LIMIT, request.getParameter(name));
            } else if ("bn".equals(name)) {
                model.put(LanewebObjectModel.BASSETT_NUMBER, request.getParameter(name));
            } else if ("r".equals(name)) {
                model.put(LanewebObjectModel.RESOURCES, Arrays.asList(request.getParameterValues(name)));
            } else if ("e".equals(name)) {
                model.put(LanewebObjectModel.ENGINES, Arrays.asList(request.getParameterValues(name)));
            } else {
                model.put(name, request.getParameter(name));
            }
        }
        if (request.getQueryString() != null) {
            model.put("query-string", request.getQueryString());
        }
        model.put(LanewebObjectModel.BASE_PATH, request.getContextPath());
        model.put("request-uri", request.getRequestURI());
        model.put("remote-addr", request.getRemoteAddr());
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


    private String getRemoteAddr(final HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        // mod_proxy puts the real remote address in an x-forwarded-for
        // header
        // Load balancer also does this
        String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
        if (header != null) {
            if (header.indexOf(",") > 0) {
                ip = header.substring(header.lastIndexOf(",") + 1, header.length()).trim();
            } else {
                ip = header;
            }
        }
        return ip;
    }
    
}

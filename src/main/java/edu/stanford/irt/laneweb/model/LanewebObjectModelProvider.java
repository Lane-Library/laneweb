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

    private TemplateChooser templateChooser;

    private LDAPDataAccess lDAPDataAccess;

    private String ezproxyKey;

    public LanewebObjectModelProvider(final ProcessInfoProvider pip, final LDAPDataAccess lDAPDataAccess,
            final TemplateChooser templateChooser, final String ezproxyKey) {
        this.processInfoProvider = pip;
        this.lDAPDataAccess = lDAPDataAccess;
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
        String sunetid = (String) request.getAttribute(Model.SUNETID);
        if (sunetid != null) {
            model.put(Model.SUNETID, sunetid);
            session.setAttribute(Model.SUNETID, sunetid);
            String name = (String) session.getAttribute(Model.NAME);
            String univid = (String) session.getAttribute(Model.UNIVID);
            String affiliation = (String) session.getAttribute(Model.AFFILIATION);
            if (null == name || univid == null  || affiliation == null) {
                LDAPData ldapData = this.lDAPDataAccess.getLdapData(sunetid);
                name = ldapData.getName();
                univid = ldapData.getUnivId();
                affiliation = ldapData.getAffiliation();
                if (name != null) {
                    session.setAttribute(Model.NAME, name);
                }
                if (univid != null) {
                    session.setAttribute(Model.UNIVID, univid);
                }
                if (affiliation != null) {
                    session.setAttribute(Model.AFFILIATION, affiliation);
                }
            }
            if (name != null) {
                model.put(Model.NAME, name);
            }
            if (univid != null) {
                model.put(Model.UNIVID, univid);
            }
            if (affiliation != null) {
                model.put(Model.AFFILIATION, affiliation);
            }
        }
        Boolean proxyLinks = (Boolean) request.getAttribute(Model.PROXY_LINKS);
        if (proxyLinks == null) {
            proxyLinks = Boolean.FALSE;
        }
        model.put(Model.PROXY_LINKS, proxyLinks);
        IPGroup ipGroup = (IPGroup) session.getAttribute(Model.IPGROUP);
        if (ipGroup == null) {
            ipGroup = IPGroup.getGroupForIP(request.getRemoteAddr());
            session.setAttribute(Model.IPGROUP, ipGroup);
        }
        model.put(Model.IPGROUP, ipGroup);
        String emrid = (String) session.getAttribute(Model.EMRID);
        if (emrid == null) {
            emrid = request.getParameter(Model.EMRID);
            if (emrid != null) {
                session.setAttribute(Model.EMRID, emrid);
                model.put(Model.EMRID, emrid);
            }
        }
        if (proxyLinks && sunetid != null && (ipGroup != IPGroup.SHC && ipGroup != IPGroup.LPCH)) {
            Ticket ticket = (Ticket) session.getAttribute(Model.TICKET);
            if (ticket == null || !ticket.isValid()) {
                ticket = new Ticket(sunetid, this.ezproxyKey);
            }
            model.put(Model.TICKET, ticket);
        }
        org.apache.cocoon.environment.Context context = ObjectModelHelper.getContext(objectModel);
        model.put("live-base", context.getAttribute("laneweb.context.live-base"));
        model.put("stage-base", context.getAttribute("laneweb.context.stage-base"));
        model.put("medblog-base", context.getAttribute("laneweb.context.medblog-base"));
        model.put("version", context.getAttribute("laneweb.context.version"));
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String name = (String) params.nextElement();
            String value = request.getParameter(name);
            if ("q".equals(name)) {
                model.put(Model.QUERY, value);
                try {
                    model.put("url-encoded-query", URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else if ("t".equals(name)) {
                model.put(Model.TYPE, value);
            } else if ("s".equals(name)) {
                model.put(Model.SUBSET, value);
            } else if ("a".equals(name)) {
                model.put(Model.ALPHA, value.substring(0,1));
            } else if ("m".equals(name)) {
                model.put(Model.MESH, value);
            } else if ("f".equals(name)) {
                model.put(Model.FACETS, value);
            } else if ("l".equals(name)) {
                model.put(Model.LIMIT, value);
            } else if ("bn".equals(name)) {
                model.put(Model.BASSETT_NUMBER, value);
            } else if ("r".equals(name)) {
                model.put(Model.RESOURCES, Arrays.asList(request.getParameterValues(name)));
            } else if ("e".equals(name)) {
                model.put(Model.ENGINES, Arrays.asList(request.getParameterValues(name)));
            } else if ("source".equals(name)) {
                model.put(Model.SOURCE, value);
            } else if ("host".equals(name)) {
                model.put(Model.HOST, value);
            } else if (Model.NONCE.equals(name)) {
                model.put(Model.NONCE, value);
            } else if ("system_user_id".equals(name)) {
                model.put(Model.SYSTEM_USER_ID, value);
            } else if ("release".equals(name)) {
                model.put(Model.RELEASE, value);
            } else if ("password".equals(name)) {
                model.put(Model.PASSWORD, value);
            } else if ("PID".equals(name)) {
                model.put(Model.PID, value);
            } else if ("liaison".equals(name)) {
                model.put(Model.LIAISON, value);
                
//            } else {
//                model.put(name, request.getParameter(name));
            }
        }
        if (request.getQueryString() != null) {
            model.put(Model.QUERY_STRING, request.getQueryString());
        }
        model.put(Model.BASE_PATH, request.getContextPath());
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

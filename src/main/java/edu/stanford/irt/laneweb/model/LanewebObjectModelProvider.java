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
            String name = (String) session.getAttribute(LanewebObjectModel.NAME);
            String univid = (String) session.getAttribute(LanewebObjectModel.UNIVID);
            String affiliation = (String) session.getAttribute(LanewebObjectModel.AFFILIATION);
            if (null == name || univid == null  || affiliation == null) {
                LDAPData ldapData = this.lDAPDataAccess.getLdapData(sunetid);
                name = ldapData.getName();
                univid = ldapData.getUnivId();
                affiliation = ldapData.getAffiliation();
                if (name != null) {
                    session.setAttribute(LanewebObjectModel.NAME, name);
                }
                if (univid != null) {
                    session.setAttribute(LanewebObjectModel.UNIVID, univid);
                }
                if (affiliation != null) {
                    session.setAttribute(LanewebObjectModel.AFFILIATION, affiliation);
                }
            }
            if (name != null) {
                model.put(LanewebObjectModel.NAME, name);
            }
            if (univid != null) {
                model.put(LanewebObjectModel.UNIVID, univid);
            }
            if (affiliation != null) {
                model.put(LanewebObjectModel.AFFILIATION, affiliation);
            }
        }
        Boolean proxyLinks = Boolean.parseBoolean(request.getParameter(LanewebObjectModel.PROXY_LINKS));
        if (proxyLinks == null) {
            proxyLinks = (Boolean) session.getAttribute(LanewebObjectModel.PROXY_LINKS);
            if (proxyLinks == null) {
                proxyLinks = this.proxyLinks.proxyLinks(request);
                session.setAttribute(LanewebObjectModel.PROXY_LINKS, proxyLinks);
            }
        } else {
            session.setAttribute(LanewebObjectModel.PROXY_LINKS, proxyLinks);
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
            if (ticket == null || !ticket.isValid()) {
                ticket = new Ticket(sunetid, this.ezproxyKey);
            }
            model.put(LanewebObjectModel.TICKET, ticket);
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
                model.put(LanewebObjectModel.QUERY, value);
                try {
                    model.put("url-encoded-query", URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else if ("t".equals(name)) {
                model.put(LanewebObjectModel.TYPE, value);
            } else if ("s".equals(name)) {
                model.put(LanewebObjectModel.SUBSET, value);
            } else if ("a".equals(name)) {
                model.put(LanewebObjectModel.ALPHA, value.substring(0,1));
            } else if ("m".equals(name)) {
                model.put(LanewebObjectModel.MESH, value);
            } else if ("f".equals(name)) {
                model.put(LanewebObjectModel.FACETS, value);
            } else if ("l".equals(name)) {
                model.put(LanewebObjectModel.LIMIT, value);
            } else if ("bn".equals(name)) {
                model.put(LanewebObjectModel.BASSETT_NUMBER, value);
            } else if ("r".equals(name)) {
                model.put(LanewebObjectModel.RESOURCES, Arrays.asList(request.getParameterValues(name)));
            } else if ("e".equals(name)) {
                model.put(LanewebObjectModel.ENGINES, Arrays.asList(request.getParameterValues(name)));
            } else if ("source".equals(name)) {
                model.put(LanewebObjectModel.SOURCE, value);
            } else if ("host".equals(name)) {
                model.put(LanewebObjectModel.HOST, value);
            } else if (LanewebObjectModel.NONCE.equals(name)) {
                model.put(LanewebObjectModel.NONCE, value);
            } else if ("system_user_id".equals(name)) {
                model.put(LanewebObjectModel.SYSTEM_USER_ID, value);
            } else if ("release".equals(name)) {
                model.put(LanewebObjectModel.RELEASE, value);
            } else if ("password".equals(name)) {
                model.put(LanewebObjectModel.PASSWORD, value);
                
//            } else {
//                model.put(name, request.getParameter(name));
            }
        }
        if (request.getQueryString() != null) {
            model.put(LanewebObjectModel.QUERY_STRING, request.getQueryString());
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

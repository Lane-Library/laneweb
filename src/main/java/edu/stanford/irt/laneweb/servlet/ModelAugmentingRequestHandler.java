package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.ldap.LDAPData;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;

public class ModelAugmentingRequestHandler extends SitemapRequestHandler {

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    private ProxyLinks proxyLinks;

    private TemplateChooser templateChooser;
    
    private SunetIdSource sunetIdSource = new SunetIdSource();
    
    private PersistentLoginProcessor persistentLoginProcessor = new PersistentLoginProcessor();
    
    private LDAPDataAccess ldapDataAccess;
    
    private String ezproxyKey;
    
    public void setLDAPDataAccess(final LDAPDataAccess ldapDataAccess) {
        this.ldapDataAccess = ldapDataAccess;
    }
    
    public void setEzproxyKey(final String ezproxyKey) {
        this.ezproxyKey = ezproxyKey;
    }

    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> model = (Map<String, Object>) request.getAttribute(Model.MODEL);
        HttpSession session = request.getSession();
        String sunetid = this.sunetIdSource.getSunetid(request, session);
        addToModel(Model.SUNETID, sunetid, model);
        addToModel(Model.DEBUG, getDebugValue(request, session), model);
        this.persistentLoginProcessor.processSunetid(sunetid, request, response);
        addLdapData(sunetid, session, model);
        String remoteAddr = getRemoteAddr(request, session);
        addToModel(Model.REMOTE_ADDR, remoteAddr, model);
        IPGroup ipGroup = getIPGroup(remoteAddr, session);
        addToModel(Model.IPGROUP, ipGroup, model);
        Boolean proxyLinks = this.proxyLinks.getProxyLinks(request, session, ipGroup, remoteAddr);
        addToModel(Model.PROXY_LINKS, proxyLinks, model);
        addToModel(Model.TEMPLATE, this.templateChooser.getTemplate(request), model);
        addToModel(Model.EMRID, getEmrid(request, session), model);
        addToModel(Model.TICKET, getTicket(proxyLinks, sunetid, ipGroup, session), model);
        addRequestParameters(request, model);
        addToModel(Model.QUERY_STRING, request.getQueryString(), model);
        addToModel(Model.BASE_PATH, getBasePath(request), model);
        addToModel("request-uri", request.getRequestURI(), model);
        addToModel("referer", request.getHeader("referer"), model);
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (SunetIdCookieCodec.LANE_COOKIE_NAME.equals(cookie.getName())) {
                    addToModel("user-cookie", cookie.getValue(), model);
                    break;
                }
            }
        }
        super.process(request, response);
    }
    
    private String getBasePath(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder(request.getContextPath());
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        if (uri.indexOf("/stage") == 0) {
            builder.append("/stage");
        } else {
            for (String key : this.baseMappings.keySet()) {
                if (uri.indexOf(key) == 0) {
                    builder.append(key);
                    break;
                }
            }
        }
        return builder.toString();
    }

    private Boolean getDebugValue(HttpServletRequest request, HttpSession session) {
        String debugParameter = request.getParameter(Model.DEBUG);
        if (debugParameter == null) {
            return (Boolean) session.getAttribute(Model.DEBUG);
        } else {
            Boolean debug = Boolean.parseBoolean(debugParameter);
            session.setAttribute(Model.DEBUG, debug);
            return debug;
        }
    }

    private void addRequestParameters(HttpServletRequest request, Map<String, Object> model) {
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
        String name = (String) params.nextElement();
        String value = request.getParameter(name);
        if ("q".equals(name)) {
            model.put(Model.QUERY, value);
            try {
                model.put("url-encoded-query", URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                //won't happen
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
        } else if ("show".equals(name)) {
            model.put(Model.SHOW, value);
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
        } else if (Model.CALLBACK.equals(name)) {
            model.put(Model.CALLBACK, value);
            
//        } else {
//            model.put(name, request.getParameter(name));
        }
    }
    }

    private Object getTicket(Boolean proxyLinks, String sunetid, IPGroup ipGroup, HttpSession session) {
        Ticket ticket = null;
        if (proxyLinks && sunetid != null && (ipGroup != IPGroup.SHC && ipGroup != IPGroup.LPCH)) {
            ticket = (Ticket) session.getAttribute(Model.TICKET);
            if (ticket == null || !ticket.isValid()) {
                ticket = new Ticket(sunetid, this.ezproxyKey);
                session.setAttribute(Model.TICKET, ticket);
            }
        }
        return ticket;
    }

    private String getEmrid(HttpServletRequest request, HttpSession session) {
        String emrid = (String) session.getAttribute(Model.EMRID);
        if (emrid == null) {
            emrid = request.getParameter(Model.EMRID);
            if (emrid != null) {
                session.setAttribute(Model.EMRID, emrid);
            }
        }
        return emrid;
    }

    private void addLdapData(String sunetid, HttpSession session, Map<String, Object> model) {
        String name = (String) session.getAttribute(Model.NAME);
        String affiliation = (String) session.getAttribute(Model.AFFILIATION);
        String univid = (String) session.getAttribute(Model.UNIVID);
        if (sunetid != null && name == null) {
            LDAPData ldapData = this.ldapDataAccess.getLdapData(sunetid);
            name = ldapData.getName();
            affiliation = ldapData.getAffiliation();
            univid = ldapData.getUnivId();
            session.setAttribute(Model.NAME, name);
            session.setAttribute(Model.AFFILIATION, affiliation);
            session.setAttribute(Model.UNIVID, univid);
        }
        addToModel(Model.NAME, name, model);
        addToModel(Model.AFFILIATION, affiliation, model);
        addToModel(Model.UNIVID, univid, model);
    }

    public void setProxyLinks(final ProxyLinks proxyLinks) {
        this.proxyLinks = proxyLinks;
    }
    
    public void setTemplateChooser(final TemplateChooser templateChooser) {
        this.templateChooser = templateChooser;
    }
    
    private IPGroup getIPGroup(String remoteAddr, HttpSession session) {
        IPGroup ipGroup = (IPGroup) session.getAttribute(Model.IPGROUP);
        if (ipGroup == null) {
            ipGroup = IPGroup.getGroupForIP(remoteAddr);
            session.setAttribute(Model.IPGROUP, ipGroup);
        }
        return ipGroup;
    }


    private String getRemoteAddr(final HttpServletRequest request, HttpSession session) {
        String ip = (String) session.getAttribute(Model.REMOTE_ADDR);
        if (ip == null) {
            // mod_proxy puts the real remote address in an x-forwarded-for
            // header
            // Load balancer also does this
            String header = request.getHeader(X_FORWARDED_FOR);
            if (header == null) {
                ip = request.getRemoteAddr();
            } else {
                if (header.indexOf(',') > 0) {
                    ip = header.substring(0, header.indexOf(','));
                } else {
                    ip = header;
                }
            }
            session.setAttribute(Model.REMOTE_ADDR, ip);
        }
        return ip;
    }
    
}

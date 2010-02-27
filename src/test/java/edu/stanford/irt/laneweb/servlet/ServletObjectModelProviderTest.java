package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;

public class ServletObjectModelProviderTest {

    @SuppressWarnings("unchecked")
    private Map objectModel;
    
    private ServletContext servletContext;

    private ObjectModelProvider provider;

    private HttpServletRequest request;

    private HttpSession session;

    private LDAPDataAccess lDAPDataAccess;
    
    @SuppressWarnings("unchecked")
    private Enumeration params;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.params = createMock(Enumeration.class);
        this.servletContext = createMock(ServletContext.class);
        this.request = createMock(HttpServletRequest.class);
        this.objectModel = new HashMap();
        this.session = createMock(HttpSession.class);
        this.lDAPDataAccess = createMock(LDAPDataAccess.class);
        this.provider = new ServletObjectModelProvider(this.servletContext, this.lDAPDataAccess, "ezproxyKey");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(this.request));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetObject() {
        this.objectModel.put("httprequest", this.request);
        expect(this.request.getSession(true)).andReturn(this.session);
        expect(this.request.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.request.getAttribute(Model.PROXY_LINKS)).andReturn(null);
        expect(this.request.getAttribute("template")).andReturn(null);
        expect(this.session.getAttribute(Model.IPGROUP)).andReturn(null);
        expect(this.session.getAttribute(Model.EMRID)).andReturn(null);
        expect(this.servletContext.getAttribute(isA(String.class))).andReturn("foo").atLeastOnce();
        expect(this.request.getParameterNames()).andReturn(this.params);
        expect(this.params.hasMoreElements()).andReturn(Boolean.FALSE);
        expect(this.request.getParameter(Model.EMRID)).andReturn(null);
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getContextPath()).andReturn("");
        expect(this.request.getRequestURI()).andReturn("/index.html");
        expect(this.request.getRemoteAddr()).andReturn("127.0.0.1").times(2);
        expect(this.request.getHeader("referer")).andReturn(null);
        expect(this.request.getCookies()).andReturn(null);
        this.session.setAttribute(Model.IPGROUP, IPGroup.OTHER);
        replayMocks();
        this.provider.getObject();
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.params);
        replay(this.lDAPDataAccess);
        replay(this.session);
        replay(this.request);
        replay(this.servletContext);
    }

    private void verifyMocks() {
        verify(this.params);
        verify(this.lDAPDataAccess);
        verify(this.session);
        verify(this.request);
        verify(this.servletContext);
    }
}

package edu.stanford.irt.laneweb.model;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.apache.cocoon.environment.Context;
import org.apache.cocoon.processing.ProcessInfoProvider;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;

public class LanewebObjectModelProviderTest {

    private Context context;

    @SuppressWarnings("unchecked")
    private Map objectModel;

    private ProcessInfoProvider pip;

    private ObjectModelProvider provider;

    private ProxyLinks proxyLinks;

    private HttpServletRequest request;

    private HttpSession session;

    private TemplateChooser templateChooser;

    private LDAPDataAccess lDAPDataAccess;
    
    @SuppressWarnings("unchecked")
    private Enumeration params;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.params = createMock(Enumeration.class);
        this.pip = createMock(ProcessInfoProvider.class);
        this.request = createMock(HttpServletRequest.class);
        this.objectModel = new HashMap();
        this.session = createMock(HttpSession.class);
        this.lDAPDataAccess = createMock(LDAPDataAccess.class);
        this.proxyLinks = createMock(ProxyLinks.class);
        this.context = createMock(Context.class);
        this.templateChooser = createMock(TemplateChooser.class);
        this.provider = new LanewebObjectModelProvider(this.pip, this.lDAPDataAccess, this.proxyLinks, this.templateChooser,"ezproxyKey");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetObject() {
        this.objectModel.put("httprequest", this.request);
        this.objectModel.put("context", this.context);
        expect(this.pip.getObjectModel()).andReturn(this.objectModel);
        expect(this.request.getSession(true)).andReturn(this.session);
        expect(this.request.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.request.getParameter(Model.PROXY_LINKS)).andReturn(null);
        expect(this.session.getAttribute(Model.IPGROUP)).andReturn(null);
        expect(this.session.getAttribute(Model.EMRID)).andReturn(null);
        this.session.setAttribute(Model.PROXY_LINKS, Boolean.FALSE);
        expect(this.context.getAttribute(isA(String.class))).andReturn("foo").atLeastOnce();
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
        expect(this.templateChooser.chooseTemplate(this.request)).andReturn("template");
        replayMocks();
        this.provider.getObject();
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.params);
        replay(this.context);
        replay(this.proxyLinks);
        replay(this.lDAPDataAccess);
        replay(this.session);
        replay(this.request);
        replay(this.pip);
    }

    private void verifyMocks() {
        verify(this.params);
        verify(this.context);
        verify(this.proxyLinks);
        verify(this.lDAPDataAccess);
        verify(this.session);
        verify(this.request);
        verify(this.pip);
    }
}

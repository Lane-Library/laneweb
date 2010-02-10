package edu.stanford.irt.laneweb.model;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Collections;
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

import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;

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

    private UserDao userDao;
    
    private Enumeration params;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.params = createMock(Enumeration.class);
        this.pip = createMock(ProcessInfoProvider.class);
        this.request = createMock(HttpServletRequest.class);
        this.objectModel = new HashMap();
        this.session = createMock(HttpSession.class);
        this.userDao = createMock(UserDao.class);
        this.proxyLinks = createMock(ProxyLinks.class);
        this.context = createMock(Context.class);
        this.templateChooser = createMock(TemplateChooser.class);
        this.provider = new LanewebObjectModelProvider(this.pip, this.userDao, this.proxyLinks, this.templateChooser);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetObject() {
        this.objectModel.put("httprequest", this.request);
        this.objectModel.put("context", this.context);
        expect(this.pip.getObjectModel()).andReturn(this.objectModel);
        expect(this.request.getSession(true)).andReturn(this.session);
        expect(this.session.getAttribute(LanewebConstants.USER)).andReturn(null);
        this.userDao.getUserData(isA(User.class), eq(this.request));
        this.session.setAttribute(eq(LanewebConstants.USER), isA(User.class));
        expect(this.proxyLinks.proxyLinks(isA(User.class), eq(this.request))).andReturn(Boolean.TRUE);
        expect(this.context.getAttribute(isA(String.class))).andReturn("foo").atLeastOnce();
        expect(this.request.getParameterNames()).andReturn(this.params);
        expect(this.params.hasMoreElements()).andReturn(Boolean.FALSE);
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getContextPath()).andReturn("");
        expect(this.request.getRequestURI()).andReturn("/index.html");
        expect(this.request.getRemoteHost()).andReturn("127.0.0.1");
        expect(this.request.getHeader("referer")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn(null);
        expect(this.request.getCookies()).andReturn(null);
        expect(this.templateChooser.chooseTemplate(this.request)).andReturn("template");
        replayMocks();
        this.provider.getObject();
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.params);
        replay(this.context);
        replay(this.proxyLinks);
        replay(this.userDao);
        replay(this.session);
        replay(this.request);
        replay(this.pip);
    }

    private void verifyMocks() {
        verify(this.params);
        verify(this.context);
        verify(this.proxyLinks);
        verify(this.userDao);
        verify(this.session);
        verify(this.request);
        verify(this.pip);
    }
}

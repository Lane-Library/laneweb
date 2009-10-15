package edu.stanford.irt.laneweb.cme;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

public class CMERedirectActionTest {

    private CMERedirectAction action;

    @Before
    public void setUp() throws Exception {
        this.action = new CMERedirectAction();
    }

    @Test
    public void testAct() {
        Parameters parameters = createMock(Parameters.class);
        expect(parameters.getParameter("host", null)).andReturn("uptodate");
        expect(parameters.getParameter("emrid", null)).andReturn("nobody");
        replay(parameters);
        assertEquals("http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?unid=nobody&srcsys=epic90710&eiv=2.1.0",
                this.action.act(null, null, null, null, parameters).get("cme-redirect"));
        verify(parameters);
    }

    @Test
    public void testActEmptyEmrid() {
        Parameters parameters = createMock(Parameters.class);
        expect(parameters.getParameter("host", null)).andReturn("uptodate");
        expect(parameters.getParameter("emrid", null)).andReturn("");
        expect(parameters.getParameter("query-string", null)).andReturn("yo");
        replay(parameters);
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, null, null, parameters).get("cme-redirect"));
        verify(parameters);
    }

    @Test
    public void testActEmptyHost() {
        Parameters parameters = createMock(Parameters.class);
        expect(parameters.getParameter("host", null)).andReturn("");
        expect(parameters.getParameter("emrid", null)).andReturn("nobody");
        expect(parameters.getParameter("query-string", null)).andReturn("yo");
        replay(parameters);
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, null, null, parameters).get("cme-redirect"));
        verify(parameters);
    }

    @Test
    public void testActNullBadHost() {
        Parameters parameters = createMock(Parameters.class);
        expect(parameters.getParameter("host", null)).andReturn("bad host");
        expect(parameters.getParameter("emrid", null)).andReturn("nobody");
        expect(parameters.getParameter("query-string", null)).andReturn("yo");
        replay(parameters);
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, null, null, parameters).get("cme-redirect"));
        verify(parameters);
    }

    @Test
    public void testActNullBadHostEmptyEmrid() {
        Parameters parameters = createMock(Parameters.class);
        expect(parameters.getParameter("host", null)).andReturn("bad host");
        expect(parameters.getParameter("emrid", null)).andReturn(null);
        expect(parameters.getParameter("query-string", null)).andReturn("yo");
        replay(parameters);
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, null, null, parameters).get("cme-redirect"));
        verify(parameters);
    }

    @Test
    public void testActNullEmrid() {
        Parameters parameters = createMock(Parameters.class);
        expect(parameters.getParameter("host", null)).andReturn("uptodate");
        expect(parameters.getParameter("emrid", null)).andReturn(null);
        expect(parameters.getParameter("query-string", null)).andReturn("yo");
        replay(parameters);
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, null, null, parameters).get("cme-redirect"));
        verify(parameters);
    }

    @Test
    public void testActNullHost() {
        Parameters parameters = createMock(Parameters.class);
        expect(parameters.getParameter("host", null)).andReturn(null);
        expect(parameters.getParameter("emrid", null)).andReturn("nobody");
        expect(parameters.getParameter("query-string", null)).andReturn("yo");
        replay(parameters);
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, null, null, parameters).get("cme-redirect"));
        verify(parameters);
    }
}

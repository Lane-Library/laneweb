package edu.stanford.irt.laneweb.cme;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class CMERedirectActionTest {

    private CMERedirectAction action;

    private Model model;

    @Before
    public void setUp() throws Exception {
        this.action = new CMERedirectAction();
        this.model = createMock(Model.class);
        this.action.setModel(this.model);
    }

    @Test
    public void testAct() throws Exception {
        expect(this.model.getString("host")).andReturn("uptodate");
        expect(this.model.getString(Model.EMRID)).andReturn("nobody");
        replayMocks();
        assertEquals(
                "http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?unid=nobody&srcsys=epic90710&eiv=2.1.0",
                this.action.doAct().get("cme-redirect"));
        verifyMocks();
    }

    @Test
    public void testActEmptyEmrid() throws Exception {
        expect(this.model.getString("host")).andReturn("uptodate");
        expect(this.model.getString(Model.EMRID)).andReturn(null);
        expect(this.model.getString(Model.QUERY_STRING)).andReturn("yo");
        replayMocks();
        assertEquals("/cmeRedirectError.html?yo", this.action.doAct().get("cme-redirect"));
        verifyMocks();
    }

    @Test
    public void testActEmptyHost() throws Exception {
        expect(this.model.getString("host")).andReturn("");
        expect(this.model.getString(Model.EMRID)).andReturn("nobody");
        expect(this.model.getString(Model.QUERY_STRING)).andReturn("yo");
        replayMocks();
        assertEquals("/cmeRedirectError.html?yo", this.action.doAct().get("cme-redirect"));
        verifyMocks();
    }

    @Test
    public void testActNullBadHost() throws Exception {
        expect(this.model.getString("host")).andReturn("bad host");
        expect(this.model.getString(Model.EMRID)).andReturn("nobody");
        expect(this.model.getString(Model.QUERY_STRING)).andReturn("yo");
        replayMocks();
        assertEquals("/cmeRedirectError.html?yo", this.action.doAct().get("cme-redirect"));
        verifyMocks();
    }

    @Test
    public void testActNullBadHostEmptyEmrid() throws Exception {
        expect(this.model.getString("host")).andReturn("bad host");
        expect(this.model.getString(Model.EMRID)).andReturn(null);
        expect(this.model.getString(Model.QUERY_STRING)).andReturn("yo");
        replayMocks();
        assertEquals("/cmeRedirectError.html?yo", this.action.doAct().get("cme-redirect"));
        verifyMocks();
    }

    @Test
    public void testActNullEmrid() throws Exception {
        expect(this.model.getString("host")).andReturn("uptodate");
        expect(this.model.getString(Model.EMRID)).andReturn(null);
        expect(this.model.getString(Model.QUERY_STRING)).andReturn("yo");
        replayMocks();
        assertEquals("/cmeRedirectError.html?yo", this.action.doAct().get("cme-redirect"));
        verifyMocks();
    }

    @Test
    public void testActNullHost() throws Exception {
        expect(this.model.getString("host")).andReturn(null);
        expect(this.model.getString(Model.EMRID)).andReturn("nobody");
        expect(this.model.getString(Model.QUERY_STRING)).andReturn("yo");
        replayMocks();
        assertEquals("/cmeRedirectError.html?yo", this.action.doAct().get("cme-redirect"));
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.model);
    }

    private void verifyMocks() {
        verify(this.model);
    }
}

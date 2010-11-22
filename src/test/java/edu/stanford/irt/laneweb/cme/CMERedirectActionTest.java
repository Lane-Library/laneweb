package edu.stanford.irt.laneweb.cme;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.Model;

public class CMERedirectActionTest {

    private CMERedirectAction action;

    private Map<String, Object> model;

    @Before
    public void setUp() throws Exception {
        this.action = new CMERedirectAction();
        this.model = new HashMap<String, Object>();
        
    }

    @Test
    public void testAct() throws Exception {
        this.model.put("host", "uptodate");
        this.model.put(Model.EMRID, "nobody");
        assertEquals("http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?unid=nobody&srcsys=epic90710&eiv=2.1.0",
                this.action.act(null, null, this.model, null, null).get("cme-redirect"));
    }

    @Test
    public void testActEmptyEmrid() throws Exception {
        this.model.put("host", "uptodate");
        this.model.put(Model.QUERY_STRING, "yo");
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, this.model, null, null).get("cme-redirect"));
    }

    @Test
    public void testActEmptyHost() throws Exception {
        this.model.put("host", "");
        this.model.put(Model.EMRID, "nobody");
        this.model.put(Model.QUERY_STRING, "yo");
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, this.model, null, null).get("cme-redirect"));
    }

    @Test
    public void testActNullBadHost() throws Exception {
        this.model.put("host", "bad host");
        this.model.put(Model.EMRID, "nobody");
        this.model.put(Model.QUERY_STRING, "yo");
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, this.model, null, null).get("cme-redirect"));
    }

    @Test
    public void testActNullBadHostEmptyEmrid() throws Exception {
        this.model.put("host", "bad host");
        this.model.put(Model.QUERY_STRING, "yo");
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, this.model, null, null).get("cme-redirect"));
    }

    @Test
    public void testActNullEmrid() throws Exception {
        this.model.put("host", "uptodate");
        this.model.put(Model.QUERY_STRING, "yo");
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, this.model, null, null).get("cme-redirect"));
    }

    @Test
    public void testActNullHost() throws Exception {
        this.model.put(Model.EMRID, "nobody");
        this.model.put(Model.QUERY_STRING, "yo");
        assertEquals("/cmeRedirectError.html?yo", this.action.act(null, null, this.model, null, null).get("cme-redirect"));
    }
}

package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;

public class BaseProxyURLDataBinderTest {

    private BaseProxyURLDataBinder binder;

    private Map<String, Object> model;

    @BeforeEach
    public void setUp() throws Exception {
        this.binder = new BaseProxyURLDataBinder();
        this.model = new HashMap<>();
        this.model.put(Model.PROXY_LINKS, Boolean.TRUE);
    }

    @Test
    public void testBindProxyLinksFalse() {
        this.model.put(Model.PROXY_LINKS, Boolean.FALSE);
        this.binder.bind(this.model, null);
        assertNull(this.model.get(Model.BASE_PROXY_URL));
    }

    @Test
    public void testBindProxyLinksNull() {
        this.model.remove(Model.PROXY_LINKS);
        this.binder.bind(this.model, null);
        assertNull(this.model.get(Model.BASE_PROXY_URL));
    }

    @Test
    public void testLPCHIP() {
        this.model.put(Model.IPGROUP, IPGroup.LPCH);
        this.binder.bind(this.model, null);
        assertEquals("https://login.laneproxy.stanford.edu/login?url=", this.model.get(Model.BASE_PROXY_URL));
    }

    @Test
    public void testNullTicket() {
        this.model.put(Model.BASE_PATH, "");
        this.model.put(Model.USER_ID, "userid");
        this.binder.bind(this.model, null);
        assertEquals("https://login.laneproxy.stanford.edu/login?url=", this.model.get(Model.BASE_PROXY_URL));
    }

    @Test
    public void testNullUserId() {
        this.model.put(Model.BASE_PATH, "");
        this.model.put(Model.TICKET, new Ticket("", ""));
        this.binder.bind(this.model, null);
        assertEquals("https://login.laneproxy.stanford.edu/login?url=", this.model.get(Model.BASE_PROXY_URL));
    }

    @Test
    public void testSHCIP() {
        this.model.put(Model.IPGROUP, IPGroup.SHC);
        this.binder.bind(this.model, null);
        assertEquals("https://login.laneproxy.stanford.edu/login?url=", this.model.get(Model.BASE_PROXY_URL));
    }

    @Test
    public void testUserIdAndTicket() {
        this.model.put(Model.BASE_PATH, "");
        this.model.put(Model.USER_ID, "userid");
        Ticket ticket = new Ticket("", "");
        this.model.put(Model.TICKET, ticket);
        this.binder.bind(this.model, null);
        assertEquals("https://login.laneproxy.stanford.edu/login?url=", this.model.get(Model.BASE_PROXY_URL));
    }
}

package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

public class AbstractProxyLinkTransformerTest {

    private Map<String, Object> model;

    private Parameters parameters;

    private AbstractProxyLinkTransformer transformer;

    @Before
    public void setUp() throws Exception {
        this.transformer = new AbstractProxyLinkTransformer() {
        };
        this.model = new HashMap<String, Object>();
        this.parameters = createMock(Parameters.class);
    }

    @Test
    public void testCreateProxyLink() {
        replay(this.parameters);
        assertEquals("null/secure/apps/proxy/credential?url=foo", this.transformer.createProxyLink("foo"));
        verify(this.parameters);
    }

    @Test
    public void testHospitalIP() {
        this.model.put(Model.IPGROUP, IPGroup.SHC);
        expect(this.parameters.getParameter(isA(String.class), (String) isNull())).andReturn(null).atLeastOnce();
        replay(this.parameters);
        this.transformer.setup(null, this.model, null, this.parameters);
        assertEquals("http://laneproxy.stanford.edu/login?url=foo", this.transformer.createProxyLink("foo"));
        verify(this.parameters);
    }
}

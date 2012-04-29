package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.excalibur.source.SourceValidity;
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
    public void testGetKeyFalse() {
    	this.model.put(Model.PROXY_LINKS, Boolean.FALSE);
        replay(this.parameters);
        this.transformer.setup(null, this.model, null, this.parameters);
        assertEquals("false", this.transformer.getKey());
        verify(this.parameters);
    }
    
    @Test
    public void testGetKeyTrue() {
    	this.model.put(Model.PROXY_LINKS, Boolean.TRUE);
        replay(this.parameters);
        this.transformer.setup(null, this.model, null, this.parameters);
        assertEquals("true", this.transformer.getKey());
        verify(this.parameters);
    }
    
    @Test
    public void testGetValidityCacheable() {
    	this.model.put(Model.PROXY_LINKS, Boolean.FALSE);
        replay(this.parameters);
        this.transformer.setup(null, this.model, null, this.parameters);
        assertEquals(SourceValidity.VALID, this.transformer.getValidity().isValid());
        verify(this.parameters);
    }
    
    @Test
    public void testGetValidityNotCacheable() {
    	this.model.put(Model.PROXY_LINKS, Boolean.TRUE);
        replay(this.parameters);
        this.transformer.setup(null, this.model, null, this.parameters);
        assertNull(this.transformer.getValidity());
        verify(this.parameters);
    }
    
    @Test
    public void testHospitalIP() {
        this.model.put(Model.IPGROUP, IPGroup.SHC);
        replay(this.parameters);
        this.transformer.setup(null, this.model, null, this.parameters);
        assertEquals("http://laneproxy.stanford.edu/login?url=foo", this.transformer.createProxyLink("foo"));
        verify(this.parameters);
    }
}

package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

// $Id$
public class AbstractProxyLinkTransformerTest {

    private Model model;

    private Parameters parameters;

    private AbstractProxyLinkTransformer transformer;

    @Before
    public void setUp() throws Exception {
        this.transformer = new AbstractProxyLinkTransformer() {
        };
        this.model = createMock(Model.class);
        this.parameters = createMock(Parameters.class);
        this.transformer.setModel(this.model);
    }

    @Test
    public void testCreateProxyLink() {
        replayMocks();
        assertEquals("null/secure/proxy?url=foo", this.transformer.createProxyLink("foo"));
        verifyMocks();
    }

    @Test
    public void testHospitalIP() {
        expect(this.model.getString(isA(String.class))).andReturn(null).atLeastOnce();
        expect(this.model.getObject(isA(String.class), isA(Class.class))).andReturn(null);
        expect(this.model.getObject(isA(String.class), isA(Class.class), eq(Boolean.FALSE))).andReturn(true);
        expect(this.model.getObject(isA(String.class), isA(Class.class), eq(IPGroup.OTHER))).andReturn(IPGroup.SHC);
        expect(this.parameters.getParameter(isA(String.class), (String) isNull())).andReturn(null).atLeastOnce();
        replayMocks();
        this.transformer.setup(null, null, null, this.parameters);
        assertEquals("http://laneproxy.stanford.edu/login?url=foo", this.transformer.createProxyLink("foo"));
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.model);
        replay(this.parameters);
    }

    private void verifyMocks() {
        verify(this.model);
        verify(this.parameters);
    }
}

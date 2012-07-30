package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;

public class IPGroupFetchControllerTest {

    private RemoteProxyIPDataBinder binder;

    private IPGroupFetchController controller;

    private Model model;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.binder = createMock(RemoteProxyIPDataBinder.class);
        this.controller = new IPGroupFetchController(this.binder);
        this.model = createMock(Model.class);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testBind() {
        expect(this.model.asMap()).andReturn(Collections.<String, Object>emptyMap());
        this.binder.bind(Collections.<String, Object>emptyMap(), this.request);
        replay(this.binder, this.model, this.request, this.response);
        this.controller.bind(this.request, this.model);
        verify(this.binder, this.model, this.request, this.response);
    }

    @Test
    public void testGetIPGroup() throws IOException {
        replay(this.binder, this.model, this.request, this.response);
        assertEquals("callback('OTHER');", this.controller.getIPGroup(this.response, IPGroup.OTHER, "callback"));
        verify(this.binder, this.model, this.request, this.response);
    }

    @Test
    public void testGetIPGroupNoCallback() throws IOException {
        replay(this.binder, this.model, this.request, this.response);
        assertEquals("OTHER", this.controller.getIPGroup(this.response, IPGroup.OTHER, null));
        verify(this.binder, this.model, this.request, this.response);
    }
}

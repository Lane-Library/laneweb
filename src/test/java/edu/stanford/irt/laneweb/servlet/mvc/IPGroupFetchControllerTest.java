package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

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

    @Before
    public void setUp() throws Exception {
        this.binder = mock(RemoteProxyIPDataBinder.class);
        this.controller = new IPGroupFetchController(this.binder);
        this.model = mock(Model.class);
        this.request = mock(HttpServletRequest.class);
    }

    @Test
    public void testBind() {
        expect(this.model.asMap()).andReturn(Collections.emptyMap());
        this.binder.bind(Collections.emptyMap(), this.request);
        replay(this.binder, this.model, this.request);
        this.controller.bind(this.request, this.model);
        verify(this.binder, this.model, this.request);
    }

    @Test
    public void testGetIPGroup() {
        replay(this.binder, this.model, this.request);
        assertEquals("callback('OTHER');", this.controller.getIPGroup(IPGroup.OTHER, "callback"));
        verify(this.binder, this.model, this.request);
    }

    @Test
    public void testGetIPGroupNoCallback() {
        replay(this.binder, this.model, this.request);
        assertEquals("OTHER", this.controller.getIPGroup(IPGroup.OTHER, null));
        verify(this.binder, this.model, this.request);
    }
}

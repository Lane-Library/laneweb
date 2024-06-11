package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;

@RunWith(Parameterized.class)
public class IPGroupFetchControllerTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // get ip with callback
                { IPGroup.OTHER, "callback", "callback('OTHER');" },
                // malicious callback
                { IPGroup.OTHER, "LANE.Tracker.setIpGroup71907%3balert(1)%2f%2f734", "OTHER" },
                // no callback
                { IPGroup.OTHER, null, "OTHER" } });
    }

    private RemoteProxyIPDataBinder binder;

    private String callback;

    private IPGroupFetchController controller;

    private String expected;

    private IPGroup ipGroup;

    private Model model;

    private HttpServletRequest request;

    public IPGroupFetchControllerTest(final IPGroup ipGroup, final String callback, final String expected) {
        this.ipGroup = ipGroup;
        this.callback = callback;
        this.expected = expected;
    }

    @Before
    public void setUp() throws Exception {
        this.binder = mock(RemoteProxyIPDataBinder.class);
        this.controller = new IPGroupFetchController(this.binder);
        this.model = mock(Model.class);
        this.request = mock(HttpServletRequest.class);
    }

    @Test
    public void shouldReturnExpectedString() {
        replay(this.binder, this.model, this.request);
        String actual = this.controller.getIPGroup(this.ipGroup, this.callback);
        assertEquals(actual, this.expected);
        verify(this.binder, this.model, this.request);
    }

    @Test
    public void testBind() {
        expect(this.model.asMap()).andReturn(Collections.emptyMap());
        this.binder.bind(Collections.emptyMap(), this.request);
        replay(this.binder, this.model, this.request);
        this.controller.bind(this.request, this.model);
        verify(this.binder, this.model, this.request);
    }
}

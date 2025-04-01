package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;

public class IPGroupFetchControllerTest {

    public static Stream<Arguments> data() {
        return Stream.of(
                // get ip with callback
                Arguments.of(IPGroup.OTHER, "callback", "callback('OTHER');"),
                // malicious callback
                Arguments.of(IPGroup.OTHER, "LANE.Tracker.setIpGroup71907%3balert(1)%2f%2f734", "OTHER"),
                // no callback
                Arguments.of(IPGroup.OTHER, null, "OTHER"));
    }

    private RemoteProxyIPDataBinder binder;

    private IPGroupFetchController controller;

    private Model model;

    private HttpServletRequest request;

    @BeforeEach
    public void setUp() throws Exception {
        this.binder = mock(RemoteProxyIPDataBinder.class);
        this.controller = new IPGroupFetchController(this.binder);
        this.model = mock(Model.class);
        this.request = mock(HttpServletRequest.class);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void shouldReturnExpectedString(final IPGroup ipGroup, final String callback, final String expected) {
        replay(this.binder, this.model, this.request);
        String actual = this.controller.getIPGroup(ipGroup, callback);
        assertEquals(actual, expected);
        verify(this.binder, this.model, this.request);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testBind(final IPGroup ipGroup, final String callback, final String expected) {
        expect(this.model.asMap()).andReturn(Collections.emptyMap());
        this.binder.bind(Collections.emptyMap(), this.request);
        replay(this.binder, this.model, this.request);
        this.controller.bind(this.request, this.model);
        verify(this.binder, this.model, this.request);
    }
}

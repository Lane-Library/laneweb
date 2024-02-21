package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class UserDataControllerTest {

    private UserDataController controller;

    private DataBinder dataBinder;

    private Model model;

    private HttpServletRequest request;

    @Before
    public void setUp() {
        this.dataBinder = mock(DataBinder.class);
        this.controller = new UserDataController(this.dataBinder);
        this.model = mock(Model.class);
        this.request = mock(HttpServletRequest.class);
    }

    @Test
    public void testGetUserData() {
        expect(this.model.asMap()).andReturn(Collections.singletonMap("key", "value")).times(2);
        this.dataBinder.bind(Collections.singletonMap("key", "value"), this.request);
        replay(this.dataBinder, this.model, this.request);
        assertEquals("value", this.controller.getUserData(this.request, this.model).get("key"));
        verify(this.dataBinder, this.model, this.request);
    }
}

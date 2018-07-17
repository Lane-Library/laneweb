package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.crm.CRMService;

public class LaneCrmControllerTest {

    private LaneCrmController controller;

    private CRMService service;

    @Before
    public void setUp() {
        this.service = mock(CRMService.class);
        this.controller = new LaneCrmController(this.service);
    }

    @Test
    public void testFormSubmitLanelibacqs() throws IOException {
        Model model = mock(Model.class);
        expect(model.asMap()).andReturn(Collections.singletonMap("foo", "bar"));
        expect(this.service.submitRequest(Collections.singletonMap("foo", "bar"))).andReturn(200);
        replay(model, this.service);
        assertEquals("redirect:/index.html", this.controller.formSubmitLanelibacqs(model, null));
        verify(model, this.service);
    }

    @Test
    public void testFormSubmitLanelibacqs500() throws IOException {
        Model model = mock(Model.class);
        expect(model.asMap()).andReturn(Collections.singletonMap("foo", "bar"));
        expect(this.service.submitRequest(Collections.singletonMap("foo", "bar"))).andReturn(500);
        replay(model, this.service);
        assertEquals("redirect:/error.html", this.controller.formSubmitLanelibacqs(model, null));
        verify(model, this.service);
    }

    @Test
    public void testFormSubmitLanelibacqsRedirect() throws IOException {
        Model model = mock(Model.class);
        expect(model.asMap()).andReturn(Collections.singletonMap("redirect", "/redirect"));
        expect(this.service.submitRequest(Collections.singletonMap("redirect", "/redirect"))).andReturn(200);
        replay(model, this.service);
        assertEquals("redirect:/redirect", this.controller.formSubmitLanelibacqs(model, null));
        verify(model, this.service);
    }

    @Test
    public void testJsonSubmitLanelibacqs() throws IOException {
        expect(this.service.submitRequest(Collections.emptyMap())).andReturn(200);
        replay(this.service);
        ResponseEntity<String> response = this.controller.jsonSubmitLanelibacqs(Collections.emptyMap());
        assertSame(HttpStatus.OK, response.getStatusCode());
        verify(this.service);
    }
}

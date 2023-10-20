package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.folio.rest.jaxrs.model.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.folio.UserService;
import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class FolioUserControllerTest {

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    private FolioUserController controller;

    private UserService folioUserService;

    private Map<String, Object> map;

    private Model model;

    private RedirectAttributes reqAttributes;

    private HttpServletRequest request;

    private EMailSender sender;

    private DataBinder userDataBinder;

    @Before
    public void setUp() throws Exception {
        this.folioUserService = mock(UserService.class);
        this.sender = mock(EMailSender.class);
        this.userDataBinder = mock(DataBinder.class);
        this.model = mock(Model.class);
        this.request = mock(HttpServletRequest.class);
        this.map = mock(Map.class);
        this.reqAttributes = mock(RedirectAttributes.class);
        this.controller = new FolioUserController(this.folioUserService, this.userDataBinder, this.sender);
    }

    @Test
    public final void testFormSubmitUserRegistration() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.getOrDefault("userid", "")).andReturn("userid@unknown");
        expect(this.map.getOrDefault("suEmail", "")).andReturn("suEmail");
        expect(this.map.getOrDefault("firstName", "")).andReturn("firstName");
        expect(this.map.getOrDefault("middleName", "")).andReturn("middleName");
        expect(this.map.getOrDefault("lastName", "")).andReturn("lastName");
        expect(this.map.getOrDefault("phone", "")).andReturn("phone");
        expect(this.map.getOrDefault("addressTypeId", "93d3d88d-499b-45d0-9bc7-ac73c3a19880"))
                .andReturn("addressTypeId");
        expect(this.map.getOrDefault("addressLine1", "")).andReturn("addressLine1");
        expect(this.map.getOrDefault("addressLine2", "")).andReturn("addressLine2");
        expect(this.map.getOrDefault("city", "")).andReturn("city");
        expect(this.map.getOrDefault("state", "")).andReturn("state");
        expect(this.map.getOrDefault("zip", "")).andReturn("zip");
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        expect(this.map.remove("user")).andReturn(null);
        expect(this.map.remove("auth")).andReturn(null);
        // expect(this.map.get("subject")).andReturn("subject");
        expect(this.folioUserService.addUser(isA(User.class))).andReturn(true);
        this.sender.sendEmail(this.map);
        replay(this.folioUserService, this.userDataBinder, this.sender, this.reqAttributes, this.model, this.map);
        String redirect = this.controller.formSubmitUserRegistration(this.model, this.reqAttributes);
        assertTrue(redirect.contains("confirmation"));
        verify(this.folioUserService, this.userDataBinder, this.sender, this.reqAttributes, this.model, this.map);
    }

    @Test
    public final void testFormSubmitUserRegistrationError() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.getOrDefault("userid", "")).andReturn("userid@unknown");
        expect(this.map.getOrDefault("suEmail", "")).andReturn("suEmail");
        expect(this.map.getOrDefault("firstName", "")).andReturn("firstName");
        expect(this.map.getOrDefault("middleName", "")).andReturn("middleName");
        expect(this.map.getOrDefault("lastName", "")).andReturn("lastName");
        expect(this.map.getOrDefault("phone", "")).andReturn("phone");
        expect(this.map.getOrDefault("addressTypeId", "93d3d88d-499b-45d0-9bc7-ac73c3a19880"))
                .andReturn("addressTypeId");
        expect(this.map.getOrDefault("addressLine1", "")).andReturn("addressLine1");
        expect(this.map.getOrDefault("addressLine2", "")).andReturn("addressLine2");
        expect(this.map.getOrDefault("city", "")).andReturn("city");
        expect(this.map.getOrDefault("state", "")).andReturn("state");
        expect(this.map.getOrDefault("zip", "")).andReturn("zip");
        expect(this.folioUserService.addUser(isA(User.class))).andReturn(false);
        replay(this.folioUserService, this.userDataBinder, this.sender, this.reqAttributes, this.model, this.map);
        String redirect = this.controller.formSubmitUserRegistration(this.model, this.reqAttributes);
        assertTrue(redirect.contains("error"));
        verify(this.folioUserService, this.userDataBinder, this.sender, this.reqAttributes, this.model, this.map);
    }

    @Test
    public final void testFormSubmitUserRegistrationException() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.getOrDefault("userid", "")).andReturn("userid@unknown");
        expect(this.map.getOrDefault("suEmail", "")).andReturn("suEmail");
        expect(this.map.getOrDefault("firstName", "")).andReturn("firstName");
        expect(this.map.getOrDefault("middleName", "")).andReturn("middleName");
        expect(this.map.getOrDefault("lastName", "")).andReturn("lastName");
        expect(this.map.getOrDefault("phone", "")).andReturn("phone");
        expect(this.map.getOrDefault("addressTypeId", "93d3d88d-499b-45d0-9bc7-ac73c3a19880"))
                .andReturn("addressTypeId");
        expect(this.map.getOrDefault("addressLine1", "")).andReturn("addressLine1");
        expect(this.map.getOrDefault("addressLine2", "")).andReturn("addressLine2");
        expect(this.map.getOrDefault("city", "")).andReturn("city");
        expect(this.map.getOrDefault("state", "")).andReturn("state");
        expect(this.map.getOrDefault("zip", "")).andReturn("zip");
        expect(this.folioUserService.addUser(isA(User.class))).andThrow(new RESTException(new IOException()));
        replay(this.folioUserService, this.userDataBinder, this.sender, this.reqAttributes, this.model, this.map);
        String redirect = this.controller.formSubmitUserRegistration(this.model, this.reqAttributes);
        assertTrue(redirect.contains("error"));
        verify(this.folioUserService, this.userDataBinder, this.sender, this.reqAttributes, this.model, this.map);
    }

    @Test
    public void testGetParameters() {
        expect(this.model.asMap()).andReturn(this.map);
        this.userDataBinder.bind(this.map, this.request);
        expect(this.request.getParameterMap()).andReturn(Collections.singletonMap("key", new String[] { "value" }));
        expect(this.model.addAttribute("key", "value")).andReturn(this.model);
        replay(this.userDataBinder, this.sender, this.reqAttributes, this.model, this.request);
        this.controller.getParameters(this.request, this.model);
        verify(this.userDataBinder, this.sender, this.reqAttributes, this.model, this.request);
    }
}

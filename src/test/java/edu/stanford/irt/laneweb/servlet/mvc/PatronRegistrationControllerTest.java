package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.folio.UserService;
import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public class PatronRegistrationControllerTest {

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    private PatronRegistrationController controller;

    private UserService folioUserService;

    private Map<String, Object> map;

    private Model model;

    private HttpServletRequest request;

    private EMailSender sender;

    private UserDataBinder userDataBinder;

    @Before
    public void setUp() throws Exception {
        this.folioUserService = mock(UserService.class);
        this.sender = mock(EMailSender.class);
        this.userDataBinder = mock(UserDataBinder.class);
        this.model = mock(Model.class);
        this.request = mock(HttpServletRequest.class);
        this.map = mock(Map.class);
        this.controller = new PatronRegistrationController(this.folioUserService, this.userDataBinder, this.sender);
    }

    @Test
    public final void testFormSubmitUserRegistration() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        expect(this.map.remove("user")).andReturn(null);
        expect(this.map.remove("auth")).andReturn(null);
        expect(this.map.remove("folio-user")).andReturn(null);
        expect(this.folioUserService.addUser(isA(Map.class))).andReturn(true);
        this.sender.sendEmail(this.map);
        replay(this.folioUserService, this.userDataBinder, this.sender, this.model, this.map);
        String redirect = this.controller.formSubmitUserRegistration(this.map, this.model);
        assertTrue(redirect.contains("confirmation"));
        verify(this.folioUserService, this.userDataBinder, this.sender,  this.model, this.map);
    }

    @Test
    public final void testFormSubmitUserRegistrationError() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.folioUserService.addUser(isA(Map.class))).andReturn(false);
        replay(this.folioUserService, this.userDataBinder, this.sender, this.model, this.map);
        String redirect = this.controller.formSubmitUserRegistration(this.map, this.model);
        assertTrue(redirect.contains("error"));
        verify(this.folioUserService, this.userDataBinder, this.sender, this.model, this.map);
    }

    @Test
    public final void testFormSubmitUserRegistrationException() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.folioUserService.addUser(isA(Map.class))).andThrow(new RESTException(new IOException()));
        replay(this.folioUserService, this.userDataBinder, this.sender,  this.model, this.map);
        String redirect = this.controller.formSubmitUserRegistration(this.map, this.model);
        assertTrue(redirect.contains("error"));
        verify(this.folioUserService, this.userDataBinder, this.sender, this.model, this.map);
    }

    @Test
    public final void testGetParameters() {
        expect(this.model.asMap()).andReturn(this.map);
        this.userDataBinder.bind(this.map, this.request);
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> personal = new HashMap<>();
        Map<String, Object> address = new HashMap<>();
        user.put("preferredContactTypeId", "002");
        user.put("departements", Collections.emptySet());
        user.put("proxyFor", Collections.emptySet());
        user.put("userName", "univ-user");
        personal.put("lastName", "value_lastName" );
        personal.put("firstName", "value_firstName" );
        personal.put("middleName", "value_middleName" );
        personal.put("email", "value_suEmail");
        personal.put("phone", "value_phone");
        address.put("addressLine1", "value_addressLine1");
        address.put("addressLine2", "value_addressLine2");
        address.put("city", "value_city");
        address.put("region", "value_state");
        address.put("postalCode", "value_zip");
        address.put("addressTypeId", "value_addressTypeId");
        personal.put("addresses", Collections.singleton(address));
        user.put("personal", personal);
        expect(this.model.getAttribute("userid")).andReturn("univ-user@stanford.edu");
        expect(this.model.addAttribute("userid", "univ-user")).andReturn(this.model);
        expect(this.request.getParameter("lastName")).andReturn("value_lastName").times(2);
        expect(this.model.addAttribute("lastName", "value_lastName")).andReturn(this.model);
        expect(this.request.getParameter("firstName")).andReturn("value_firstName").times(2);
        expect(this.model.addAttribute("firstName", "value_firstName")).andReturn(this.model);
        expect(this.request.getParameter("middleName")).andReturn("value_middleName").times(2);
        expect(this.model.addAttribute("middleName", "value_middleName")).andReturn(this.model);
        expect(this.request.getParameter("suEmail")).andReturn("value_suEmail").times(2);
        expect(this.model.addAttribute("suEmail", "value_suEmail")).andReturn(this.model);
        expect(this.request.getParameter("phone")).andReturn("value_phone").times(2);
        expect(this.model.addAttribute("phone", "value_phone")).andReturn(this.model);
        expect(this.request.getParameter("addressLine1")).andReturn("value_addressLine1").times(2);
        expect(this.model.addAttribute("addressLine1", "value_addressLine1")).andReturn(this.model);
        expect(this.request.getParameter("addressLine2")).andReturn("value_addressLine2").times(2);
        expect(this.model.addAttribute("addressLine2", "value_addressLine2")).andReturn(this.model);
        expect(this.request.getParameter("city")).andReturn("value_city").times(2);
        expect(this.model.addAttribute("city", "value_city")).andReturn(this.model);
        expect(this.request.getParameter("state")).andReturn("value_state").times(2);
        expect(this.model.addAttribute("state", "value_state")).andReturn(this.model);
        expect(this.request.getParameter("zip")).andReturn("value_zip").times(2);
        expect(this.model.addAttribute("zip", "value_zip")).andReturn(this.model);
        expect(this.request.getParameter("addressTypeId")).andReturn("value_addressTypeId").times(2);
        expect(this.model.addAttribute("addressTypeId", "value_addressTypeId")).andReturn(this.model);
        expect(this.model.addAttribute("folio-user", user)).andReturn(this.model);
        expect(this.request.getParameter("subject")).andReturn("value_subject");
           expect(this.model.addAttribute("subject", "value_subject")).andReturn(this.model);
        
        replay(this.userDataBinder, this.sender,  this.model, this.request);
        this.controller.getParameters(this.request, this.model);
        verify(this.userDataBinder, this.sender, this.model, this.request);
        
    }
    
}

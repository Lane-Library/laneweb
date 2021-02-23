package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.stanford.irt.laneweb.email.EMailSender;

public class LaneCrmControllerTest {

    private LaneCrmController controller;

    private EMailSender emailSender;

    private HttpServletRequest request;

    Map<String, Object> emailContent;

    private static String NEXT_PAGE = "redirect:/contacts/sfp-confirmation.html";



//
//    @Before
//    public void setUp() {
//        this.emailSender = mock(EMailSender.class);
//        this.request = mock(HttpServletRequest.class);
//        this.controller = new LaneCrmController(this.emailSender, "email@address.com");
//        emailContent = new HashMap<String, Object>();
//        emailContent.put("subject", "SFP:ARRIVAL");
//        emailContent.put("IP", "ip");
//        emailContent.put("recipient", "email@address.com");
//    }

//
//    @Test
//    public void testSendEmail() throws IOException {
//        emailContent.put("json", "{\"requestedBy.email\":\"test@stanford.edu\"}");
//        emailContent.put("requestedBy.email", "test@stanford.edu");
//        expect(this.request.getParameterMap());
//        expect(this.request.getRemoteAddr()).andReturn("ip");
//        this.emailSender.sendEmail(emailContent);
//        replay(this.request, this.emailSender);
//        String nextPage = this.controller.sendEmail(this.request);
//        assertSame( NEXT_PAGE , nextPage);
//        verify(this.emailSender, this.request);
//    }
//
//    @Test
//    public void testShcValidEmail() throws IOException {
//        emailContent.put("json", "{\"requestedBy.email\":\"test@stanfordhealthcare.org\"}");
//        emailContent.put("requestedBy.email", "test@stanfordhealthcare.org");
//        expect(this.request.getParameterMap());
//
//        expect(this.request.getRemoteAddr()).andReturn("ip");
//        expect(this.request.getParameter("requestedBy.email")).andReturn( "test@stanfordhealthcare.org");
//
//        this.emailSender.sendEmail(emailContent);
//        replay(this.request, this.emailSender);
//        String nextPage =  this.controller.sendEmail( this.request);
//        assertSame(NEXT_PAGE, nextPage);
//        verify(this.emailSender, this.request);
//    }
//
//     @Test
//     public void testLpchValidEmail() throws IOException {
//         emailContent.put("json", "{\"requestedBy.email\":\"test@stanfordchildrens.org\"}");
//         emailContent.put("requestedBy.email", "test@stanfordchildrens.org");
//         expect(this.request.getParameterMap());
//
//         expect(this.request.getRemoteAddr()).andReturn("ip");
//         expect(this.request.getParameter("requestedBy.email")).andReturn( "test@stanfordchildrens.org");
//         this.emailSender.sendEmail(emailContent);
//         replay(this.request, this.emailSender);
//         String nextPage =  this.controller.sendEmail( this.request);
//         assertSame(NEXT_PAGE, nextPage);
//         verify(this.emailSender, this.request);
//     }

//     @Test
//     public void testNotValidEmail() throws IOException {
//       expect(this.request.getParameter("requestedBy.email")).andReturn( "test@stanfordchil.org");
//       List<String> httParameters =  new ArrayList();
//       httParameters.add("requestedBy.email");
//       expect(this.request.getParameterNames()).andReturn(Collections.enumeration());
//       replay(this.request);
//       String nextPage =  this.controller.sendEmail( this.request);
//       assertSame(NEXT_PAGE, nextPage);
//       verify(this.emailSender, this.request);
//     }





}

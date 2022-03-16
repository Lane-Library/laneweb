package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RequestHeaderDataBinder;
import edu.stanford.irt.laneweb.spam.SpamService;

public class EMailControllerTest {

    private static final String ERROR_PAGE = "redirect:/error_upload_file.html";

    private static final String NEXT_EJP_PAGE = "redirect:/contacts/ejp-confirmation.html";

    private static final String NEXT_PAGE = "redirect:/contacts/confirmation.html";

    private RedirectAttributes atts;

    private EMailController controller;

    private RequestHeaderDataBinder headerBinder;

    private Map<String, Object> map;

    private Model model;

    private MultipartFile multipartFile;

    private RemoteProxyIPDataBinder remoteIPBinder;

    private HttpServletRequest request;

    private EMailSender sender;

    private SpamService spamService;

    @Before
    public void setUp() throws Exception {
        this.headerBinder = mock(RequestHeaderDataBinder.class);
        this.remoteIPBinder = mock(RemoteProxyIPDataBinder.class);
        this.spamService = mock(SpamService.class);
        this.sender = mock(EMailSender.class);
        this.controller = new EMailController(this.headerBinder, this.remoteIPBinder, this.sender, this.spamService);
        this.atts = mock(RedirectAttributes.class);
        this.model = mock(Model.class);
        this.request = mock(HttpServletRequest.class);
        this.map = mock(Map.class);
        this.multipartFile = mock(MultipartFile.class);
    }

    @Test
    public void testFormSubmitAskUs() throws IllegalStateException, IOException {
        expect(this.multipartFile.isEmpty()).andReturn(true).times(2);
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("email")).andReturn("email").times(2);
        expect(this.map.get("name")).andReturn("name").times(2);
        expect(this.map.get("subject")).andReturn("subject");
        expect(this.map.get("name")).andReturn("name");
        expect(this.map.put("subject", "subject (name)")).andReturn(null);
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(false);
        this.sender.sendEmail(this.map, null);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
        String nextPage = this.controller.formSubmitAskUs(this.model, this.multipartFile, this.atts);
        assertSame(NEXT_PAGE, nextPage);
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
    }

    @Test
    public void testFormSubmitAskUsAttachmentSizeError() throws IllegalStateException, IOException {
        StringBuilder image = new StringBuilder(
                "data:image/gif;base64,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");
        for (int i = 0; i < 3000; i++) {
            image.append(
                    "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");
        }
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("email")).andReturn("email").times(2);
        expect(this.map.get("name")).andReturn("name").times(2);
        expect(this.multipartFile.isEmpty()).andReturn(false).times(2);
        expect(this.multipartFile.getContentType()).andReturn("image/jpeg");
        expect(this.multipartFile.getOriginalFilename()).andReturn("filename.jpeg");
        expect(this.multipartFile.getBytes()).andReturn(image.toString().getBytes());
        expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(false);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
        assertEquals(ERROR_PAGE, this.controller.formSubmitAskUs(this.model, this.multipartFile, this.atts));
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
    }

    @Test
    public void testFormSubmitAskUsContentTypeError() throws IllegalStateException, IOException {
        expect(this.multipartFile.isEmpty()).andReturn(false).times(2);
        expect(this.multipartFile.getContentType()).andReturn("asdfads");
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("name")).andReturn("name").times(2);
        expect(this.map.get("email")).andReturn("email").times(2);
        expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(false);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
        assertEquals(ERROR_PAGE, this.controller.formSubmitAskUs(this.model, this.multipartFile, this.atts));
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
    }

    @Test
    public void testFormSubmitAskUsWithAttachment() throws IllegalStateException, IOException {
        File file = new File("filename.jpeg");
        String image = "data:image/gif;base64,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";
        expect(this.multipartFile.isEmpty()).andReturn(false).times(1);
        expect(this.multipartFile.getContentType()).andReturn("image/jpeg");
        expect(this.multipartFile.getOriginalFilename()).andReturn("filename.jpeg");
        expect(this.multipartFile.getBytes()).andReturn(image.toString().getBytes());
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("name")).andReturn("name").times(2);
        expect(this.map.get("email")).andReturn("email").times(2);
        expect(this.map.get("subject")).andReturn("subject");
        expect(this.map.get("name")).andReturn("name");
        expect(this.map.put("subject", "subject (name)")).andReturn(null);
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(false);
        this.sender.sendEmail(this.map, file);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
        this.controller.formSubmitAskUs(this.model, this.multipartFile, this.atts);
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
    }

    @Test
    public void testFormSubmitEjp() throws IllegalStateException, IOException {
        expect(this.multipartFile.isEmpty()).andReturn(true).times(2);
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("email")).andReturn("email").times(2);
        expect(this.map.get("name")).andReturn("name").times(2);
        expect(this.map.get("subject")).andReturn("[ejp]");
        expect(this.map.get("title")).andReturn("title");
        expect(this.map.put("subject", "[ejp] title")).andReturn(null);
        expect(this.spamService.isSpam("ejp", this.map)).andReturn(false);
        expect(this.map.put("recipient", "ejproblem@lists.stanford.edu")).andReturn(null);
        this.sender.sendEmail(this.map, null);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
        String nextPage = this.controller.formSubmitEJP(this.model, this.multipartFile, this.atts);
        assertSame(NEXT_EJP_PAGE, nextPage);
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
    }

    @Test
    public void testFormSubmitEjpWithAttachment() throws IllegalStateException, IOException {
        File file = new File("filename.jpeg");
        String image = "data:image/gif;base64,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";
        expect(this.multipartFile.isEmpty()).andReturn(false).times(1);
        expect(this.multipartFile.getContentType()).andReturn("image/jpeg");
        expect(this.multipartFile.getOriginalFilename()).andReturn("filename.jpeg");
        expect(this.multipartFile.getBytes()).andReturn(image.toString().getBytes());
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("email")).andReturn("email").times(2);
        expect(this.map.get("name")).andReturn("name").times(2);
        expect(this.map.get("subject")).andReturn("[ejp]");
        expect(this.map.get("title")).andReturn("title");
        expect(this.map.put("subject", "[ejp] title")).andReturn(null);
        expect(this.map.put("recipient", "ejproblem@lists.stanford.edu")).andReturn(null);
        expect(this.spamService.isSpam("ejp", this.map)).andReturn(false);
        this.sender.sendEmail(this.map, file);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
        String nextPage = this.controller.formSubmitEJP(this.model, this.multipartFile, this.atts);
        assertSame(NEXT_EJP_PAGE, nextPage);
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
    }

    @Test
    public void testGetParameters() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.request.getParameter("question")).andReturn("question");
        this.remoteIPBinder.bind(this.map, this.request);
        this.headerBinder.bind(this.map, this.request);
        expect(this.request.getParameterMap()).andReturn(Collections.singletonMap("key", new String[] { "value" }));
        expect(this.model.addAttribute("key", "value")).andReturn(this.model);
        replay(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.request);
        this.controller.getParameters(this.request, this.model);
        verify(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.request);
    }

    @Test(expected = LanewebException.class)
    public void testGetParametersMultipleValues() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.request.getParameter("question")).andReturn("question");
        this.remoteIPBinder.bind(this.map, this.request);
        this.headerBinder.bind(this.map, this.request);
        expect(this.request.getParameterMap())
                .andReturn(Collections.singletonMap("key", new String[] { "value", "anothervalue" }));
        expect(this.model.addAttribute("key", "value")).andReturn(this.model);
        expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(false);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model,
                this.request);
        this.controller.getParameters(this.request, this.model);
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model,
                this.request);
    }

    @Test
    public void testRedirectToIndex() throws IllegalStateException, IOException {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("subject")).andReturn("subject");
        expect(this.map.get("name")).andReturn("name");
        expect(this.map.put("subject", "subject (name)")).andReturn(null);
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        expect(this.map.get("redirect")).andReturn(null);
        expect(this.map.get("referrer")).andReturn(null);
        expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(false);
        this.sender.sendEmail(this.map);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
        assertEquals("redirect:/index.html", this.controller.submitAskUs(this.model, this.atts));
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
    }

    @Test
    public void testRedirectToReferrer() throws IllegalStateException, IOException {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("subject")).andReturn("subject");
        expect(this.map.get("name")).andReturn("name");
        expect(this.map.put("subject", "subject (name)")).andReturn(null);
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        expect(this.map.get("redirect")).andReturn(null);
        expect(this.map.get("referrer")).andReturn("referrer");
        expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(false);
        this.sender.sendEmail(this.map);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
        assertEquals("redirect:referrer", this.controller.submitAskUs(this.model, this.atts));
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
    }

    @Test
    public void testSpam() throws IllegalStateException, IOException {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("subject")).andReturn("subject");
        expect(this.map.get("name")).andReturn("name");
        expect(this.map.put("subject", "subject (name)")).andReturn(null);
        expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(true);
        replay(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
        assertEquals("redirect:/error.html", this.controller.submitAskUs(this.model, this.atts));
        verify(this.spamService, this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map,
                this.multipartFile);
    }
}

package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;
import edu.stanford.irt.laneweb.spam.SpamService;

public class EMailControllerTest {
        private static final String DOCXPRESS_ADDRESS = "docxpress@lists.stanford.edu";

        private RedirectAttributes atts;

        private EMailController controller;

        private DataBinder emailDataBinder;

        private Map<String, Object> map;

        private Model model;

        private HttpServletRequest request;

        private EMailSender sender;

        private SpamService spamService;

        @BeforeEach
        public void setUp() throws Exception {
                this.emailDataBinder = mock(DataBinder.class);
                this.spamService = mock(SpamService.class);
                this.sender = mock(EMailSender.class);
                this.controller = new EMailController(this.emailDataBinder, this.sender, this.spamService);
                this.atts = mock(RedirectAttributes.class);
                this.model = mock(Model.class);
                this.request = mock(HttpServletRequest.class);
                this.map = mock(Map.class);
        }

        @Test
        public void testFormSubmitDocXpress() throws IllegalStateException, IOException {
                expect(this.model.asMap()).andReturn(this.map);
                expect(this.map.put("recipient", DOCXPRESS_ADDRESS)).andReturn(null);
                expect(this.map.get("redirect")).andReturn(null);
                expect(this.map.get("referrer")).andReturn(null);
                expect(this.spamService.isSpam("docxpress", this.map)).andReturn(false);
                this.sender.sendEmail(this.map);
                replay(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model, this.map);
                String nextPage = this.controller.formSubmitDocxpress(this.model, this.atts);
                assertEquals("redirect:/index.html", nextPage);
                verify(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model, this.map);
        }

        @Test
        public void testGetParameters() {
                expect(this.model.asMap()).andReturn(this.map);
                expect(this.request.getParameter("question")).andReturn("question");
                this.emailDataBinder.bind(this.map, this.request);
                expect(this.request.getParameterMap())
                                .andReturn(Collections.singletonMap("key", new String[] { "value" }));
                expect(this.model.addAttribute("key", "value")).andReturn(this.model);
                replay(this.emailDataBinder, this.sender, this.atts, this.model, this.request);
                this.controller.getParameters(this.request, this.model);
                verify(this.emailDataBinder, this.sender, this.atts, this.model, this.request);
        }

        @Test
        public void testGetParametersMultipleValues() {
                expect(this.model.asMap()).andReturn(this.map);
                expect(this.request.getParameter("question")).andReturn("question");
                this.emailDataBinder.bind(this.map, this.request);
                this.emailDataBinder.bind(this.map, this.request);
                expect(this.request.getParameterMap())
                                .andReturn(Collections.singletonMap("key", new String[] { "value", "anothervalue" }));
                expect(this.model.addAttribute("key", "value")).andReturn(this.model);
                expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(false);
                replay(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model, this.request);
                assertThrows(LanewebException.class, () -> {
                        this.controller.getParameters(this.request, this.model);
                        verify(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model,
                                        this.request);
                });

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
                replay(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model, this.map);
                assertEquals("redirect:/index.html", this.controller.submitAskUs(this.model, this.atts));
                verify(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model, this.map);
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
                replay(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model, this.map);
                assertEquals("redirect:referrer", this.controller.submitAskUs(this.model, this.atts));
                verify(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model, this.map);
        }

        @Test
        public void testSpam() throws IllegalStateException, IOException {
                expect(this.model.asMap()).andReturn(this.map);
                expect(this.map.get("subject")).andReturn("subject");
                expect(this.map.get("name")).andReturn("name");
                expect(this.map.put("subject", "subject (name)")).andReturn(null);
                expect(this.spamService.isSpam("laneaskus", this.map)).andReturn(true);
                replay(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model, this.map);
                assertEquals("redirect:/error.html", this.controller.submitAskUs(this.model, this.atts));
                verify(this.spamService, this.emailDataBinder, this.sender, this.atts, this.model, this.map);
        }
}

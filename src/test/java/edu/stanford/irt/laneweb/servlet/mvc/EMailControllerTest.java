package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RequestHeaderDataBinder;

public class EMailControllerTest {

    private RedirectAttributes atts;

    private EMailController controller;

    private RequestHeaderDataBinder headerBinder;

    private Map<String, Object> map;

    private Model model;

    private RemoteProxyIPDataBinder remoteIPBinder;

    private HttpServletRequest request;

    private EMailSender sender;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.headerBinder = mock(RequestHeaderDataBinder.class);
        this.remoteIPBinder = mock(RemoteProxyIPDataBinder.class);
        this.sender = mock(EMailSender.class);
        this.controller = new EMailController(this.headerBinder, this.remoteIPBinder, this.sender);
        this.atts = mock(RedirectAttributes.class);
        this.model = mock(Model.class);
        this.request = mock(HttpServletRequest.class);
        this.map = mock(Map.class);
    }

    @Test
    public void testFormSubmitAskUs() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("subject")).andReturn("subject");
        expect(this.map.get("name")).andReturn("name");
        expect(this.map.put("subject", "subject (name)")).andReturn(null);
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        this.sender.sendEmail(this.map);
        expect(this.map.get("redirect")).andReturn("redirect");
        replay(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
        this.controller.formSubmitAskUs(this.model, this.atts);
        verify(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
    }

    @Test
    public void testFormSubmitLaneissue() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.put("recipient", "lane-issue@med.stanford.edu")).andReturn(null);
        this.sender.sendEmail(this.map);
        expect(this.map.get("redirect")).andReturn("redirect");
        replay(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
        this.controller.formSubmitLaneissue(this.model, this.atts);
        verify(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
    }

    @Test
    public void testGetParameters() {
        expect(this.model.asMap()).andReturn(this.map);
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
        this.remoteIPBinder.bind(this.map, this.request);
        this.headerBinder.bind(this.map, this.request);
        expect(this.request.getParameterMap())
                .andReturn(Collections.singletonMap("key", new String[] { "value", "anothervalue" }));
        expect(this.model.addAttribute("key", "value")).andReturn(this.model);
        replay(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.request);
        this.controller.getParameters(this.request, this.model);
        verify(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.request);
    }

    @Test
    public void testJsonSubmitAskUs() {
        expect(this.model.asMap()).andReturn(this.map);
        this.map.putAll(this.map);
        expect(this.map.get("subject")).andReturn("subject");
        expect(this.map.get("name")).andReturn("name");
        expect(this.map.put("subject", "subject (name)")).andReturn(null);
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        this.sender.sendEmail(this.map);
        replay(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
        this.controller.jsonSubmitAskUs(this.map, this.model);
        verify(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
    }

    @Test
    public void testJsonSubmitLaneissue() {
        expect(this.model.asMap()).andReturn(this.map);
        this.map.putAll(this.map);
        expect(this.map.put("recipient", "lane-issue@med.stanford.edu")).andReturn(null);
        this.sender.sendEmail(this.map);
        replay(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
        this.controller.jsonSubmitLaneissue(this.map, this.model);
        verify(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
    }

    @Test
    public void testRedirectToIndex() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("subject")).andReturn("subject");
        expect(this.map.get("name")).andReturn("name");
        expect(this.map.put("subject", "subject (name)")).andReturn(null);
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        this.sender.sendEmail(this.map);
        expect(this.map.get("redirect")).andReturn(null);
        expect(this.map.get("referrer")).andReturn(null);
        replay(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
        assertEquals("redirect:/index.html", this.controller.formSubmitAskUs(this.model, this.atts));
        verify(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
    }

    @Test
    public void testRedirectToReferrer() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.map.get("subject")).andReturn("subject");
        expect(this.map.get("name")).andReturn("name");
        expect(this.map.put("subject", "subject (name)")).andReturn(null);
        expect(this.map.put("recipient", "LaneAskUs@stanford.edu")).andReturn(null);
        this.sender.sendEmail(this.map);
        expect(this.map.get("redirect")).andReturn(null);
        expect(this.map.get("referrer")).andReturn("referrer");
        replay(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
        assertEquals("redirect:referrer", this.controller.formSubmitAskUs(this.model, this.atts));
        verify(this.headerBinder, this.remoteIPBinder, this.sender, this.atts, this.model, this.map);
    }
}

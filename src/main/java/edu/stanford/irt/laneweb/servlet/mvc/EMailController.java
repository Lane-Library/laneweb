package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RequestHeaderDataBinder;

@Controller
@RequestMapping(value = "/apps/mail", method = RequestMethod.POST)
public class EMailController {

    @Autowired
    private RequestHeaderDataBinder headerBinder;

    @Autowired
    private RemoteProxyIPDataBinder remoteIPBinder;

    @Autowired
    private EMailSender sender;

    @RequestMapping(value = "/askus", consumes = "application/x-www-form-urlencoded")
    public String formSubmitAskUs(final Model model, final RedirectAttributes atts) {
        appendNameToSubject(model.asMap());
        sendEmail("LaneAskUs@stanford.edu", model.asMap());
        return getRedirectTo(model.asMap());
    }

    @RequestMapping(value = "/lane-issue", consumes = "application/x-www-form-urlencoded")
    public String formSubmitLaneissue(final Model model, final RedirectAttributes atts) {
        sendEmail("lane-issue@med.stanford.edu", model.asMap());
        return getRedirectTo(model.asMap());
    }

    @RequestMapping(value = "/lanelibacqs", consumes = "application/x-www-form-urlencoded")
    public String formSubmitLanelibacqs(final Model model, final RedirectAttributes atts) {
        sendEmail("lanelibacqs@lists.stanford.edu", model.asMap());
        return getRedirectTo(model.asMap());
    }

    @RequestMapping(value = "/askus", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitAskUs(@RequestBody final Map<String, Object> feedback, final Model model) {
        feedback.putAll(model.asMap());
        appendNameToSubject(feedback);
        sendEmail("LaneAskUs@stanford.edu", feedback);
    }

    @RequestMapping(value = "/lane-issue", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitLaneissue(@RequestBody final Map<String, Object> feedback, final Model model) {
        feedback.putAll(model.asMap());
        sendEmail("lane-issue@med.stanford.edu", feedback);
    }

    @RequestMapping(value = "/lanelibacqs", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitLanelibacqs(@RequestBody final Map<String, Object> feedback, final Model model) {
        feedback.putAll(model.asMap());
        sendEmail("lanelibacqs@lists.stanford.edu", feedback);
    }
    
    private void appendNameToSubject(Map<String, Object> feedback) {
        StringBuilder subject = new StringBuilder((String)feedback.get("subject"));
        subject.append(" (").append(feedback.get("name")).append(")");
        feedback.put("subject", subject.toString());
    }

    private String getRedirectTo(final Map<String, Object> map) {
        String redirectTo = (String) map.get("redirect");
        if (redirectTo == null) {
            redirectTo = (String) map.get(edu.stanford.irt.laneweb.model.Model.REFERRER);
        }
        if (redirectTo == null) {
            redirectTo = "/index.html";
        }
        return "redirect:" + redirectTo;
    }

    private void sendEmail(final String recipient, final Map<String, Object> data) {
        data.put("recipient", recipient);
        this.sender.sendEmail(data);
    }

    @ModelAttribute
    protected void getParameters(final HttpServletRequest request, final Model model) {
        this.remoteIPBinder.bind(model.asMap(), request);
        this.headerBinder.bind(model.asMap(), request);
        @SuppressWarnings("unchecked")
        Map<String, String[]> map = request.getParameterMap();
        for (Entry<String, String[]> entry : map.entrySet()) {
            String[] value = entry.getValue();
            if (value.length == 1) {
                model.addAttribute(entry.getKey(), value[0]);
            } else {
                throw new LanewebException("multiple values for parameter " + entry.getKey());
            }
        }
    }
}

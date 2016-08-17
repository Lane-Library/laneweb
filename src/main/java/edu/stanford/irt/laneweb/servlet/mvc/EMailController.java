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

// TODO: the individual methods for each address probably can be combined
@Controller
@RequestMapping(value = "/apps/mail", method = RequestMethod.POST)
public class EMailController {

    private static final String ASKUS_ADDRESS = "LaneAskUs@stanford.edu";

    private static final String ASKUS_PATH = "/askus";

    private static final String DOCXPRESS_ADDRESS = "docxpress@lists.stanford.edu";

    private static final String DOCXPRESS_PATH = "/docxpress";

    private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final String ISSUE_ADDRESS = "lane-issue@med.stanford.edu";

    private static final String ISSUE_PATH = "/lane-issue";

    private static final String JSON_MIME_TYPE = "application/json";

    private static final String SUBJECT = "subject";

    @Autowired
    private RequestHeaderDataBinder headerBinder;

    @Autowired
    private RemoteProxyIPDataBinder remoteIPBinder;

    @Autowired
    private EMailSender sender;

    public EMailController() {
        // empty default constructor
    }

    public EMailController(final RequestHeaderDataBinder headerBinder, final RemoteProxyIPDataBinder remoteIPBinder,
            final EMailSender sender) {
        this.headerBinder = headerBinder;
        this.remoteIPBinder = remoteIPBinder;
        this.sender = sender;
    }

    @RequestMapping(value = ASKUS_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitAskUs(final Model model, final RedirectAttributes atts) {
        Map<String, Object> map = model.asMap();
        appendNameToSubject(map);
        sendEmail(ASKUS_ADDRESS, map);
        return getRedirectTo(map);
    }

    @RequestMapping(value = DOCXPRESS_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitDocxpress(final Model model, final RedirectAttributes atts) {
        Map<String, Object> map = model.asMap();
        sendEmail(DOCXPRESS_ADDRESS, map);
        return getRedirectTo(map);
    }

    @RequestMapping(value = ISSUE_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitLaneissue(final Model model, final RedirectAttributes atts) {
        Map<String, Object> map = model.asMap();
        sendEmail(ISSUE_ADDRESS, map);
        return getRedirectTo(map);
    }

    @RequestMapping(value = ASKUS_PATH, consumes = JSON_MIME_TYPE)
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitAskUs(@RequestBody final Map<String, Object> feedback, final Model model) {
        feedback.putAll(model.asMap());
        appendNameToSubject(feedback);
        sendEmail(ASKUS_ADDRESS, feedback);
    }

    @RequestMapping(value = DOCXPRESS_PATH, consumes = JSON_MIME_TYPE)
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitDocxpress(@RequestBody final Map<String, Object> feedback, final Model model) {
        feedback.putAll(model.asMap());
        sendEmail(DOCXPRESS_ADDRESS, feedback);
    }

    @RequestMapping(value = ISSUE_PATH, consumes = JSON_MIME_TYPE)
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitLaneissue(@RequestBody final Map<String, Object> feedback, final Model model) {
        feedback.putAll(model.asMap());
        sendEmail(ISSUE_ADDRESS, feedback);
    }

    @ModelAttribute
    protected void getParameters(final HttpServletRequest request, final Model model) {
        Map<String, Object> modelMap = model.asMap();
        this.remoteIPBinder.bind(modelMap, request);
        this.headerBinder.bind(modelMap, request);
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

    private void appendNameToSubject(final Map<String, Object> feedback) {
        StringBuilder subject = new StringBuilder((String) feedback.get(SUBJECT));
        subject.append(" (").append(feedback.get("name")).append(")");
        feedback.put(SUBJECT, subject.toString());
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
}

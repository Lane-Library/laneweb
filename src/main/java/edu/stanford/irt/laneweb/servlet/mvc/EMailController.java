package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;
import edu.stanford.irt.laneweb.spam.SpamService;

@Controller
@RequestMapping(value = "/apps/mail/")
public class EMailController {

    private static final String ASKUS_ADDRESS = "LaneAskUs@stanford.edu";

    private static final String ASKUS_PATH = "askus";

    private static final String ASKUS_PORTAL = "laneaskus";

    private static final String DOCXPRESS_ADDRESS = "docxpress@lists.stanford.edu";

    private static final String DOCXPRESS_PATH = "docxpress";

    private static final String DOCXPRESS_PORTAL = "docxpress";

    private static final String ERROR_PAGE = "redirect:/error.html";

    private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final String QUESTION = "question";

    private static final String SUBJECT = "subject";

    private EMailSender sender;

    private SpamService spamService;

    private DataBinder emailDataBinder;

    public EMailController(
            @Qualifier("edu.stanford.irt.laneweb.servlet.binding.DataBinder/email") final DataBinder dataBinder,
            final EMailSender sender, final SpamService spamService) {
        this.emailDataBinder = dataBinder;
        this.sender = sender;
        this.spamService = spamService;
    }

    // Form coming from docxpress.stanford.edu
    @PostMapping(value = DOCXPRESS_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitDocxpress(final Model model, final RedirectAttributes atts) {
        Map<String, Object> map = model.asMap();
        if (this.spamService.isSpam(DOCXPRESS_PORTAL, map)) {
            return ERROR_PAGE;
        }
        sendEmail(DOCXPRESS_ADDRESS, map);
        return getRedirectTo(map);
    }

    // Form from the error 404 page
    @PostMapping(value = ASKUS_PATH, consumes = FORM_MIME_TYPE)
    public String submitAskUs(final Model model, final RedirectAttributes atts) throws IllegalStateException {
        Map<String, Object> map = model.asMap();
        appendNameToSubject(map);
        if (this.spamService.isSpam(ASKUS_PORTAL, map)) {
            return ERROR_PAGE;
        }
        sendEmail(ASKUS_ADDRESS, map);
        return getRedirectTo(map);
    }

    private void appendNameToSubject(final Map<String, Object> feedback) {
        StringBuilder subject = new StringBuilder((String) feedback.get(SUBJECT));
        subject.append(" (").append(feedback.get("name")).append(')');
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

    @ModelAttribute
    protected void getParameters(final HttpServletRequest request, final Model model) {
        Map<String, Object> modelMap = model.asMap();
        Map<String, String[]> map = request.getParameterMap();
        String question = request.getParameter(QUESTION);
        if (question != null) {
            modelMap.put(QUESTION, question.concat("\n\n"));
            map.remove(QUESTION);
        }
        this.emailDataBinder.bind(modelMap, request);
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

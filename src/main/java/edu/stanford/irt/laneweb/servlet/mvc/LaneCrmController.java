package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.email.EMailSender;

@Controller
public class LaneCrmController {

    private static final String ERROR_URL = "/error.html";

    private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final String JSON_MIME_TYPE = "application/json";

    private static final String LANELIBACQ_PATH = "/apps/lanelibacqs";

    private static final String[] VALID_EMAILS = { ".*@stanford.edu$", ".*@stanfordhealthcare.org$",  ".*@stanfordchildrens.org$" };

    private static final String SUBJECT = "subject";

    private static final String SUBJECT_CONTENT = "SFP:ARRIVAL";

    private String sfpEmailAddress;

    private EMailSender sender;

    @Autowired
    public LaneCrmController(final EMailSender sender,
            @Value("${edu.stanford.irt.laneweb.acquisition.email}") String sfpEmail) {
        this.sender = sender;
        this.sfpEmailAddress = sfpEmail;
    }

    @PostMapping(value = LANELIBACQ_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitLanelibacqs(final RedirectAttributes atts, HttpServletRequest request) {
        return "redirect:" + ERROR_URL;
    }

    @PostMapping(value = LANELIBACQ_PATH, consumes = JSON_MIME_TYPE)
    public ResponseEntity<String> sendEmail(@RequestBody final  Map<String, Object> feedback,   final HttpServletRequest request) throws JsonProcessingException {
        if (!validateStanfordEmail(feedback)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> emailContent = new HashMap<>();
        emailContent.putAll(feedback);
        emailContent.put("json", om.writeValueAsString(feedback));
        emailContent.put("IP", request.getRemoteAddr());
        emailContent.put(SUBJECT, SUBJECT_CONTENT);
        emailContent.put("recipient", this.sfpEmailAddress);
        this.sender.sendEmail(emailContent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean validateStanfordEmail(Map<String, Object> httpParameters) {
        String email = (String) httpParameters.get("requestedBy.email");
        if (null == email || "".equals(email)) {
            return false;
        }
        for (String emailValidation : VALID_EMAILS) {
            if (email.matches(emailValidation)) {
                return true;
            }
        }
        return false;
    }
}

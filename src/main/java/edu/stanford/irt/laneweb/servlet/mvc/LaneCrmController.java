package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.email.EMailSender;

@Controller
public class LaneCrmController {

  private static final String ERROR_URL = "/error.html";

  private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";



  private static final String LANELIBACQ_PATH = "/apps/lanelibacqs";

  private static final String[] VALID_EMAILS = { ".*@stanford.edu$", ".*@stanfordhealthcare.org$",
      ".*@stanfordchildrens.org$" };

  private static final String SUBJECT = "subject";

  private static final String SUBJECT_CONTENT = "SFP:ARRIVAL";

  private String sfpEmailAddress;

  private EMailSender sender;

  @Autowired
  public LaneCrmController(final EMailSender sender, @Value("${edu.stanford.irt.laneweb.acquisition.email}") String sfpEmail) {
    this.sender = sender;
    this.sfpEmailAddress = sfpEmail;
  }

  @PostMapping(value = LANELIBACQ_PATH, consumes = FORM_MIME_TYPE)
  public String sendEmail( final HttpServletRequest request) throws JsonProcessingException {
    Map<String, String> data = this.getParameterMap(request);
    if (!validateStanfordEmail(request)) {
      return ERROR_URL;
    }
    ObjectMapper om = new ObjectMapper();
    Map<String, Object> emailContent = new HashMap<>();
    emailContent.putAll(data);
    emailContent.put("json", om.writeValueAsString(data));
    emailContent.put("IP", request.getRemoteAddr());
    emailContent.put(SUBJECT, SUBJECT_CONTENT);
    emailContent.put("recipient", this.sfpEmailAddress);

    System.out.println(emailContent);

//    this.sender.sendEmail(emailContent);
    return "redirect:/contacts/sfp-confirmation.html";
  }

  private boolean validateStanfordEmail(final HttpServletRequest request) {
    String email = (String) request.getParameter("requestedBy.email");
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

  private Map<String, String> getParameterMap(final HttpServletRequest request) {
    Map<String, String> parameterMap = new HashMap<String, String>();
    Enumeration<String> parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String key = (String) parameterNames.nextElement();
      parameterMap.put(key, request.getParameter(key));
    }
    return parameterMap;
  }

}

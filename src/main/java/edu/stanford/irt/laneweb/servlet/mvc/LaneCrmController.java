package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;
import edu.stanford.irt.laneweb.spam.SpamService;

@Controller
public class LaneCrmController {

  private static final String ERROR_PAGE = "redirect:/error.html";

  private static final String NEXT_PAGE = "redirect:/contacts/sfp-confirmation.html";

  private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

  private static final String LANELIBACQ_PATH = "/apps/lanelibacqs";

  private static final String[] VALID_EMAILS = { ".*@stanford.edu$", ".*@stanfordhealthcare.org$", ".*@stanfordchildrens.org$" };

  private static final String SUBJECT = "subject";

  private static final String SUBJECT_CONTENT = "SFP:ARRIVAL";

  private static final String SFP_PORTAL = "sfp";

  private SpamService spamService;

  private String sfpEmailAddress;

  private EMailSender sender;

  private DataBinder emailDataBinder;

  public LaneCrmController(@Qualifier("edu.stanford.irt.laneweb.servlet.binding.DataBinder/email") final DataBinder dataBinder, final EMailSender sender,
      @Value("${edu.stanford.irt.laneweb.acquisition.email}") String sfpEmail, SpamService spamService) {
    this.emailDataBinder = dataBinder;
    this.sender = sender;
    this.sfpEmailAddress = sfpEmail;
    this.spamService = spamService;
  }

  @PostMapping(value = LANELIBACQ_PATH, consumes = FORM_MIME_TYPE)
  public String sendEmail(final Model model, final RedirectAttributes atts) throws JsonProcessingException {
    Map<String, Object> data = model.asMap();
    if (!validateStanfordEmail(data)) {
      return ERROR_PAGE;
    }
    this.sender.sendEmail(data);
    return NEXT_PAGE;
  }

  private boolean validateStanfordEmail(final Map<String, Object> data) {
    String email = (String) data.get("requestedBy.email");
    if (null == email || "".equals(email)) {
      return false;
    }
    if (this.spamService.isSpam(SFP_PORTAL, data)) {
      return false;
    }
    for (String emailValidation : VALID_EMAILS) {
      if (email.matches(emailValidation)) {
        return true;
      }
    }
    return false;
  }

  @ModelAttribute
  protected void getParameters(final HttpServletRequest request, final Model model) throws JsonProcessingException {
    Map<String, String[]> map = request.getParameterMap();
    for (Entry<String, String[]> entry : map.entrySet()) {
      String[] value = entry.getValue();
      if (value.length == 1) {
        model.addAttribute(entry.getKey(), value[0]);
      } else {
        throw new LanewebException("multiple values for parameter " + entry.getKey());
      }
    }
    this.emailDataBinder.bind(model.asMap(), request);
    ObjectMapper om = new ObjectMapper();
    model.addAttribute("json", om.writeValueAsString(model));
    model.addAttribute("email", model.getAttribute("requestedBy.email"));
    model.addAttribute("remote-addr", request.getRemoteAddr());
    model.addAttribute(SUBJECT, SUBJECT_CONTENT);
    model.addAttribute("recipient", this.sfpEmailAddress);
  }

}

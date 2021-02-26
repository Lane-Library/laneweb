package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RequestHeaderDataBinder;

// TODO: the individual methods for each address probably can be combined
@Controller
@RequestMapping(value = "/apps/mail")
public class EMailController {

  private static final String ASKUS_ADDRESS = "LaneAskUs@stanford.edu";

  private static final String ASKUS_PATH = "/askus";

  private static final String DOCXPRESS_ADDRESS = "docxpress@lists.stanford.edu";

  private static final String DOCXPRESS_PATH = "/docxpress";

  private static final String EJP_ADDRESS = "ejproblem@lists.stanford.edu";

  private static final String EJP_PATH = "/ejp";

  private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

  private static final String MULTIPART_MIME_TYPE = "multipart/form-data";

  private static final String SUBJECT = "subject";

  private static final String CONFIRMATION_PAGE = "redirect:/contacts/confirmation.html";

  private static final String UPLOAD_ERROR_URL = "redirect:/error_upload_file.html";

  public static final long MAX_UPLOAD_SIZE = 4194304;

  private RequestHeaderDataBinder headerBinder;

  private RemoteProxyIPDataBinder remoteIPBinder;

  private EMailSender sender;

  public EMailController(final RequestHeaderDataBinder headerBinder, final RemoteProxyIPDataBinder remoteIPBinder,
      final EMailSender sender) {
    this.headerBinder = headerBinder;
    this.remoteIPBinder = remoteIPBinder;
    this.sender = sender;
  }

  @PostMapping(value = ASKUS_PATH, consumes = MULTIPART_MIME_TYPE)
  public String formSubmitAskUs(final Model model, @RequestParam("attachment") MultipartFile file,
      final RedirectAttributes atts)
    throws IllegalStateException, IOException {
    File attachment = validateFileMultipartFile(file);
    if (attachment == null && !file.isEmpty()) {
      return UPLOAD_ERROR_URL;
    }
    Map<String, Object> map = model.asMap();
    appendNameToSubject(map);
    sendEmail(ASKUS_ADDRESS, map, attachment);
    return CONFIRMATION_PAGE;
  }

  // Form from the error 404 page
  @PostMapping(value = ASKUS_PATH, consumes = FORM_MIME_TYPE)
  public String submitAskUs(final Model model, final RedirectAttributes atts) throws IllegalStateException, IOException {
    Map<String, Object> map = model.asMap();
    appendNameToSubject(map);
    sendEmail(ASKUS_ADDRESS, map);
    return getRedirectTo(map);
  }

  // Form coming from docxpress.stanford.edu
  @PostMapping(value = DOCXPRESS_PATH, consumes = FORM_MIME_TYPE)
  public String formSubmitDocxpress(final Model model, final RedirectAttributes atts) {
    Map<String, Object> map = model.asMap();
    sendEmail(DOCXPRESS_ADDRESS, map);
    return getRedirectTo(map);
  }

  @PostMapping(value = EJP_PATH, consumes = MULTIPART_MIME_TYPE)
  public String formSubmitEJP(final Model model, @RequestParam("attachment") MultipartFile file, final RedirectAttributes atts)
    throws IllegalStateException, IOException {
    File attachment = validateFileMultipartFile(file);
    if (attachment == null && !file.isEmpty()) {
      return UPLOAD_ERROR_URL;
    }
    Map<String, Object> map = model.asMap();
    StringBuilder subject = new StringBuilder((String) map.get(SUBJECT));
    subject.append(" ").append(map.get("title"));
    map.put(SUBJECT, subject.toString());
    sendEmail(EJP_ADDRESS, map);
    return CONFIRMATION_PAGE;
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

  private void sendEmail(final String recipient, final Map<String, Object> data, File file) {
    try {
      data.put("recipient", recipient);
      this.sender.sendEmail(data, file);
    } catch (Exception e) {
      throw new LanewebException(e);
    } finally {
      if (null != file) {
        file.delete();
      }
    }
  }

  private File validateFileMultipartFile(MultipartFile attachment) throws IllegalStateException, IOException {
    File file = null;
    FileOutputStream fos = null;
    if (attachment != null && !attachment.isEmpty()) {
      try {
        String contentType = attachment.getContentType();
        if (!contentType.startsWith("image/")) {
          return null;
        }
        file = new File(attachment.getOriginalFilename());
        fos = new FileOutputStream(file);
        fos.write(attachment.getBytes());
        if (file.length() > MAX_UPLOAD_SIZE) {
          file.delete();
          return null;
        }
      } catch (IOException e) {
        throw new LanewebException(e);
      } finally {
        if (null != fos) {
          fos.close();
        }
      }
    }
    return file;
  }
}

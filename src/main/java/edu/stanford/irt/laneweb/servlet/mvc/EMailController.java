package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
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
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;
import edu.stanford.irt.laneweb.spam.SpamService;


@Controller
@RequestMapping(value = "/apps/mail/")
public class EMailController {

    public static final long MAX_UPLOAD_SIZE = 4194304;

    private static final String ASKUS_ADDRESS = "LaneAskUs@stanford.edu";

    private static final String ASKUS_PATH = "askus";

    private static final String ASKUS_PORTAL = "laneaskus";

    private static final String CONFIRMATION_PAGE_EJP = "redirect:/contacts/ejp-confirmation.html";

    private static final String DOCXPRESS_ADDRESS = "docxpress@lists.stanford.edu";

    private static final String DOCXPRESS_PATH = "docxpress";

    private static final String DOCXPRESS_PORTAL = "docxpress";

    private static final String EJP_ADDRESS = "ejproblem@lists.stanford.edu";

    private static final String EJP_PATH = "ejp";

    private static final String EJP_PORTAL = "ejp";

    private static final String ERROR_PAGE = "redirect:/error.html";

    private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final String MULTIPART_MIME_TYPE = "multipart/form-data";

    private static final String QUESTION = "question";

    private static final String SUBJECT = "subject";

    private static final String UPLOAD_ERROR_PAGE = "redirect:/error_upload_file.html";

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

    @PostMapping(value = EJP_PATH, consumes = MULTIPART_MIME_TYPE)
    public String formSubmitEJP(final Model model, @RequestParam("attachment") final MultipartFile[] files,
            final RedirectAttributes atts) throws IllegalStateException, IOException {
        Map<String, Object> map = model.asMap();
        if (!validateForm(map, EJP_PORTAL)) {
            return ERROR_PAGE;
        }
        File[] attachments = validateFileMultipartFiles(files);
        if (attachments == null) {
            return UPLOAD_ERROR_PAGE;
        }
        StringBuilder subject = new StringBuilder((String) map.get(SUBJECT));
        subject.append(" ").append(map.get("title"));
        map.put(SUBJECT, subject.toString());
        sendEmail(EJP_ADDRESS, map, attachments);
        return CONFIRMATION_PAGE_EJP;
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

    private void sendEmail(final String recipient, final Map<String, Object> data, final File[] files)
            throws IOException {
        try {
            data.put("recipient", recipient);
            this.sender.sendEmail(data, files);
        } catch (Exception e) {
            throw new LanewebException(e);
        } finally {
            if (null != files && files.length > 0) {
                for (File file : files) {
                    if (file != null) {
                        Files.delete(file.toPath());
                    }
                }
            }
        }
    }

    private File[] validateFileMultipartFiles(final MultipartFile[] attachments)
            throws IllegalStateException, IOException {
        File[] files = new File[attachments.length];
        for (int i = 0; i < attachments.length; i++) {
            if (attachments[i] != null && !attachments[i].isEmpty()) {
                File checkedFile = validateFileMultipartFile(attachments[i]);
                if (checkedFile != null) {
                    files[i] = checkedFile;
                } else {
                    return null;
                }
            }
        }
        return files;
    }

    private File validateFileMultipartFile(final MultipartFile attachment) throws IllegalStateException, IOException {
        File file = null;
        if (attachment != null && !attachment.isEmpty()) {
            String contentType = attachment.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return null;
            }
            file = new File("/tmp/" + attachment.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(attachment.getBytes());
                if (file.length() > MAX_UPLOAD_SIZE) {
                    Files.delete(file.toPath());
                    return null;
                }
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        }
        return file;
    }

    private boolean validateForm(final Map<String, Object> feedback, final String portal) {
        if (this.spamService.isSpam(portal, feedback)) {
            return false;
        }
        return !(feedback.get("email") == null || feedback.get("email").toString().isEmpty()
                || feedback.get("name") == null || feedback.get("name").toString().isEmpty());
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

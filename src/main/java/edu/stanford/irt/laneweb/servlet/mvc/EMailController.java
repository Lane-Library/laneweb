package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RequestHeaderDataBinder;

// TODO: the individual methods for each address probably can be combined
@Controller
@RequestMapping(value = "/apps/mail")
public class EMailController {

    private static final String ASKUS_ADDRESS = "laneaskus@stanford.edu";

    private static final String ASKUS_PATH = "/askus";

    private static final String DOCXPRESS_ADDRESS = "docxpress@lists.stanford.edu";

    private static final String DOCXPRESS_PATH = "/docxpress";

    private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final String JSON_MIME_TYPE = "application/json";

    private static final String SUBJECT = "subject";

    private static final long FOUR_MEGA_BYTES = 4194304;

    private RequestHeaderDataBinder headerBinder;

    private RemoteProxyIPDataBinder remoteIPBinder;

    private EMailSender sender;

    public EMailController(final RequestHeaderDataBinder headerBinder, final RemoteProxyIPDataBinder remoteIPBinder,
            final EMailSender sender) {
        this.headerBinder = headerBinder;
        this.remoteIPBinder = remoteIPBinder;
        this.sender = sender;
    }

    @PostMapping(value = ASKUS_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitAskUs(final Model model, final RedirectAttributes atts) {
        Map<String, Object> map = model.asMap();
        appendNameToSubject(map);
        sendEmail(ASKUS_ADDRESS, map);
        return getRedirectTo(map);
    }

    @PostMapping(value = DOCXPRESS_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitDocxpress(final Model model, final RedirectAttributes atts) {
        Map<String, Object> map = model.asMap();
        sendEmail(DOCXPRESS_ADDRESS, map);
        return getRedirectTo(map);
    }

    @PostMapping(value = ASKUS_PATH, consumes = JSON_MIME_TYPE)
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitAskUs(@RequestBody final Map<String, Object> feedback, final Model model) {
        File file = getAttachmentFile(feedback);
        feedback.putAll(model.asMap());
        appendNameToSubject(feedback);
        sendEmail(ASKUS_ADDRESS, feedback, file);
    }

    @PostMapping(value = DOCXPRESS_PATH, consumes = JSON_MIME_TYPE)
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitDocxpress(@RequestBody final Map<String, Object> feedback, final Model model) {
        feedback.putAll(model.asMap());
        sendEmail(DOCXPRESS_ADDRESS, feedback);
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

    private File getAttachmentFile(Map<String, Object> feedback) {
        File file = null;
        String content = (String) feedback.remove("attachment");
        if (null != content && !"".equals(content)) {
            String contentType = content.substring(5, content.indexOf(","));
            if (!contentType.contains("image/")) {
                return null;
            }
            file = generateImageFile(content, feedback);
            if (file.length() > FOUR_MEGA_BYTES) {
                return null;
            }
        }
        return file;
    }

    private File generateImageFile(String content, Map<String, Object> feedback) {
        String fileName = (String) feedback.remove("attachmentFileName");
        File file = null;
        try {
            file = new File(fileName);
            String image = content.substring(content.indexOf(",") + 1);
            byte[] imageBytes = DatatypeConverter.parseBase64Binary(image);
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(imageBytes);
            out.close();
        } catch (IOException e) {
            throw new LanewebException(feedback.toString(), e);
        }
        return file;
    }
}

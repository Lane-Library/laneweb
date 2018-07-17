package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.crm.CRMService;

@Controller
public class LaneCrmController {

    private static final String ERROR_URL = "/error.html";

    private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final String JSON_MIME_TYPE = "application/json";

    private static final String LANELIBACQ_PATH = "/apps/lanelibacqs";

    private CRMService crmService;

    @Autowired
    public LaneCrmController(final CRMService crmService) {
        this.crmService = crmService;
    }

    @PostMapping(value = LANELIBACQ_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitLanelibacqs(final Model model, final RedirectAttributes atts) throws IOException {
        Map<String, Object> map = model.asMap();
        int responseCode = this.crmService.submitRequest(map);
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.resolve(responseCode));
        return getRedirectTo(map, response);
    }

    @PostMapping(value = LANELIBACQ_PATH, consumes = JSON_MIME_TYPE)
    public ResponseEntity<String> jsonSubmitLanelibacqs(@RequestBody final Map<String, Object> feedback)
            throws IOException {
        int responseCode = this.crmService.submitRequest(feedback);
        return new ResponseEntity<>(HttpStatus.resolve(responseCode));
    }

    private String getRedirectTo(final Map<String, Object> map, final ResponseEntity<String> response) {
        String redirectTo = (String) map.get("redirect");
        if (response.getStatusCode() != HttpStatus.OK) {
            redirectTo = ERROR_URL;
        } else if (redirectTo == null) {
            redirectTo = (String) map.get(edu.stanford.irt.laneweb.model.Model.REFERRER);
        }
        if (redirectTo == null) {
            redirectTo = "/index.html";
        }
        return "redirect:" + redirectTo;
    }
}

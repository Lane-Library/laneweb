package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    private static final String[] VALID_EMAILS = { ".*@stanford.edu$", ".*@stanfordhealthcare.org$", ".*@stanfordchildrens.org$" };

    private CRMService crmService;

    @Autowired
    public LaneCrmController(final CRMService crmService) {
        this.crmService = crmService;
    }
    
   
    @PostMapping(value = LANELIBACQ_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitLanelibacqs( final RedirectAttributes atts, HttpServletRequest request)  {
            return  "redirect:"+ ERROR_URL;
    }
    
    @PostMapping(value = LANELIBACQ_PATH, consumes = JSON_MIME_TYPE)
    public ResponseEntity<String> jsonSubmitLanelibacqs(@RequestBody final Map<String, Object> feedback,
            HttpServletRequest request) throws IOException {
          if (!validateStanfordEmail(feedback)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String ip = request.getRemoteAddr();
        int responseCode = this.crmService.submitRequest(feedback, ip);
        return new ResponseEntity<>(HttpStatus.resolve(responseCode));
    }

    
    private boolean validateStanfordEmail(Map<String, Object> httpParameters) {
        String email = (String) httpParameters.get("requestedBy.email");
        if(null == email || "".equals(email)) {
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

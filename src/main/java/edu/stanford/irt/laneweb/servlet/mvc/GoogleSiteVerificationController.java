package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GoogleSiteVerificationController {

    private static final String VERIFICATION_STRING = "google-site-verification: google708f1eef3c6d1e52.html";

    @GetMapping(value = "/*/google708f1eef3c6d1e52.html")
    @ResponseBody
    public String getVerificationString() {
        return VERIFICATION_STRING;
    }
}

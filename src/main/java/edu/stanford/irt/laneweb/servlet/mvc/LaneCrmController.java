package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LaneCrmController {

	private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final String JSON_MIME_TYPE = "application/json";
	
    private static final String ACQUISITION_URL = "https://lane-local-02.stanford.edu/crm/sfp/api/new";
    
    private static final String LANEASKUS_URL = "https://lane-local-02.stanford.edu/crm/laneaskus/api/new";

    private static final String LANELIBACQ_PATH = "/apps/lanelibacqs";

    private static final String ASKUS_PATH = "/apps/laneaskus";

    
    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @RequestMapping(value = LANELIBACQ_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitLanelibacqs(final Model model, final RedirectAttributes atts) throws IOException {
        Map<String, Object> map = model.asMap();
        submitRequestToCrmServer(map, ACQUISITION_URL);
        return getRedirectTo(map);
    }

    @RequestMapping(value = LANELIBACQ_PATH, consumes = JSON_MIME_TYPE)
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitLanelibacqs(@RequestBody final Map<String, Object> feedback) throws IOException {
        submitRequestToCrmServer(feedback, ACQUISITION_URL);
    }


    @RequestMapping(value = ASKUS_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitLaneaskus(final Model model, final RedirectAttributes atts) throws IOException {
        Map<String, Object> map = model.asMap();
        submitRequestToCrmServer(map, LANEASKUS_URL );
        return getRedirectTo(map);
    }

    @RequestMapping(value = ASKUS_PATH, consumes = JSON_MIME_TYPE)
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitLaneaskus(@RequestBody final Map<String, Object> feedback) throws IOException {
        submitRequestToCrmServer(feedback, LANEASKUS_URL);
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

    private void submitRequestToCrmServer(final Map<String, Object> feedback, String crmUrl) throws IOException {
        StringBuilder queryString = new StringBuilder();
        for (Entry<String, Object> entry : feedback.entrySet()) {
            queryString.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue().toString(), UTF_8))
                    .append('&');
        }
        queryString.append("id=");
        URL url = new URL(crmUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(queryString.toString());
        wr.close();
        con.getResponseCode();
    }
}

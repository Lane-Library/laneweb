package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LaneCrmController {

    /**
     * A factory for URLConnections that can be mocked in tests.
     */
    static class URLConnectionFactory {

        public URLConnection getConnection(final String url) throws IOException {
            return new URL(url).openConnection();
        }
    }

    private static final String ERROR_URL = "/error.html";

    private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final String JSON_MIME_TYPE = "application/json";

    private static final String LANELIBACQ_PATH = "/apps/lanelibacqs";

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private String acquisitionURL;

    private URLConnectionFactory connectionFactory;

    @Autowired
    public LaneCrmController(@Value("${edu.stanford.irt.laneweb.acquisition-api.url}") final String acquisitionURL) {
        this(acquisitionURL, new URLConnectionFactory());
    }

    LaneCrmController(final String acquisitionURL, final URLConnectionFactory connectionFactory) {
        this.acquisitionURL = acquisitionURL;
        this.connectionFactory = connectionFactory;
    }

    @PostMapping(value = LANELIBACQ_PATH, consumes = FORM_MIME_TYPE)
    public String formSubmitLanelibacqs(final Model model, final RedirectAttributes atts) throws IOException {
        Map<String, Object> map = model.asMap();
        ResponseEntity<String> response = submitRequestToCrmServer(map, this.acquisitionURL);
        return getRedirectTo(map, response);
    }

    @PostMapping(value = LANELIBACQ_PATH, consumes = JSON_MIME_TYPE)
    public ResponseEntity<String> jsonSubmitLanelibacqs(@RequestBody final Map<String, Object> feedback)
            throws IOException {
        return submitRequestToCrmServer(feedback, this.acquisitionURL);
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

    private ResponseEntity<String> submitRequestToCrmServer(final Map<String, Object> feedback, final String crmUrl)
            throws IOException {
        StringBuilder queryString = new StringBuilder();
        for (Entry<String, Object> entry : feedback.entrySet()) {
            queryString.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue().toString(), UTF_8))
                    .append('&');
        }
        queryString.append("id=");
        HttpsURLConnection con = (HttpsURLConnection) this.connectionFactory.getConnection(crmUrl);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(queryString.toString());
        wr.close();
        int responseCode = con.getResponseCode();
        if (HttpsURLConnection.HTTP_OK != responseCode) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

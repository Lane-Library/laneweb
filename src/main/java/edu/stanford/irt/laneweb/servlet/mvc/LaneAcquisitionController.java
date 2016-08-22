package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class LaneAcquisitionController {

    private static final String ACQUISITION_URL = "https://lane-local-02.stanford.edu/sfp/api/new";

    @RequestMapping(value = "/apps/lanelibacqs", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitLanelibacqs(@RequestBody final Map<String, Object> feedback) throws IOException {
        StringBuilder queryString = new StringBuilder();
        for (Entry<String, Object> entry: feedback.entrySet()) {
            queryString.append(entry.getKey())
                .append('=')
                .append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"))
                .append('&');
        }
        queryString.append("id=");
        URL url = new URL(ACQUISITION_URL);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(queryString.toString());
        wr.close();
        con.getResponseCode();
    }
}

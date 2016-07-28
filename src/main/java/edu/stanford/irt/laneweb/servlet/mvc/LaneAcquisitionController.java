package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class LaneAcquisitionController {

	
	private String ACQUISITION_URL = "https://lane-local-02.stanford.edu/sfp/api/new"; 
	
	  
    @RequestMapping(value = "/apps/lanelibacqs", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void jsonSubmitLanelibacqs(@RequestBody final Map<String, Object> feedback) throws IOException {
        Set<String>keys =  feedback.keySet();
        StringBuilder queryString = new StringBuilder();
        for (String key : keys) {
        	String value = URLEncoder.encode((String) feedback.get(key),"UTF-8");
        	queryString.append( key +"="+ value +"&");
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

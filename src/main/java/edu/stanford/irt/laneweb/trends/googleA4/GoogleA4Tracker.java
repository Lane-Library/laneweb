package edu.stanford.irt.laneweb.trends.googleA4;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import edu.stanford.irt.laneweb.LanewebException;



public class GoogleA4Tracker {

	private static final Logger logger = LoggerFactory.getLogger(GoogleA4Tracker.class);
    
    private String clientId;
    
    
    private URL url;
    
    private static final  String EVENT_NAME = "public_computer_trends";
    
    
    
    public GoogleA4Tracker(String endPoint, String measurementId, String apiSecret, String clientId){
        this.clientId = clientId;
        try {
			this.url = new URL( endPoint.concat("?measurement_id=").concat(measurementId).concat("&api_secret=").concat(apiSecret));
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(),e);
		}
    }

    
    public void trackEvent(String path,  String category, String action, String label, int value) {
        String googleRequest = getPayLoad(path, category, action, label, value);
        try {
            this.sendMeasurementRequest(googleRequest);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        logger.info("track event {}/{}/{}/{}/{}", path, category, action, label, value);
    }

    
    private  String getPayLoad( String path,  String category,  String action, String label, int value) {
        Payload payLoad = new Payload(this.clientId);
        Event event = new Event(EVENT_NAME);
        event.addParamters("label", label);
        event.addParamters("category", category);
        event.addParamters("action", action);
        event.addParamters("value", value);
        if(null != path && !path.isEmpty()) {
            event.addParamters("path", path);
        }
        payLoad.addEvents(event);
        Gson gson = new Gson();
        return  gson.toJson(payLoad);
    }
    
    
    private void sendMeasurementRequest(String payLoad) throws IOException  {
        HttpURLConnection con = getUrlConnection();
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(payLoad);
        wr.flush();
        wr.close();
        con.disconnect();
        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new LanewebException("Had problem with http connection with google Analytics");
        }
    }

    private HttpURLConnection getUrlConnection() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");
        return con;
    }
}

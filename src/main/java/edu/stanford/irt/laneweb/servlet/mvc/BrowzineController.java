package edu.stanford.irt.laneweb.servlet.mvc;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.BrowzineException;
import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;

@Controller
public class BrowzineController {

    private RESTService restService;

    private String browzineToken;

    private String browzineUrl;

    private static final String BROWZINE_PATH = "/apps/browzine/";

    @GetMapping(value = BROWZINE_PATH + "**")
    @ResponseBody
    public String getDoi(HttpServletRequest req) {
        String requestUrl = req.getRequestURL().toString();
        String brozineValue = requestUrl.substring(requestUrl.indexOf(BROWZINE_PATH) + BROWZINE_PATH.length());
        String url = MessageFormat.format(this.browzineUrl, brozineValue, this.browzineToken);
        try {
            return restService.getObject(new URI(url), String.class);
        } catch (RESTException | URISyntaxException e) {
            throw new BrowzineException(url + "\t" + e.getMessage());
        }
    }

    @Autowired
    public void setRestService(RESTService restService) {
        this.restService = restService;
    }

    @Value("${edu.stanford.irt.laneweb.browzine-token}")
    public void setBrowzineToken(String browzineToken) {
        this.browzineToken = browzineToken;
    }

    @Value("${edu.stanford.irt.laneweb.browzine-url}")
    public void setBrowzineUrl( String browzineUrl) {
        this.browzineUrl = browzineUrl;
    }
}
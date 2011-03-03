package edu.stanford.irt.laneweb.servlet.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.history.TrackingData;

@Controller
public class HistoryTrackerController {

    private final Logger log = LoggerFactory.getLogger(HistoryTrackerController.class);
    
    @RequestMapping(value = "**/history/track")
    @ResponseBody
    public void trackHistory(@RequestBody TrackingData trackingData) {
        log.info("some history just got tracked! " + trackingData);
    }
}

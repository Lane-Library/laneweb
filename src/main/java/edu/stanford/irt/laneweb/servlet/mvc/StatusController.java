package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.status.ApplicationStatus;
import edu.stanford.irt.laneweb.status.StatusService;

@Controller
public class StatusController {

    private StatusService statusService;

    @Autowired
    public StatusController(final StatusService statusService) {
        this.statusService = statusService;
    }

    @RequestMapping(value = "/status.json", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ApplicationStatus getStatusJson() {
        return this.statusService.getApplicationStatus();
    }

    @RequestMapping(value = "/status.txt", produces = "text/plain; charset=utf-8")
    @ResponseBody
    public String getStatusTxt() {
        return this.statusService.getApplicationStatus().toString();
    }
}

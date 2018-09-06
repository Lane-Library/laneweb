package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.status.LanewebStatusService;
import edu.stanford.irt.status.ApplicationStatus;

@Controller
public class StatusController {

    private LanewebStatusService statusService;

    public StatusController(final LanewebStatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping(value = "/status.json", produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<ApplicationStatus> getStatusJson() {
        return this.statusService.getStatus();
    }

    @GetMapping(value = "/status.txt", produces = "text/plain; charset=utf-8")
    @ResponseBody
    public String getStatusTxt() {
        return this.statusService.getStatus().stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}

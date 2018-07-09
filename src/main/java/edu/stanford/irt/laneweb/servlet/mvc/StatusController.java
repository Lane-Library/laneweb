package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.bookcovers.BookCoverService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

@Controller
public class StatusController {

    private BookCoverService bookcoverService;

    private StatusService statusService;

    public StatusController(final StatusService statusService,
            @Qualifier("edu.stanford.irt.laneweb.bookcovers.BookCoverService/HTTP")
            final BookCoverService bookcoverService) {
        this.statusService = statusService;
        this.bookcoverService = bookcoverService;
    }

    @RequestMapping(value = "/status.json", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ApplicationStatus getStatusJson() {
        ApplicationStatus bookcoverStatus = this.bookcoverService.getStatus();
        return this.statusService.getApplicationStatus(Collections.singletonList(bookcoverStatus));
    }

    @RequestMapping(value = "/status.txt", method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
    @ResponseBody
    public String getStatusTxt() {
        ApplicationStatus bookcoverStatus = this.bookcoverService.getStatus();
        return this.statusService.getApplicationStatus(Collections.singletonList(bookcoverStatus)).toString();
    }
}

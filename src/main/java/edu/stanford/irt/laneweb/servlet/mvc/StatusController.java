package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.bookcovers.BookCoverService;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

@Controller
public class StatusController {

    private BookCoverService bookcoverService;

    private BookmarkService bookmarkService;

    private StatusService statusService;

    public StatusController(final StatusService statusService, final BookCoverService bookcoverService,
            final BookmarkService bookmarkService) {
        this.statusService = statusService;
        this.bookcoverService = bookcoverService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping(value = "/status.json", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ApplicationStatus getStatusJson() {
        return doGetStatus();
    }

    @GetMapping(value = "/status.txt", produces = "text/plain; charset=utf-8")
    @ResponseBody
    public String getStatusTxt() {
        return doGetStatus().toString();
    }

    private ApplicationStatus doGetStatus() {
        List<ApplicationStatus> list = new ArrayList<>();
        list.add(this.bookcoverService.getStatus());
        list.add(this.bookmarkService.getStatus());
        return this.statusService.getApplicationStatus(list);
    }
}

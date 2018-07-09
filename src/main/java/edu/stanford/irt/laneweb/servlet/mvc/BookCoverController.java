package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.bookcovers.BookCoverService;

@Controller
public class BookCoverController {

    private BookCoverService service;

    public BookCoverController(
            @Qualifier("edu.stanford.irt.laneweb.bookcovers.BookCoverService/HTTP")
            final BookCoverService service) {
        this.service = service;
    }

    @RequestMapping(value = "/apps/bookcovers", method = RequestMethod.GET)
    @ResponseBody
    public Map<Integer, String> getBookCovers(@RequestParam final List<Integer> bibid) {
        return this.service.getBookCoverURLs(bibid);
    }
}

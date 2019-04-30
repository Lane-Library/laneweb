package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.bookcovers.BookCoverService;

@Controller
public class BookCoverController {

    private BookCoverService service;

    public BookCoverController(final BookCoverService service) {
        this.service = service;
    }

    @GetMapping(value = "/apps/bookcovers")
    @ResponseBody
    public Map<String, String> getBookCovers(@RequestParam final List<String> bcid) {
        return this.service.getBookCoverURLs(bcid);
    }
}

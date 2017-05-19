package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.bookcovers.BookCoverService;

@Controller
public class BookCoverController {

    private BookCoverService service;

    @Autowired
    public BookCoverController(final BookCoverService service) {
        this.service = service;
    }

    @RequestMapping(value = "/apps/bookcovers")
    @ResponseBody
    public Map<Integer, String> getBookCovers(@RequestParam final Integer[] bibid) {
        return this.service.getBookCoverURLs(new ArrayList<>(Arrays.asList(bibid)));
    }
}

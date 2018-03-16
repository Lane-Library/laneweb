package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

@Controller
public class UrlTester {

    private MetaSearchService metaSearchService;

    public UrlTester(final MetaSearchService metaSearchService) {
        this.metaSearchService = metaSearchService;
    }

    @RequestMapping(value = "/apps/url-tester")
    public void testUrl(@RequestParam final String url, OutputStream output) throws IOException {
        output.write(this.metaSearchService.testURL(url));
    }
}

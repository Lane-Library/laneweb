package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

@Controller
public class UrlTester {

    private MetaSearchService metaSearchService;

    public UrlTester(final MetaSearchService metaSearchService) {
        this.metaSearchService = metaSearchService;
    }

    @GetMapping(value = "/apps/url-tester")
    public void testUrl(@RequestParam final String url, final HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "text/plain");
        response.getOutputStream().write(this.metaSearchService.testURL(url));
    }
}

package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CMERedirectController {

    private static final String ERROR_URL = "/cmeRedirectError.html";

    private static final String PROXY_LINK = "http://laneproxy.stanford.edu/login?url=";

    // TODO: once more vendors, move UTD strings to collection of host objects
    private static final String UTD_CME_STRING = "http://www.uptodate.com/online/content/search.do?unid=EMRID&srcsys=epic90710&eiv=2.1.0";

    @RequestMapping(value = "redirect/cme")
    public void cmeRedirect(@RequestParam final String emrid, @RequestParam final String host, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        if ("uptodate".equalsIgnoreCase(host)) {
            response.sendRedirect(PROXY_LINK + UTD_CME_STRING.replaceFirst("EMRID", emrid));
        } else {
            String queryString = request.getQueryString();
            if (queryString == null) {
                throw new IllegalArgumentException("null query-string");
            }
            response.sendRedirect(null == queryString ? ERROR_URL : ERROR_URL + '?' + queryString);
        }
    }
}

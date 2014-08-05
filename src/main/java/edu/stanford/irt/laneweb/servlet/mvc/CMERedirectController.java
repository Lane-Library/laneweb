package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;

@Controller
public class CMERedirectController {

    private static final String ERROR_URL = "/cmeRedirectError.html";

    private static final String PROXY_LINK = "http://laneproxy.stanford.edu/login?url=";

    private static final String SHC_EMRID_ARGS = "unid=?&srcsys=epic90710&eiv=2.1.0";

    private static final String SU_SUNETID_ARGS = "unid=?&srcsys=EZPX90710&eiv=2.1.0";

    private static final String UTD_CME_URL = "http://www.uptodate.com/contents/search?";

    @Autowired
    private CompositeDataBinder dataBinder;

    public CMERedirectController() {
        // empty default constructor
    }

    public CMERedirectController(final CompositeDataBinder dataBinder) {
        this.dataBinder = dataBinder;
    }

    /**
     * puts the EMRID, AUTH and PROXY_LINKS into the model.
     * 
     * @param request
     *            the request
     * @param model
     *            the model
     */
    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.dataBinder.bind(model.asMap(), request);
        if (!model.containsAttribute(Model.AUTH)) {
            model.addAttribute(Model.AUTH, null);
        }
        if (!model.containsAttribute(Model.BASE_PATH)) {
            model.addAttribute(Model.BASE_PATH, null);
        }
        if (!model.containsAttribute(Model.EMRID)) {
            model.addAttribute(Model.EMRID, null);
        }
        if (!model.containsAttribute(Model.PROXY_LINKS)) {
            model.addAttribute(Model.PROXY_LINKS, Boolean.FALSE);
        }
    }

    @RequestMapping(value = "redirect/cme")
    public void cmeRedirect(@ModelAttribute(Model.AUTH) final String sunetHash,
            @ModelAttribute(Model.BASE_PATH) final String basePath, @ModelAttribute(Model.EMRID) final String emrid,
            @ModelAttribute(Model.PROXY_LINKS) final boolean proxyLinks, @RequestParam final String url,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("null url");
        }
        if (emrid == null && sunetHash == null) {
            response.sendRedirect(basePath + "/secure/redirect/cme?url=" + url);
        } else {
            doRedirect(sunetHash, emrid, proxyLinks, url, request, response);
        }
    }

    @RequestMapping(value = "secure/redirect/cme")
    public void cmeSecureRedirect(@ModelAttribute(Model.AUTH) final String sunetHash,
            @ModelAttribute(Model.EMRID) final String emrid,
            @ModelAttribute(Model.PROXY_LINKS) final boolean proxyLinks, @RequestParam final String url,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("null url");
        }
        doRedirect(sunetHash, emrid, proxyLinks, url, request, response);
    }

    private String createCMELink(final String link, final String emrid, final String sunetHash, final boolean proxyLinks) {
        StringBuilder sb = new StringBuilder();
        String id = null;
        String args = null;
        if (proxyLinks) {
            sb.append(PROXY_LINK);
        }
        if (emrid == null && sunetHash == null) {
            sb.append(link);
            return sb.toString();
        }
        if (emrid != null) {
            id = emrid;
            args = SHC_EMRID_ARGS;
        } else if (sunetHash != null) {
            id = sunetHash;
            args = SU_SUNETID_ARGS;
        }
        if (link.contains("?")) {
            sb.append(link).append("&").append(args.replaceFirst("\\?", id));
        } else if (link.endsWith("/") || link.endsWith("online") || link.endsWith("search")) {
            sb.append(UTD_CME_URL).append(args.replaceFirst("\\?", id));
        } else {
            sb.append(link);
        }
        return sb.toString();
    }

    private void doRedirect(final String sunetHash, final String emrid, final boolean proxyLinks, final String url,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (url.contains("www.uptodate.com")) {
            response.sendRedirect(createCMELink(url, emrid, sunetHash, proxyLinks));
        } else {
            String queryString = request.getQueryString();
            response.sendRedirect(null == queryString ? ERROR_URL : ERROR_URL + '?' + queryString);
        }
    }
}

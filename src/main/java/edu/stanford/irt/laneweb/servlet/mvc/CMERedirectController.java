package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

@Controller
public class CMERedirectController {

    private static final String ERROR_URL = "/cmeRedirectError.html";

    private static final String ORGID_EZP = "EZPX90710";

    private static final String ORGID_LPCH = "EPICLPCH90710";

    private static final String ORGID_SHC = "epic90710";

    private static final String PROXY_LINK = "https://login.laneproxy.stanford.edu/login?url=";

    private static final Pattern QUESTION_MARK_PATTERN = Pattern.compile("\\?");

    private static final String UTD_CME_ARGS = "unid=?&srcsys=?&eiv=2.1.0";

    private static final String UTD_CME_URL = "https://www.uptodate.com/contents/search?";

    private DataBinder dataBinder;

    public CMERedirectController(
            @Qualifier("edu.stanford.irt.laneweb.servlet.binding.DataBinder/cme") final DataBinder dataBinder) {
        this.dataBinder = dataBinder;
    }

    @GetMapping(value = "redirect/cme")
    public void cmeRedirect(@ModelAttribute(Model.AUTH) final String userHash,
            @ModelAttribute(Model.BASE_PATH) final String basePath, @ModelAttribute(Model.EMRID) final String emrid,
            @ModelAttribute(Model.PROXY_LINKS) final boolean proxyLinks, @RequestParam final String url,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (emrid == null && userHash == null) {
            response.sendRedirect(basePath + "/secure/redirect/cme?url=" + url);
        } else {
            doRedirect(userHash, emrid, proxyLinks, url, request, response);
        }
    }

    @GetMapping(value = "secure/redirect/cme")
    public void cmeSecureRedirect(@ModelAttribute(Model.AUTH) final String userHash,
            @ModelAttribute(Model.EMRID) final String emrid,
            @ModelAttribute(Model.PROXY_LINKS) final boolean proxyLinks, @RequestParam final String url,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        doRedirect(userHash, emrid, proxyLinks, url, request, response);
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

    private String buildArgsFromUserids(final String emrid, final String userHash) {
        String args;
        String userid;
        String orgid;
        if (emrid != null) {
            userid = emrid;
            if (emrid.toLowerCase(Locale.US).startsWith("lpch-")) {
                orgid = ORGID_LPCH;
            } else {
                orgid = ORGID_SHC;
            }
        } else {
            userid = removeDomainFromUserHash(userHash);
            orgid = ORGID_EZP;
        }
        args = QUESTION_MARK_PATTERN.matcher(UTD_CME_ARGS).replaceFirst(userid);
        return QUESTION_MARK_PATTERN.matcher(args).replaceFirst(orgid);
    }

    private String createCMELink(final String url, final String emrid, final String userHash,
            final boolean proxyLinks) {
        StringBuilder sb = new StringBuilder();
        if (proxyLinks) {
            sb.append(PROXY_LINK);
        }
        String args = buildArgsFromUserids(emrid, userHash);
        if (url.contains("?")) {
            sb.append(url).append('&').append(args);
        } else if (url.endsWith("/") || url.endsWith("online") || url.endsWith("search")) {
            sb.append(UTD_CME_URL).append(args);
        } else {
            sb.append(url);
        }
        return sb.toString();
    }

    private void doRedirect(final String userHash, final String emrid, final boolean proxyLinks, final String url,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (url.contains("www.uptodate.com")) {
            response.sendRedirect(createCMELink(url, emrid, userHash, proxyLinks));
        } else {
            String queryString = request.getQueryString();
            response.sendRedirect(null == queryString ? ERROR_URL : ERROR_URL + '?' + queryString);
        }
    }

    private String removeDomainFromUserHash(final String userHash) {
        String[] tokens = userHash.split("@");
        if (tokens.length > 1) {
            return tokens[0];
        }
        return userHash;
    }
}

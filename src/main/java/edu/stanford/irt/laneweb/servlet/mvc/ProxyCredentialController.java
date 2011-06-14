package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

@Controller
@SessionAttributes({ Model.TICKET, Model.SUNETID })
public class ProxyCredentialController {

    private static final String PROXY_URL_BASE = "http://laneproxy.stanford.edu/login?user=";

    private static final String TICKET_PARAM = "&ticket=";

    @Resource(name = "laneweb.context.ezproxy-key")
    private String ezproxyKey;

    private SunetIdSource sunetIdSource = new SunetIdSource();

    @ModelAttribute(Model.SUNETID)
    public String getSunetid(final HttpServletRequest request) {
        return this.sunetIdSource.getSunetid(request);
    }

    @ModelAttribute(Model.TICKET)
    public Ticket getTicket(@ModelAttribute(Model.SUNETID) final String sunetid) {
        return new Ticket(sunetid, this.ezproxyKey);
    }

    @RequestMapping(value = "**/secure/apps/proxy/credential")
    public void proxyRedirect(final HttpServletResponse response, final HttpServletRequest request,
            @ModelAttribute(Model.SUNETID) final String sunetid, @ModelAttribute(Model.TICKET) final Ticket ticket)
            throws IOException {
        String queryString = request.getQueryString();
        if (queryString == null) {
            throw new IllegalArgumentException("null query-string");
        }
        response.sendRedirect(PROXY_URL_BASE + sunetid + TICKET_PARAM + ticket + "&" + queryString);
    }
}

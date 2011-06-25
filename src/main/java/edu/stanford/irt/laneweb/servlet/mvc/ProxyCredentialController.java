package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

@Controller
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
    public Ticket getTicket(@ModelAttribute(Model.SUNETID) final String sunetid, final HttpSession session) {
        Ticket ticket = null;
        if (sunetid != null) {
            ticket = (Ticket) session.getAttribute(Model.TICKET);
            if (ticket == null || !ticket.isValid()) {
                ticket = new Ticket(sunetid, this.ezproxyKey);
                session.setAttribute(Model.TICKET, ticket);
            }
        }
        return ticket;
    }

    @RequestMapping(value = "**/apps/proxy/credential")
    public void proxyRedirect(final HttpServletResponse response, final HttpServletRequest request,
            @ModelAttribute(Model.SUNETID) final String sunetid, @ModelAttribute(Model.TICKET) final Ticket ticket)
            throws IOException {
        String queryString = request.getQueryString();
        if (sunetid == null || ticket == null) {
            response.sendRedirect("/secure/apps/proxy/credential?" + queryString);
        } else {
            secureProxyRedirect(response, request, sunetid, ticket);
        }
    }

    @RequestMapping(value = "**/secure/apps/proxy/credential")
    public void secureProxyRedirect(final HttpServletResponse response, final HttpServletRequest request,
            @ModelAttribute(Model.SUNETID) final String sunetid, @ModelAttribute(Model.TICKET) final Ticket ticket)
            throws IOException {
        String queryString = request.getQueryString();
        if (queryString == null) {
            throw new IllegalArgumentException("null query-string");
        }
        response.sendRedirect(PROXY_URL_BASE + sunetid + TICKET_PARAM + ticket + "&" + queryString);
    }
}

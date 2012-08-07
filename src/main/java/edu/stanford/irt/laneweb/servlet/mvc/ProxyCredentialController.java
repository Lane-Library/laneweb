package edu.stanford.irt.laneweb.servlet.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.binding.SunetIdAndTicketDataBinder;

@Controller
public class ProxyCredentialController {

    private static final String PROXY_URL_BASE = "http://laneproxy.stanford.edu/login?user=";

    private static final String TICKET_PARAM = "&ticket=";

    private SunetIdAndTicketDataBinder binder;

    @Autowired
    public ProxyCredentialController(final SunetIdAndTicketDataBinder binder) {
        this.binder = binder;
    }

    @RequestMapping(value = "**/apps/proxy/credential")
    public String proxyRedirect(
            final HttpServletRequest request,
            final RedirectAttributes attrs,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @ModelAttribute(Model.TICKET) final Ticket ticket) {
        StringBuilder sb = new StringBuilder("redirect:");
        String queryString = request.getQueryString();
        if (queryString == null) {
            throw new IllegalArgumentException("null queryString");
        }
        if (sunetid == null || ticket == null) {
            sb.append("/secure/apps/proxy/credential?").append(queryString);
        } else {
            sb.append(PROXY_URL_BASE).append(sunetid).append(TICKET_PARAM).append(ticket).append('&').append(queryString);
        }
        return sb.toString();
    }

    @RequestMapping(value = "**/secure/apps/proxy/credential")
    public String secureProxyRedirect(
            final HttpServletRequest request,
            final RedirectAttributes attrs,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @ModelAttribute(Model.TICKET) final Ticket ticket) {
        String queryString = request.getQueryString();
        if (queryString == null) {
            throw new IllegalArgumentException("null queryString");
        }
        return new StringBuilder("redirect:").append(PROXY_URL_BASE).append(sunetid).append(TICKET_PARAM).append(ticket)
                .append('&').append(queryString).toString();
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.binder.bind(model.asMap(), request);
        //case 74082 /apps/proxy/credential causes an error, need to put null values into the model
        if (!model.containsAttribute(Model.SUNETID)) {
            model.addAttribute(Model.SUNETID, null);
            model.addAttribute(Model.TICKET, null);
        }
    }
}

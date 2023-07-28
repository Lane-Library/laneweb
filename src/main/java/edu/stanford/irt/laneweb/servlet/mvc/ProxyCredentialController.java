package edu.stanford.irt.laneweb.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.binding.TicketDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

@Controller
public class ProxyCredentialController {

    private static final String PROXY_URL_BASE = "https://login.laneproxy.stanford.edu/login?user=";

    private static final String TICKET_PARAM = "&ticket=";

    private TicketDataBinder ticketBinder;

    private UserDataBinder userBinder;

    public ProxyCredentialController(final TicketDataBinder ticketBinder, final UserDataBinder userBinder) {
        this.ticketBinder = ticketBinder;
        this.userBinder = userBinder;
    }

    @GetMapping(value = "/apps/proxy/credential")
    public View proxyRedirect(final HttpServletRequest request,
            final RedirectAttributes attrs,
            @ModelAttribute(Model.USER_ID) final String userid,
            @ModelAttribute(Model.TICKET) final Ticket ticket) {
        String queryString = request.getQueryString();
        if (queryString == null) {
            throw new IllegalArgumentException("null queryString");
        }
        StringBuilder sb = new StringBuilder();
        if (userid == null || ticket == null) {
            sb.append("/secure/apps/proxy/credential?").append(queryString);
        } else {
            sb.append(PROXY_URL_BASE).append(userid).append(TICKET_PARAM).append(ticket).append('&')
                    .append(queryString);
        }
        RedirectView view = new RedirectView(sb.toString(), true, true);
        view.setExpandUriTemplateVariables(false);
        return view;
    }

    @GetMapping(value = "/secure/apps/proxy/credential")
    public View secureProxyRedirect(final HttpServletRequest request,
            final RedirectAttributes attrs,
            @ModelAttribute(Model.USER_ID) final String userid,
            @ModelAttribute(Model.TICKET) final Ticket ticket) {
        String queryString = request.getQueryString();
        if (queryString == null) {
            throw new IllegalArgumentException("null queryString");
        }
        String url = new StringBuilder(PROXY_URL_BASE).append(userid).append(TICKET_PARAM).append(ticket).append('&')
                .append(queryString).toString();
        RedirectView view = new RedirectView(url, true, true);
        view.setExpandUriTemplateVariables(false);
        return view;
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.userBinder.bind(model.asMap(), request);
        this.ticketBinder.bind(model.asMap(), request);
        // case 74082 /apps/proxy/credential causes an error, need to put null
        // values into the model
        if (!model.containsAttribute(Model.USER_ID)) {
            model.addAttribute(Model.USER_ID, null);
            model.addAttribute(Model.TICKET, null);
        }
    }
}

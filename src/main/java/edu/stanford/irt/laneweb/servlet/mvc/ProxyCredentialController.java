package edu.stanford.irt.laneweb.servlet.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.binding.UserIdAndTicketDataBinder;

@Controller
public class ProxyCredentialController {

    private static final String PROXY_URL_BASE = "http://laneproxy.stanford.edu/login?user=";

    private static final String TICKET_PARAM = "&ticket=";

    private UserIdAndTicketDataBinder binder;

    @Autowired
    public ProxyCredentialController(final UserIdAndTicketDataBinder binder) {
        this.binder = binder;
    }

    @RequestMapping(value = "/apps/proxy/credential")
    public View proxyRedirect(
            final HttpServletRequest request,
            final RedirectAttributes attrs,
            @ModelAttribute(Model.USER_ID) final String userid,
            @ModelAttribute(Model.TICKET) final Ticket ticket) {
        StringBuilder sb = new StringBuilder();
        String queryString = request.getQueryString();
        if (queryString == null) {
            throw new IllegalArgumentException("null queryString");
        }
        if (userid == null || ticket == null) {
            sb.append("/secure/apps/proxy/credential?").append(queryString);
        } else {
            sb.append(PROXY_URL_BASE).append(userid).append(TICKET_PARAM).append(ticket).append('&').append(queryString);
        }
        RedirectView view = new RedirectView(sb.toString(), true, true);
        view.setExpandUriTemplateVariables(false);
        return view;
    }

    @RequestMapping(value = "/secure/apps/proxy/credential")
    public View secureProxyRedirect(
            final HttpServletRequest request,
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
        this.binder.bind(model.asMap(), request);
        // case 74082 /apps/proxy/credential causes an error, need to put null
        // values into the model
        if (!model.containsAttribute(Model.USER_ID)) {
            model.addAttribute(Model.USER_ID, null);
            model.addAttribute(Model.TICKET, null);
        }
    }
}

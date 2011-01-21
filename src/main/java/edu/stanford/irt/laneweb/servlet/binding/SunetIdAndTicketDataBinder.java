package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

public class SunetIdAndTicketDataBinder implements DataBinder {

    private String ezproxyKey;

    private SunetIdSource sunetIdSource = new SunetIdSource();

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String sunetid = this.sunetIdSource.getSunetid(request);
        if (sunetid != null) {
            model.put(Model.SUNETID, sunetid);
            HttpSession session = request.getSession();
            Ticket ticket = (Ticket) session.getAttribute(Model.TICKET);
            if (ticket == null || !ticket.isValid()) {
                ticket = new Ticket(sunetid, this.ezproxyKey);
            }
            session.setAttribute(Model.TICKET, ticket);
            model.put(Model.TICKET, ticket);
        }
    }

    public void setEzproxyKey(final String ezproxyKey) {
        this.ezproxyKey = ezproxyKey;
    }
}

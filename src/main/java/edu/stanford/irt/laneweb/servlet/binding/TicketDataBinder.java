package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;


public class TicketDataBinder implements DataBinder {
    
    private String ezproxyKey;

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        String sunetid = (String) model.get(Model.SUNETID);
        if (sunetid != null) {
            HttpSession session = request.getSession();
            Ticket ticket = (Ticket) session.getAttribute(Model.TICKET);
            if (ticket == null || !ticket.isValid()) {
                ticket = new Ticket(sunetid, this.ezproxyKey);
                session.setAttribute(Model.TICKET, ticket);
                model.put(Model.TICKET, ticket);
            }
            session.setAttribute(Model.TICKET, ticket);
            model.put(Model.TICKET, ticket);
        }
    }
    
    public void setEzproxyKey(String ezproxyKey) {
        this.ezproxyKey = ezproxyKey;
    }
}

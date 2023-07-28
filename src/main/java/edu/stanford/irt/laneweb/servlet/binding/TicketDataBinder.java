package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.proxy.Ticket;

/**
 * This DataBinder puts an up to date ticket into the model if there is a user id.
 */
public class TicketDataBinder implements DataBinder {

    private String ezproxyKey;

    public TicketDataBinder(final String ezproxyKey) {
        this.ezproxyKey = ezproxyKey;
    }

    /**
     * Adds the ticket to the model.
     */
    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String userid = ModelUtil.getString(model, Model.USER_ID);
        if (userid != null) {
            Ticket ticket;
            HttpSession session = request.getSession();
            // create a new Ticket if it is not in the session or it is not
            // valid.
            ticket = (Ticket) session.getAttribute(Model.TICKET);
            if (ticket == null || !ticket.isValid()) {
                ticket = new Ticket(userid, this.ezproxyKey);
                session.setAttribute(Model.TICKET, ticket);
            }
            model.put(Model.TICKET, ticket);
        }
    }
}

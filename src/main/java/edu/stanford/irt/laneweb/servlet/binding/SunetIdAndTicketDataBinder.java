package edu.stanford.irt.laneweb.servlet.binding;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

/**
 * This DataBinder puts thing related to the sunetid into the model, including the sunetid, the ticket and the hashed
 * sunetid.
 */
public class SunetIdAndTicketDataBinder implements DataBinder {

    private String ezproxyKey;

    private String sunetidHashKey;

    private SunetIdSource sunetIdSource;

    public SunetIdAndTicketDataBinder(final SunetIdSource sunetIdSource, final String ezproxyKey,
            final String sunetidHashKey) {
        this.sunetIdSource = sunetIdSource;
        this.ezproxyKey = ezproxyKey;
        this.sunetidHashKey = sunetidHashKey;
    }

    /**
     * Adds the sunetid, ticket and hashed sunetid to the model.
     */
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        // the SunetidSource takes care of putting the sunetid into the session
        // TODO: reconsider that as part of restricting access to the session to
        // this package.
        String sunetid = this.sunetIdSource.getSunetid(request);
        if (sunetid != null) {
            Ticket ticket = null;
            String auth = null;
            HttpSession session = request.getSession();
            synchronized (session) {
                // create a new Ticket if it is not in the session or it is not
                // valid.
                ticket = (Ticket) session.getAttribute(Model.TICKET);
                if (ticket == null || !ticket.isValid()) {
                    ticket = new Ticket(sunetid, this.ezproxyKey);
                    session.setAttribute(Model.TICKET, ticket);
                }
                // create a new hashed sunetid if it is not in the session
                auth = (String) session.getAttribute(Model.AUTH);
                if (auth == null) {
                    auth = getDigest(getDigest(this.sunetidHashKey + sunetid));
                    session.setAttribute(Model.AUTH, auth);
                }
            }
            model.put(Model.SUNETID, sunetid);
            model.put(Model.TICKET, ticket);
            model.put(Model.AUTH, auth);
        }
    }

    private String getDigest(final String buffer) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(buffer.getBytes(StandardCharsets.UTF_8));
            for (byte element : bytes) {
                sb.append(Integer.toHexString((element & 0xf0) >> 4) + Integer.toHexString(element & 0x0f));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new LanewebException(e);
        }
        return sb.toString();
    }
}

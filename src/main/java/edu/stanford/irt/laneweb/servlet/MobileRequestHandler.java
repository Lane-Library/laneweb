package edu.stanford.irt.laneweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.IPGroup;

/**
 * This subclass of ModelAugmentingRequestHandler doesn't put stuff that can change
 * while roaming into the session.
 * 
 * @author ceyates
 *
 */
public class MobileRequestHandler extends ModelAugmentingRequestHandler {

    @Override
    protected IPGroup getIPGroup(final String remoteAddr, final HttpSession session) {
        return IPGroup.getGroupForIP(remoteAddr);
    }

    @Override
    protected String getRemoteAddr(final HttpServletRequest request, final HttpSession session) {
        // mod_proxy puts the real remote address in an x-forwarded-for
        // header
        // Load balancer also does this
        String header = request.getHeader(X_FORWARDED_FOR);
        if (header == null) {
            return request.getRemoteAddr();
        } else {
            if (header.indexOf(',') > 0) {
                return header.substring(0, header.indexOf(','));
            } else {
                return header;
            }
        }
    }
}

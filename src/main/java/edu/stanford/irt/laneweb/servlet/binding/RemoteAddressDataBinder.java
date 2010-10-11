package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;

public class RemoteAddressDataBinder implements DataBinder {

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String ip = (String) session.getAttribute(Model.REMOTE_ADDR);
        if (ip == null) {
            ip = getRemoteAddress(request);
            session.setAttribute(Model.REMOTE_ADDR, ip);
        }
        model.put(Model.REMOTE_ADDR, ip);
    }
    
    protected String getRemoteAddress(HttpServletRequest request) {
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

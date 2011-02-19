package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

@Controller
public class IPGroupFetchController {

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    @RequestMapping(value = "**/apps/ipGroupFetch")
    public void getIPGroup(final HttpServletRequest request, final HttpServletResponse response, @RequestParam(required = false) final String callback)
            throws IOException {
        String currentIP = getRemoteAddress(request);
        HttpSession session = request.getSession();
        boolean isSameIP = currentIP.equals(session.getAttribute(Model.REMOTE_ADDR));
        if (!isSameIP) {
            session.setAttribute(Model.REMOTE_ADDR, currentIP);
        }
        IPGroup ipGroup = (IPGroup) session.getAttribute(Model.IPGROUP);
        if (ipGroup == null || !isSameIP) {
            ipGroup = IPGroup.getGroupForIP(currentIP);
            session.setAttribute(Model.IPGROUP, ipGroup);
        }
        response.setHeader("Content-Type", "application/x-javascript");
        if (callback != null) {
            response.getWriter().write(callback + "('" + ipGroup + "');");
        } else {
            response.getWriter().write(ipGroup.toString());
        }
    }

    private String getRemoteAddress(final HttpServletRequest request) {
        // mod_proxy puts the real remote address in an x-forwarded-for
        // header
        // Load balancer also does this
        String header = request.getHeader(X_FORWARDED_FOR);
        if (header == null) {
            return request.getRemoteAddr();
        } else if (header.indexOf(',') > 0) {
            return header.substring(header.lastIndexOf(",") + 1, header.length()).trim();
        } else {
            return header;
        }
    }
}

package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.Model;


public class IPGroupDataBinder implements DataBinder {

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        IPGroup ipGroup = (IPGroup) session.getAttribute(Model.IPGROUP);
        if (ipGroup == null) {
            ipGroup = IPGroup.getGroupForIP((String) model.get(Model.REMOTE_ADDR));
            session.setAttribute(Model.IPGROUP, ipGroup);
        }
        model.put(Model.IPGROUP, ipGroup);
    }
}

package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.Model;


public class MobileIPGroupDataBinder implements DataBinder {

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        model.put(Model.IPGROUP, IPGroup.getGroupForIP((String) model.get(Model.REMOTE_ADDR)));
    }
}

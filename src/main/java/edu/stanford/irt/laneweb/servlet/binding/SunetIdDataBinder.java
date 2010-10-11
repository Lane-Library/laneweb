package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;


public class SunetIdDataBinder implements DataBinder {

    private SunetIdSource sunetIdSource = new SunetIdSource();

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        String sunetid = this.sunetIdSource.getSunetid(request, request.getSession());
        if (sunetid != null) {
            model.put(Model.SUNETID, sunetid);
        }
    }
}

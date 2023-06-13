package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class UnividDataBinder implements DataBinder {

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String userid = ModelUtil.getString(model, Model.USER_ID);
        if (userid != null && userid.contains("@stanford.edu")) {
            HttpSession session = request.getSession();
            String univid = (String) session.getAttribute(Model.UNIVID);
            if (univid == null) {
                univid = (String) request.getAttribute("suUnivID");
                if (univid != null) {
                    session.setAttribute(Model.UNIVID, univid);
                }
            }
            if (univid != null) {
                model.put(Model.UNIVID, univid);
            }
        }
    }
}

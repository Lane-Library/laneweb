package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;

public class ActiveSunetidDataBinder implements DataBinder {

    private LDAPDataAccess ldapDataAccess;

    public ActiveSunetidDataBinder(final LDAPDataAccess ldapDataAccess) {
        this.ldapDataAccess = ldapDataAccess;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Boolean isActive = null;
        String userid = ModelUtil.getString(model, Model.USER_ID);
        if (userid != null && userid.indexOf("@stanford.edu") > -1) {
            HttpSession session = request.getSession();
            isActive = (Boolean) session.getAttribute(Model.IS_ACTIVE_SUNETID);
            if (isActive == null) {
                String sunetid = userid.substring(0, userid.indexOf('@'));
                isActive = Boolean.valueOf(this.ldapDataAccess.isActive(sunetid));
                session.setAttribute(Model.IS_ACTIVE_SUNETID, isActive);
            }
        } else {
            isActive = Boolean.FALSE;
        }
        model.put(Model.IS_ACTIVE_SUNETID, isActive);
    }
}

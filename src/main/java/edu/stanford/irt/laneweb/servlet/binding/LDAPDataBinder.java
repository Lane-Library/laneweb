package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.LDAPData;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;

public class LDAPDataBinder implements DataBinder {

    private LDAPDataAccess ldapDataAccess;

    public LDAPDataBinder(final LDAPDataAccess ldapDataAccess) {
        this.ldapDataAccess = ldapDataAccess;
    }

    // TODO: create an immutable user object with all this stuff and userid, etc.
    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String userid = (String) model.get(Model.USER_ID);
        if (userid != null) {
            String name;
            String univid;
            String email;
            Boolean isActive;
            String sunetid = userid.substring(0, userid.indexOf('@'));
            HttpSession session = request.getSession();
            name = (String) session.getAttribute(Model.NAME);
            univid = (String) session.getAttribute(Model.UNIVID);
            isActive = (Boolean) session.getAttribute(Model.IS_ACTIVE_SUNETID);
            email = (String) session.getAttribute(Model.EMAIL);
            if (name == null) {
                LDAPData ldapData = this.ldapDataAccess.getLdapDataForSunetid(sunetid);
                name = ldapData.getName();
                univid = ldapData.getUnivId();
                isActive = Boolean.valueOf(ldapData.isActive());
                email = ldapData.getEmailAddress();
                session.setAttribute(Model.NAME, name);
                session.setAttribute(Model.UNIVID, univid);
                session.setAttribute(Model.IS_ACTIVE_SUNETID, isActive);
                session.setAttribute(Model.EMAIL, email);
            }
            model.put(Model.NAME, name);
            if (univid != null) {
                model.put(Model.UNIVID, univid);
            }
            if (isActive != null) {
                model.put(Model.IS_ACTIVE_SUNETID, isActive);
            }
            if (email != null) {
                model.put(Model.EMAIL, email);
            }
        }
    }
}

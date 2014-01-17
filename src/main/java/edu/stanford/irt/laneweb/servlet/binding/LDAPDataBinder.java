package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.ldap.LDAPData;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;

public class LDAPDataBinder implements DataBinder {

    private LDAPDataAccess ldapDataAccess;

    public LDAPDataBinder(final LDAPDataAccess ldapDataAccess) {
        this.ldapDataAccess = ldapDataAccess;
    }

    //TODO: create an immutable user object with all this stuff and sunetid, etc.
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String sunetid = (String) model.get(Model.SUNETID);
        if (sunetid != null) {
            String name = null;
            String univid = null;
            Boolean isActive = null;
            String email = null;
            HttpSession session = request.getSession();
            synchronized(session) {
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

package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.ldap.LDAPData;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;

public class LDAPDataBinder implements DataBinder {

    private LDAPDataAccess ldapDataAccess;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String sunetid = (String) model.get(Model.SUNETID);
        if (sunetid != null) {
            HttpSession session = request.getSession();
            String name = (String) session.getAttribute(Model.NAME);
            String univid = (String) session.getAttribute(Model.UNIVID);
            Boolean isActive = (Boolean)session.getAttribute(Model.IS_ACTIVE_SUNETID);
            if (name == null) {
                LDAPData ldapData = this.ldapDataAccess.getLdapData(sunetid);
                name = ldapData.getName();
                isActive = ldapData.isActive();
                univid = ldapData.getUnivId();
                session.setAttribute(Model.NAME, name);
                session.setAttribute(Model.UNIVID, univid);
                session.setAttribute(Model.IS_ACTIVE_SUNETID,isActive);
            }
            model.put(Model.NAME, name);
            if (univid != null) {
                model.put(Model.UNIVID, univid);
            }
        }
    }

    public void setLDAPDataAccess(final LDAPDataAccess ldapDataAccess) {
        this.ldapDataAccess = ldapDataAccess;
    }
}

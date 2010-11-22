package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.Model;
import edu.stanford.irt.laneweb.ldap.LDAPData;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;

public class LDAPDataBinder implements DataBinder {

    private LDAPDataAccess ldapDataAccess;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String sunetid = (String) model.get(Model.SUNETID);
        if (sunetid != null) {
            HttpSession session = request.getSession();
            String name = (String) session.getAttribute(Model.NAME);
            String univid = (String) session.getAttribute(Model.UNIVID);
            if (name == null) {
                LDAPData ldapData = this.ldapDataAccess.getLdapData(sunetid);
                name = ldapData.getName();
                univid = ldapData.getUnivId();
                session.setAttribute(Model.NAME, name);
                session.setAttribute(Model.UNIVID, univid);
            }
            model.put(Model.NAME, name);
            if (univid != null) {
                model.put(Model.UNIVID, univid);
            }
        }
    }
    
    public void setLDAPDataAccess(LDAPDataAccess ldapDataAccess) {
        this.ldapDataAccess = ldapDataAccess;
    }
}

package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;

@Controller
public class VoyagerLoginController {

    private static final String BEAN_ROOT_NAME = VoyagerLogin.class.getName() + "/";

    @Autowired
    private LDAPDataAccess ldapDataSource;

    private SunetIdSource sunetIdSource = new SunetIdSource();

    @Autowired
    private Map<String, VoyagerLogin> voyagerLogins;

    @ModelAttribute(Model.SUNETID)
    public String getSunetid(final HttpServletRequest request) {
        return this.sunetIdSource.getSunetid(request);
    }

    @ModelAttribute(Model.UNIVID)
    public String getUnivId(final HttpSession session, @ModelAttribute(Model.SUNETID) final String sunetid) {
        String univId = (String) session.getAttribute(Model.UNIVID);
        if (univId == null) {
            univId = this.ldapDataSource.getLdapData(sunetid).getUnivId();
            if (univId != null) {
                session.setAttribute(Model.UNIVID, univId);
            }
        }
        return univId;
    }

    @RequestMapping(value = "/secure/voyager/{db}")
    public void login(@PathVariable final String db, @ModelAttribute(Model.UNIVID) final String univid,
            @RequestParam final String PID, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        VoyagerLogin voyagerLogin = this.voyagerLogins.get(BEAN_ROOT_NAME + db);
        String queryString = request.getQueryString();
        String url = voyagerLogin.getVoyagerURL(univid, PID, queryString);
        response.sendRedirect(url);
    }

    public void setVoyagerLogins(final Map<String, VoyagerLogin> voyagerLogins) {
        this.voyagerLogins = voyagerLogins;
    }
}

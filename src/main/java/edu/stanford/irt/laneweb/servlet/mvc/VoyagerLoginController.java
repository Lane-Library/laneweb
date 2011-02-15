package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;

@Controller
@SessionAttributes({Model.UNIVID, Model.SUNETID})
public class VoyagerLoginController {
    
    private SunetIdSource sunetIdSource = new SunetIdSource();
    
    @Autowired
    private LDAPDataAccess ldapDataSource;

    @Autowired
    private VoyagerLogin voyagerLogin;

    @RequestMapping(value = "/secure/voyager.html")
    public void voyagerLogin(@ModelAttribute(Model.UNIVID) final String univid, @RequestParam final String PID, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        String queryString = request.getQueryString();
        String url = this.voyagerLogin.getVoyagerURL(univid, PID, queryString);
        response.sendRedirect(url);
    }

    public void setVoyagerLogin(VoyagerLogin voyagerLogin) {
        this.voyagerLogin = voyagerLogin;
    }
    
    @ModelAttribute(Model.UNIVID)
    public String getUnivId(@ModelAttribute(Model.SUNETID) String sunetid) {
        return this.ldapDataSource.getLdapData(sunetid).getUnivId();
    }


    @ModelAttribute(Model.SUNETID)
    public String getSunetid(final HttpServletRequest request) {
        return this.sunetIdSource.getSunetid(request);
    }
}

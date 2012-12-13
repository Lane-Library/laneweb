package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;
import edu.stanford.irt.laneweb.servlet.binding.LDAPDataBinder;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;

@Controller
public class VoyagerLoginController {

    private static final String BEAN_ROOT_NAME = VoyagerLogin.class.getName() + "/";

    @Autowired
    private LDAPDataBinder ldapDataBinder;

    private final Logger log = LoggerFactory.getLogger(VoyagerLoginController.class);

    @Autowired
    private SunetIdSource sunetIdSource;

    @Autowired
    private Map<String, VoyagerLogin> voyagerLogins;

    @ModelAttribute(Model.SUNETID)
    public String getSunetid(final HttpServletRequest request) {
        String sunetid = this.sunetIdSource.getSunetid(request);
        this.log.error("getSunetid: " + sunetid);
        return sunetid;
    }

    @ModelAttribute(Model.UNIVID)
    public void getUnivid(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.log.error("model before ldapDataBinder: " + model);
        this.ldapDataBinder.bind(model.asMap(), request);
        this.log.error("model after ldapDataBinder: " + model);
    }

    @RequestMapping(value = "/secure/voyager/{db}")
    public void login(@PathVariable final String db, @RequestParam("PID") final String pid,
            @ModelAttribute(Model.SUNETID) final String sunetid, @ModelAttribute(Model.UNIVID) final String univid,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        VoyagerLogin voyagerLogin = this.voyagerLogins.get(BEAN_ROOT_NAME + db);
        String queryString = request.getQueryString();
        String url = voyagerLogin.getVoyagerURL(univid, pid, queryString);
        response.sendRedirect(url);
    }

    public void setVoyagerLogins(final Map<String, VoyagerLogin> voyagerLogins) {
        this.voyagerLogins = voyagerLogins;
    }
}

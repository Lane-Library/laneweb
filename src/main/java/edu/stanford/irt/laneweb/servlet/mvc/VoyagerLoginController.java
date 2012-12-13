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

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.LDAPDataBinder;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;

@Controller
public class VoyagerLoginController {

    private static final String BEAN_ROOT_NAME = VoyagerLogin.class.getName() + "/";

    @Autowired
    private LDAPDataBinder ldapDataBinder;

    @Autowired
    private Map<String, VoyagerLogin> voyagerLogins;

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.ldapDataBinder.bind(model.asMap(), request);
    }

    @RequestMapping(value = "/secure/voyager/{db}")
    public void login(@PathVariable final String db, @RequestParam("PID") final String pid, final HttpSession session,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String univid = (String) session.getAttribute(Model.UNIVID);
        VoyagerLogin voyagerLogin = this.voyagerLogins.get(BEAN_ROOT_NAME + db);
        String queryString = request.getQueryString();
        String url = voyagerLogin.getVoyagerURL(univid, pid, queryString);
        response.sendRedirect(url);
    }

    public void setVoyagerLogins(final Map<String, VoyagerLogin> voyagerLogins) {
        this.voyagerLogins = voyagerLogins;
    }
}

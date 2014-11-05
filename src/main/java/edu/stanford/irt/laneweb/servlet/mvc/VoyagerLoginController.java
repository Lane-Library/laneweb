package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.LDAPDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;

@Controller
public class VoyagerLoginController {

    @Autowired
    private LDAPDataBinder ldapDataBinder;

    @Autowired
    private UserDataBinder userBinder;

    @Autowired
    private VoyagerLogin voyagerLogin;

    @ModelAttribute(Model.UNIVID)
    public void getUnivid(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.userBinder.bind(model.asMap(), request);
        this.ldapDataBinder.bind(model.asMap(), request);
    }

    @RequestMapping(value = "/secure/voyager/lmldb")
    public void login(@RequestParam("PID") final String pid, @ModelAttribute(Model.UNIVID) final String univid,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String queryString = request.getQueryString();
        String url = this.voyagerLogin.getVoyagerURL(univid, pid, queryString);
        response.sendRedirect(url);
    }

    public void setVoyagerLogin(final VoyagerLogin voyagerLogin) {
        this.voyagerLogin = voyagerLogin;
    }
}

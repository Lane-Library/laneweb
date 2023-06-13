package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.UnividDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;

@Controller
public class VoyagerLoginController {

    private UnividDataBinder unividDataBinder;

    private UserDataBinder userDataBinder;

    private VoyagerLogin voyagerLogin;

    public VoyagerLoginController(final VoyagerLogin voyagerLogin, final UserDataBinder userDataBinder,
            final UnividDataBinder unividDataBinder) {
        this.voyagerLogin = voyagerLogin;
        this.userDataBinder = userDataBinder;
        this.unividDataBinder = unividDataBinder;
    }

    @GetMapping(value = "/secure/voyager/lmldb")
    public void login(@RequestParam("PID") final String pid,
            @ModelAttribute(Model.UNIVID) final String univid,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        String queryString = request.getQueryString();
        String url = this.voyagerLogin.getVoyagerURL(univid, pid, queryString);
        response.sendRedirect(url);
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.userDataBinder.bind(model.asMap(), request);
        this.unividDataBinder.bind(model.asMap(), request);
    }
}

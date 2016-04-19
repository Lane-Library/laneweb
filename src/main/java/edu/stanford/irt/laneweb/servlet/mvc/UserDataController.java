package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;

@Controller
public class UserDataController {

    private UserDataBinder binder;

    @Autowired
    public UserDataController(final UserDataBinder binder) {
        this.binder = binder;
    }

    @RequestMapping(value = "/apps/userData")
    @ResponseBody
    public Map<String, Object> getUserData(@ModelAttribute(Model.USER) final User user) {
        return Collections.singletonMap(Model.USER, user);
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.binder.bind(model.asMap(), request);
        if (!model.containsAttribute(edu.stanford.irt.laneweb.model.Model.USER)) {
            model.addAttribute(edu.stanford.irt.laneweb.model.Model.USER, null);
        }
    }
}

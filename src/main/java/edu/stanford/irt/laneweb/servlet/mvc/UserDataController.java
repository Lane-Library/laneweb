package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

@Controller
public class UserDataController {

    private DataBinder binder;

    public UserDataController(
            @Qualifier("edu.stanford.irt.laneweb.servlet.binding.DataBinder/userdata") final DataBinder binder) {
        this.binder = binder;
    }

    @GetMapping(value = "/apps/userData")
    @ResponseBody
    public Map<String, Object> getUserData(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.binder.bind(model.asMap(), request);
        return new HashMap<>(model.asMap());
    }
}

package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;

@Controller
public class IPGroupFetchController {

    private static final Pattern CALLBACK_PATTERN = Pattern.compile("[A-Za-z\\.]+");

    private RemoteProxyIPDataBinder binder;

    public IPGroupFetchController(final RemoteProxyIPDataBinder binder) {
        this.binder = binder;
    }

    @GetMapping(value = "/apps/ipGroupFetch", produces = "application/javascript")
    @ResponseBody
    public String getIPGroup(@ModelAttribute(Model.IPGROUP) final IPGroup ipGroup,
            @RequestParam(required = false) final String callback) {
        if (callback == null || !CALLBACK_PATTERN.matcher(callback).matches()) {
            return ipGroup.toString();
        }
        return callback + "('" + ipGroup + "');";
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.binder.bind(model.asMap(), request);
    }
}

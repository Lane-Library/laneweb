package edu.stanford.irt.laneweb.servlet.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;

@Controller
public class IPGroupFetchController {

    private RemoteProxyIPDataBinder binder;

    public IPGroupFetchController(final RemoteProxyIPDataBinder binder) {
        this.binder = binder;
    }

    @RequestMapping(value = "/apps/ipGroupFetch", method = RequestMethod.GET, produces = "application/javascript")
    @ResponseBody
    public String getIPGroup(@ModelAttribute(Model.IPGROUP) final IPGroup ipGroup,
            @RequestParam(required = false) final String callback) {
        if (callback == null) {
            return ipGroup.toString();
        } else {
            return callback + "('" + ipGroup + "');";
        }
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.binder.bind(model.asMap(), request);
    }
}

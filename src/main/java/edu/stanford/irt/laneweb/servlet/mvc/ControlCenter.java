package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.SpamFilter;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

@Controller
@RequestMapping(value = "/control-center")
public class ControlCenter {

    private Set<String> admins;

    private ApplicationContext context;

    private UserDataBinder userBinder;

    @Autowired
    public ControlCenter(final ApplicationContext context, final UserDataBinder userBinder) {
        this.userBinder = userBinder;
        this.context = context;
        this.admins = new HashSet<>();
        this.admins.add("alainb@stanford.edu");
        this.admins.add("ceyates@stanford.edu");
        this.admins.add("ryanmax@stanford.edu");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/email/spamIP")
    @ResponseBody
    public String addSpamIP(@RequestParam final String ip,
            @ModelAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID) final String userid) {
        checkAccess(userid);
        this.context.getBean(SpamFilter.class).addSpamIP(ip);
        return ip;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/email/spamReferrer")
    @ResponseBody
    public String addSpamReferrer(@RequestParam final String referrer,
            @ModelAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID) final String userid) {
        checkAccess(userid);
        this.context.getBean(SpamFilter.class).addSpamReferrer(referrer);
        return referrer;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/email/spamIP")
    @ResponseBody
    public boolean removeSpamIP(@RequestParam final String ip,
            @ModelAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID) final String userid) {
        checkAccess(userid);
        return this.context.getBean(SpamFilter.class).removeSpamIP(ip);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/email/spamReferrer")
    @ResponseBody
    public boolean removeSpamReferrer(@RequestParam final String referrer,
            @ModelAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID) final String userid) {
        checkAccess(userid);
        return this.context.getBean(SpamFilter.class).removeSpamReferrer(referrer);
    }

    @ModelAttribute
    protected void getParameters(final HttpServletRequest request, final Model model) {
        this.userBinder.bind(model.asMap(), request);
    }

    private void checkAccess(final String userid) {
        if (!this.admins.contains(userid)) {
            throw new LanewebException(userid + " not authorized");
        }
    }
}

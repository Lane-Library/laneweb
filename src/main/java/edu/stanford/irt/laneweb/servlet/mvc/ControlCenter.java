package edu.stanford.irt.laneweb.servlet.mvc;

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
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.SunetIdAndTicketDataBinder;

@Controller
@RequestMapping(value="/control-center")
public class ControlCenter {

    private ApplicationContext context;

    private SunetIdAndTicketDataBinder sunetidBinder;

    @Autowired
    public ControlCenter(final ApplicationContext context, final SunetIdAndTicketDataBinder sunetidBinder) {
        this.sunetidBinder = sunetidBinder;
        this.context = context;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/email/spamIP")
    @ResponseBody
    public String addSpamIP(@RequestParam final String ip,
            @ModelAttribute(edu.stanford.irt.laneweb.model.Model.SUNETID) final String sunetid) {
        checkAccess(sunetid);
        this.context.getBean(EMailSender.class).addSpamIP(ip);
        return ip;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/email/spamIP")
    @ResponseBody
    public boolean removeSpamIP(@RequestParam final String ip,
            @ModelAttribute(edu.stanford.irt.laneweb.model.Model.SUNETID) final String sunetid) {
        checkAccess(sunetid);
        return this.context.getBean(EMailSender.class).removeSpamIP(ip);
    }

    @ModelAttribute
    protected void getParameters(final HttpServletRequest request, final Model model) {
        this.sunetidBinder.bind(model.asMap(), request);
    }

    private void checkAccess(final String sunetid) {
        if (!"ceyates".equals(sunetid)) {
            throw new LanewebException(sunetid + " not authorized");
        }
    }
}

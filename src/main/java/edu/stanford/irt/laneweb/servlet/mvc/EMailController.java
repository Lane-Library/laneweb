package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RequestHeaderDataBinder;

@Controller
public class EMailController {

    @Autowired
    private RequestHeaderDataBinder headerBinder;

    @Autowired
    private RemoteProxyIPDataBinder remoteIPBinder;

    @Autowired
    private EMailSender sender;

    @RequestMapping(value = "/apps/mail")
    public String sendMail(final Model model, final RedirectAttributes atts) {
        Map<String, Object> map = model.asMap();
        this.sender.sendEmail(map);
        return "redirect:" + map.get("redirect");
    }

    @ModelAttribute
    protected void getParameters(final HttpServletRequest request, final Model model) {
        this.remoteIPBinder.bind(model.asMap(), request);
        this.headerBinder.bind(model.asMap(), request);
        @SuppressWarnings("unchecked")
        Map<String, String[]> map = request.getParameterMap();
        for (Entry<String, String[]> entry : map.entrySet()) {
            String[] value = entry.getValue();
            if (value.length == 1) {
                model.addAttribute(entry.getKey(), value[0]);
            } else {
                throw new LanewebException("multiple values for parameter " + entry.getKey());
            }
        }
    }
}

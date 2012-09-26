package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EMailController {

    @RequestMapping(value = "/apps/mail")
    public Map<String, ?> sendMail(final Model model) {
        return model.asMap();
    }

    @ModelAttribute
    protected void getParameters(final HttpServletRequest request, final Model model) {
        @SuppressWarnings("unchecked")
        Map<String, String[]> map = request.getParameterMap();
        for (Entry<String, String[]> entry : map.entrySet()) {
            model.addAttribute(entry.getKey(), entry.getValue()[0]);
        }
    }
}

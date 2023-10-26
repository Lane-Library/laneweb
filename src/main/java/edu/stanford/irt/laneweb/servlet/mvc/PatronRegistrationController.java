package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.folio.rest.jaxrs.model.Address;
import org.folio.rest.jaxrs.model.Personal;
import org.folio.rest.jaxrs.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.folio.UserService;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

@Controller
@RequestMapping(value = "/patron-registration/")
public class PatronRegistrationController {

    private static final String ASKUS_ADDRESS = "LaneAskUs@stanford.edu";

    private static final String CONFIRMATION_PAGE = "redirect:/patron-registration/confirmation.html";

    private static final String ERROR_PAGE = "redirect:/patron-registration/error.html";

    private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final Logger log = LoggerFactory.getLogger(PatronRegistrationController.class);

    private UserService folioUserService;

    private EMailSender sender;

    private DataBinder userDataBinder;

    public PatronRegistrationController(final UserService folioUserService, final DataBinder userDataBinder,
            final EMailSender sender) {
        this.folioUserService = folioUserService;
        this.userDataBinder = userDataBinder;
        this.sender = sender;
    }

    @PostMapping(value = "register", consumes = FORM_MIME_TYPE)
    public String formSubmitUserRegistration(final Model model, final RedirectAttributes atts) {
        Map<String, Object> map = model.asMap();
        User user = folioUserFromMap(map);
        try {
            if (this.folioUserService.addUser(user)) {
                map.put("recipient", ASKUS_ADDRESS);
                map.remove(edu.stanford.irt.laneweb.model.Model.USER);
                map.remove(edu.stanford.irt.laneweb.model.Model.AUTH);
                this.sender.sendEmail(map);
                return CONFIRMATION_PAGE;
            }
        } catch (LanewebException e) {
            log.error(e.getMessage(), e);
        }
        return ERROR_PAGE;
    }

    private User folioUserFromMap(final Map<String, Object> map) {
        User user = new User();
        Personal personal = new Personal();
        personal.setPreferredContactTypeId("002");
        Address address = new Address();
        String userid = map.getOrDefault("userid", "").toString();
        if (userid.contains("@")) {
            user.setUsername(userid.substring(0, userid.indexOf('@')));
        }
        personal.setEmail(map.getOrDefault("suEmail", "").toString());
        personal.setFirstName(map.getOrDefault("firstName", "").toString());
        personal.setMiddleName(map.getOrDefault("middleName", "").toString());
        personal.setLastName(map.getOrDefault("lastName", "").toString());
        personal.setPhone(map.getOrDefault("phone", "").toString());
        address.setAddressTypeId(map.getOrDefault("addressTypeId", "93d3d88d-499b-45d0-9bc7-ac73c3a19880").toString());
        address.setAddressLine1(map.getOrDefault("addressLine1", "").toString());
        address.setAddressLine2(map.getOrDefault("addressLine2", "").toString());
        address.setCity(map.getOrDefault("city", "").toString());
        address.setRegion(map.getOrDefault("state", "").toString());
        address.setPostalCode(map.getOrDefault("zip", "").toString());
        personal.setAddresses(Collections.singletonList(address));
        user.setPersonal(personal);
        return user;
    }

    @ModelAttribute
    protected void getParameters(final HttpServletRequest request, final Model model) {
        Map<String, Object> modelMap = model.asMap();
        Map<String, String[]> map = request.getParameterMap();
        this.userDataBinder.bind(modelMap, request);
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

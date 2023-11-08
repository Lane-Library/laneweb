package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    public PatronRegistrationController(final UserService folioUserService, final DataBinder userDataBinder, final EMailSender sender) {
        this.folioUserService = folioUserService;
        this.userDataBinder = userDataBinder;
        this.sender = sender;
    }

    @PostMapping(value = "register", consumes = FORM_MIME_TYPE)
    public String formSubmitUserRegistration(final @ModelAttribute(FOLIO_USER) Map<String, Object> user,
            final Model model) {
        Map<String, Object> map = model.asMap();
        try {
            if (this.folioUserService.addUser(user)) {
                map.put("recipient", ASKUS_ADDRESS);
                map.remove(FOLIO_USER);
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

    @ModelAttribute
    protected void getParameters(final HttpServletRequest req, final Model model) {
        this.userDataBinder.bind(model.asMap(), req);
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> personal = new HashMap<>();
        Map<String, Object> address = new HashMap<>();
        user.put(PREFERERED_CONTACT_TYPE_ID, "002");
        user.put(DEPARTMENTS, Collections.emptySet());
        user.put(PROXY_FOR, Collections.emptySet());
        String userid = (String) model.getAttribute(USER_ID);
        if (userid != null && userid.contains("@")) {
            userid = userid.substring(0, userid.indexOf('@'));
            model.addAttribute(USER_ID, userid);
            user.put(USER_NAME, userid);
        }
        personal.put(LAST_NAME, getValueOrDefault(model, req, LAST_NAME, ""));
        personal.put(FIRST_NAME, getValueOrDefault(model, req, FIRST_NAME, ""));
        personal.put(MIDDLE_NAME, getValueOrDefault(model, req, MIDDLE_NAME, ""));
        personal.put(EMAIL, getValueOrDefault(model, req, SU_EMAIL, ""));
        personal.put(PHONE, getValueOrDefault(model, req, PHONE, ""));
        address.put(ADDRESSES_LINE_1, getValueOrDefault(model, req, ADDRESSES_LINE_1, ""));
        address.put(ADDRESSES_LINE_2, getValueOrDefault(model, req, ADDRESSES_LINE_2, ""));
        address.put(CITY, getValueOrDefault(model, req, CITY, ""));
        address.put(REGION, getValueOrDefault(model, req, STATE, ""));
        address.put(POSTAL_CODE, getValueOrDefault(model, req, ZIP_CODE, ""));
        address.put(ADDRESS_TYPE_ID, getValueOrDefault(model, req, ADDRESS_TYPE_ID, ADDRESS_TYPE_ID_DEFAULT_VALUE));
        personal.put(ADDRESSES, Collections.singleton(address));
        user.put(PERSONAL, personal);
        model.addAttribute(FOLIO_USER, user);
        model.addAttribute(SUBJECT, req.getParameter(SUBJECT));        
    }

    private String getValueOrDefault(final Model model, final HttpServletRequest request, final String key,
            final String defaultValue) {
        String value = (request.getParameter(key) == null) ? defaultValue : request.getParameter(key);
        model.addAttribute(key, value);
        return value;
    }

    private static final String FOLIO_USER = "folio-user";
    
    private static final String PREFERERED_CONTACT_TYPE_ID = "preferredContactTypeId";

    private static final String USER_NAME = "username";

    private static final String USER_ID = "userid";

    private static final String DEPARTMENTS = "departements";

    private static final String PROXY_FOR = "proxyFor";

    private static final String LAST_NAME = "lastName";

    private static final String FIRST_NAME = "firstName";
    
    private static final String MIDDLE_NAME = "middleName";

    private static final String PERSONAL = "personal";

    private static final String EMAIL = "email";

    private static final String SU_EMAIL = "suEmail";

    private static final String ADDRESSES = "addresses";

    private static final String ADDRESSES_LINE_1 = "addressLine1";

    private static final String ADDRESSES_LINE_2 = "addressLine2";

    private static final String CITY = "city";

    private static final String REGION = "region";

    private static final String POSTAL_CODE = "postalCode";

    private static final String ZIP_CODE = "zip";

    private static final String STATE = "state";
    
    private static final String PHONE = "phone";

    private static final String ADDRESS_TYPE_ID = "addressTypeId";

    private static final String ADDRESS_TYPE_ID_DEFAULT_VALUE = "93d3d88d-499b-45d0-9bc7-ac73c3a19880";
    
    private static final String SUBJECT = "subject";

}

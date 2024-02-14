package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
import edu.stanford.irt.laneweb.servlet.binding.UnividDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

@Controller
@RequestMapping(value = "/patron-registration/")
public class PatronRegistrationController {

    private static final String ADDRESS_TYPE_ID = "addressTypeId";

    private static final String ADDRESSES = "addresses";

    private static final String ASKUS_ADDRESS = "LaneAskUs@stanford.edu";

    private static final String ASKUS_EMAIL_SUBJECT = "subject";

    private static final String CONFIRMATION_PAGE = "redirect:/patron-registration/confirmation.html";

    private static final String EMAIL = "email";

    private static final String ERROR_PAGE = "redirect:/patron-registration/error.html";

    private static final String ERROR_USER_EXISTS_PAGE = "redirect:/patron-registration/error-user-exists.html";

    private static final String EXTERNAL_SYSTEM_ID = "externalSystemId";

    private static final String FOLIO_USER = "folio-user";

    private static final String FORM_MIME_TYPE = "application/x-www-form-urlencoded";

    private static final Logger log = LoggerFactory.getLogger(PatronRegistrationController.class);

    private static final String PERSONAL = "personal";

    private static final String[] STRIP_FROM_EMAIL = new String[] { FOLIO_USER,
            "org.springframework.validation.BindingResult." + FOLIO_USER, edu.stanford.irt.laneweb.model.Model.USER,
            edu.stanford.irt.laneweb.model.Model.AUTH };

    private static final String USER_ID = "userid";

    private static final String USER_INPUT_ADDRESSES_LINE_1 = "addressLine1";

    private static final String USER_INPUT_ADDRESSES_LINE_2 = "addressLine2";

    private static final String USER_INPUT_CITY = "city";

    private static final String USER_INPUT_EMAIL = "email";

    private static final String USER_INPUT_FIRST_NAME = "firstName";

    private static final String USER_INPUT_LAST_NAME = "lastName";

    private static final String USER_INPUT_MIDDLE_NAME = "middleName";

    private static final String USER_INPUT_PHONE = "phone";

    private static final String USER_INPUT_POSTAL_CODE = "postalCode";

    private static final String USER_INPUT_PREFERERED_CONTACT_TYPE_ID = "preferredContactTypeId";

    private static final String USER_INPUT_REGION = "region";

    private static final String USER_INPUT_STATE = "state";

    private static final String USER_INPUT_ZIP_CODE = "zip";

    private static final String USERNAME = "username";

    private DataBinder emailDataBinder;

    private UserService folioUserService;

    private EMailSender sender;

    private DataBinder unividDataBinder;

    private DataBinder userDataBinder;

    public PatronRegistrationController(final UserService folioUserService, final UserDataBinder userDataBinder,
            final UnividDataBinder unividDataBinder,
            @Qualifier("edu.stanford.irt.laneweb.servlet.binding.DataBinder/email") final DataBinder emailDataBinder,
            final EMailSender sender) {
        this.folioUserService = folioUserService;
        this.userDataBinder = userDataBinder;
        this.unividDataBinder = unividDataBinder;
        this.emailDataBinder = emailDataBinder;
        this.sender = sender;
    }

    @PostMapping(value = "register", consumes = FORM_MIME_TYPE)
    public String formSubmitUserRegistration(final @ModelAttribute(FOLIO_USER) Map<String, Object> user,
            final Model model, final RedirectAttributes atts) {
        Map<String, Object> map = model.asMap();
        try {
            if (!this.folioUserService.getUser((String) user.get(USERNAME), (String) user.get(EXTERNAL_SYSTEM_ID),
                    (String) model.getAttribute(USER_INPUT_EMAIL)).isEmpty()) {
                return ERROR_USER_EXISTS_PAGE;
            }
            if (this.folioUserService.addUser(user)) {
                map.put("recipient", ASKUS_ADDRESS);
                for (String field : STRIP_FROM_EMAIL) {
                    map.remove(field);
                }
                map.put(EMAIL, nameAndEmail(model));
                this.sender.sendEmail(map);
                return CONFIRMATION_PAGE;
            }
        } catch (LanewebException e) {
            log.error(e.getMessage(), e);
        }
        return ERROR_PAGE;
    }

    private String getValueOrDefault(final Model model, final HttpServletRequest request, final String key,
            final String defaultValue) {
        String value = (request.getParameter(key) == null) ? defaultValue : request.getParameter(key);
        model.addAttribute(key, value);
        return value;
    }

    private String nameAndEmail(final Model model) {
        StringBuilder sb = new StringBuilder();
        sb.append(model.getAttribute(USER_INPUT_FIRST_NAME));
        String middle = (String) model.getAttribute(USER_INPUT_MIDDLE_NAME);
        sb.append(" ");
        if (null != middle && !middle.isBlank()) {
            sb.append(middle);
            sb.append(" ");
        }
        sb.append(model.getAttribute(USER_INPUT_LAST_NAME));
        sb.append(" <");
        sb.append(model.getAttribute(EMAIL));
        sb.append(">");
        return sb.toString();
    }

    @ModelAttribute
    protected void getParameters(final HttpServletRequest req, final Model model) {
        this.userDataBinder.bind(model.asMap(), req);
        this.unividDataBinder.bind(model.asMap(), req);
        this.emailDataBinder.bind(model.asMap(), req);
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> personal = new HashMap<>();
        Map<String, Object> address = new HashMap<>();
        String userid = (String) model.getAttribute(USER_ID);
        if (userid != null && userid.contains("@")) {
            userid = userid.toLowerCase();
            // @stanford.edu users: use SUNetID/userid as FOLIO username, UnivId for FOLIO external system ID
            // hospital users: use SSO userid for both FOLIO username and external system ID
            if (userid.endsWith("@stanford.edu")) {
                user.put(USERNAME, userid.substring(0, userid.indexOf('@')));
                user.put(EXTERNAL_SYSTEM_ID, model.getAttribute(edu.stanford.irt.laneweb.model.Model.UNIVID));
            } else {
                user.put(USERNAME, userid);
                user.put(EXTERNAL_SYSTEM_ID, userid);
            }
            // leave this in the model so staff see SSO username in the notification email
            model.addAttribute(USER_ID, userid);
        }
        personal.put(USER_INPUT_LAST_NAME, getValueOrDefault(model, req, USER_INPUT_LAST_NAME, ""));
        personal.put(USER_INPUT_FIRST_NAME, getValueOrDefault(model, req, USER_INPUT_FIRST_NAME, ""));
        personal.put(USER_INPUT_MIDDLE_NAME, getValueOrDefault(model, req, USER_INPUT_MIDDLE_NAME, ""));
        personal.put(EMAIL, getValueOrDefault(model, req, USER_INPUT_EMAIL, ""));
        personal.put(USER_INPUT_PREFERERED_CONTACT_TYPE_ID, EMAIL);
        personal.put(USER_INPUT_PHONE, getValueOrDefault(model, req, USER_INPUT_PHONE, ""));
        address.put(USER_INPUT_ADDRESSES_LINE_1, getValueOrDefault(model, req, USER_INPUT_ADDRESSES_LINE_1, ""));
        address.put(USER_INPUT_ADDRESSES_LINE_2, getValueOrDefault(model, req, USER_INPUT_ADDRESSES_LINE_2, ""));
        address.put(USER_INPUT_CITY, getValueOrDefault(model, req, USER_INPUT_CITY, ""));
        address.put(USER_INPUT_REGION, getValueOrDefault(model, req, USER_INPUT_STATE, ""));
        address.put(USER_INPUT_POSTAL_CODE, getValueOrDefault(model, req, USER_INPUT_ZIP_CODE, ""));
        address.put(ADDRESS_TYPE_ID, getValueOrDefault(model, req, ADDRESS_TYPE_ID, "Home"));
        personal.put(ADDRESSES, Collections.singleton(address));
        user.put(PERSONAL, personal);
        model.addAttribute(FOLIO_USER, user);
        model.addAttribute(ASKUS_EMAIL_SUBJECT, req.getParameter(ASKUS_EMAIL_SUBJECT));
    }
}

package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;

@RestController
public class AuthenticatedUserDataController {

  private UserDataBinder userBinder;

  public AuthenticatedUserDataController(final UserDataBinder binder) {
    this.userBinder = binder;
  }

  private static final String IDP_AZURE_SHC = "sts.windows.net/9866b506-dc9d-48dd-b720-3a50db77a1cc";

  private static final String AT_STANFORDHEALTHCARE_ORG = "@stanfordhealthcare.org";

  private boolean shcIdpMismatch(final User user, final String idp) {
    return idp.contains(IDP_AZURE_SHC) && !user.getId().contains(AT_STANFORDHEALTHCARE_ORG);
  }

  // using /patron-registration/ for testing convenience; move to its own
  // shib-protected path?
  @GetMapping(value = "/patron-registration/userData")
  public Map<String, Object> getUserData(final HttpServletRequest request, final org.springframework.ui.Model model) {
    this.userBinder.bind(model.asMap(), request);
    User user = (User) model.getAttribute(Model.USER);
    String idp = (String) request.getAttribute("Shib-Identity-Provider");
    if (null != idp && null != user) {
      model.addAttribute("idp", idp.toLowerCase());
      model.addAttribute("shcIdpMismatch", shcIdpMismatch(user, idp));
    }
    return new HashMap<>(model.asMap());
  }

}

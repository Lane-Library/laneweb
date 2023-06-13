package edu.stanford.irt.laneweb.servlet.mvc;

import java.time.Clock;
/**
 * This class will add three cookies the persistent-expired-date and user. The user cookie will have the userid, name,
 * email , the userAgent and the expired date appended and encrypted. The persistent-expired-date cookie have the
 * expired date. So 3 days will be subtract from it to popup a extension window if the user is active and from stanford.
 */
import java.time.Duration;
import java.util.Collection;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.CookieName;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;

@Controller
public class PersistentLoginController {

  private static final long DURATION_MILLIS = Duration.ofDays(28).toMillis();

  // login duration is two weeks:
  private static final int DURATION_SECONDS = Math.toIntExact(Duration.ofDays(28).getSeconds());

  private static final String SAME_SITE = "; SameSite=Lax";

  private static final String COOKIE_HEADERS = "Set-Cookie";

  private UserCookieCodec codec;

  private UserDataBinder userBinder;

  private Clock clock;

  @Autowired
  public PersistentLoginController(final UserDataBinder userBinder, final UserCookieCodec codec) {
    this(userBinder, codec, Clock.systemDefaultZone());
  }

  public PersistentLoginController(final UserDataBinder userBinder, final UserCookieCodec codec, final Clock clock) {
    this.userBinder = userBinder;
    this.codec = codec;
    this.clock = clock;
  }


  @GetMapping(value = { "/secure/persistentLogin/myaccount.html", "/persistentLogin/myaccount.html" })
  public String myaccount(final RedirectAttributes redirectAttrs, @ModelAttribute(Model.USER) final User user,
          final String pl, final HttpServletRequest request, final HttpServletResponse response) {
      if ("true".equals(pl) && null != user) {
          setCookies(request, response, user);
      } else {
          resetCookies(response);
      }
      return "redirect:/myaccounts.html";
  }

  @GetMapping(value = "/secure/persistentLogin.html", params = { "pl=true" })
  public String enablePersistentLogin(final RedirectAttributes redirectAttrs,
          @ModelAttribute(Model.USER) final User user, final String url, final HttpServletRequest request,
          final HttpServletResponse response) {
      if (null != user) {
          setCookies(request, response, user);
          return getRedirectURL(url);
      }
      resetCookies(response);
      return "redirect:/error.html";
  }

  @GetMapping(value = "/secure/login.html")
  public String login(final RedirectAttributes redirectAttrs, String url, User user) {
    if (null != user) {
      return getRedirectURL(url);
    }
    return "redirect:/error.html";
  }

  @ModelAttribute
  protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
    this.userBinder.bind(model.asMap(), request);
    if (!model.containsAttribute(Model.USER)) {
      model.addAttribute(Model.USER, null);
    }
  }

  private String getRedirectURL(final String url) {
    StringBuilder sb = new StringBuilder("redirect:");
    if (null == url) {
      sb.append("/index.html");
    } else {
      sb.append(url);
    }
    return sb.toString();
  }

  private void resetCookies(final HttpServletResponse response) {
    Cookie cookie = new Cookie(CookieName.EXPIRATION.toString(), null);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    cookie = new Cookie(CookieName.USER.toString(), null);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
  }

  /**
   * create and set the lane-user cookie
   *
   * @param user
   * @param request
   * @param response
   */
  private void setCookies(final HttpServletRequest request, final HttpServletResponse response, User user) {
    String userAgent = request.getHeader("User-Agent");
    if (null != userAgent && null != user) {
      PersistentLoginToken token = this.codec.createLoginToken(user, userAgent.hashCode());
      this.addCookie( CookieName.USER.toString(), token.getEncryptedValue(), response);
      long expires = this.clock.millis() + DURATION_MILLIS;
      this.addCookie( CookieName.EXPIRATION.toString(), Long.toString(expires), response);
      addSameSiteToCookies(response);
    }
  }

  private void addCookie(final String name, final String value, final HttpServletResponse response) {
    Cookie cookie = new Cookie( name, value);
    cookie.setPath("/");
    cookie.setMaxAge(DURATION_SECONDS);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    response.addCookie(cookie);
  }

  //Because there is no method to add SameSite=strict with jakarta.servlet.http.Cookie
  private void addSameSiteToCookies(HttpServletResponse response) {
    Collection<String> headers = response.getHeaders(COOKIE_HEADERS);
    boolean cookieReseted = false;
    for (String header : headers) {
      if (header.startsWith(CookieName.EXPIRATION.toString()) || header.startsWith(CookieName.USER.toString())) {
        header = header.concat(SAME_SITE);
      }
      if (!cookieReseted) {
        response.setHeader(COOKIE_HEADERS, header);
        cookieReseted = true;
      } else {
        response.addHeader(COOKIE_HEADERS, header);
      }
    }
  }
}

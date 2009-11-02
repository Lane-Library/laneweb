package edu.stanford.irt.laneweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.processing.ProcessInfoProvider;

import edu.stanford.irt.laneweb.proxy.ProxyLinks;
import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;

public class LanewebObjectModelProvider implements ObjectModelProvider {

    private Map<String, Object> jndiData;

    private ProcessInfoProvider processInfoProvider;

    private ProxyLinks proxyLinks;

    private UserDao userDao;
    
    private TemplateChooser templateChooser;

    public LanewebObjectModelProvider(final ProcessInfoProvider pip, final Map<String, Object> jndiData, final UserDao userDao, final ProxyLinks proxyLinks,
            final TemplateChooser templateChooser) {
        this.processInfoProvider = pip;
        this.jndiData = jndiData;
        this.userDao = userDao;
        this.proxyLinks = proxyLinks;
        this.templateChooser = templateChooser;
    }

    @SuppressWarnings("unchecked")
    public Object getObject() {
        Map objectModel = this.processInfoProvider.getObjectModel();
        Map<String, Object> cocoonMap = new HashMap<String, Object>();
        // cocoon.request
        Request request = ObjectModelHelper.getRequest(objectModel);
        cocoonMap.put("request", request);
        // cocoon.session
        HttpSession session = request.getSession(true);
        cocoonMap.put("session", session);
        User user = (User) session.getAttribute(LanewebConstants.USER);
        if (null == user) {
            user = new User();
            session.setAttribute(LanewebConstants.USER, user);
        }
        this.userDao.getUserData(user, request);
        cocoonMap.put("proxyLinks", this.proxyLinks.proxyLinks(user, request));
        // cocoon.context
        org.apache.cocoon.environment.Context context = ObjectModelHelper.getContext(objectModel);
        cocoonMap.put("context", context);
        cocoonMap.put("jndi", this.jndiData);
        String query = request.getParameter("q");
        if (null != query) {
            try {
                cocoonMap.put("urlencoded-query", URLEncoder.encode(query, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (LanewebConstants.LANE_COOKIE_NAME.equals(cookie.getName())) {
                    cocoonMap.put("user-cookie", cookie.getValue());
                    break;
                }
            }
        }
        cocoonMap.put("template", this.templateChooser.chooseTemplate(request));
        return cocoonMap;
    }
}

package edu.stanford.irt.laneweb.webdash;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.directory.LDAPPerson;
import edu.stanford.irt.laneweb.UserInfo;
import edu.stanford.irt.laneweb.UserInfoHelper;

public class WebdashAction extends AbstractLogEnabled implements Action,
		Serviceable, ThreadSafe {

	private static final String REGISTRATION_URL = "https://webda.sh/auth/init_post?";

	private static final String LOGIN_URL = "https://webda.sh/auth/auth_post?";

	private static final String ERROR_URL = "/webdashError.html";

	private static final String RESULT_KEY = "webdash-url";

	private UserInfoHelper userInfoHelper;

	private WebdashLogin webDashLogin;

	public void setWebdashLogin(final WebdashLogin webdashLogin) {
		if (null == webdashLogin) {
			throw new IllegalArgumentException("null webdashLogin");
		}
		this.webDashLogin = webdashLogin;
	}

	public void setUserInfoHelper(final UserInfoHelper userInfoHelper) {
		if (null == userInfoHelper) {
			throw new IllegalArgumentException("null userInfoHelper");
		}
		this.userInfoHelper = userInfoHelper;
	}

	public Map act(final Redirector redirector,
			final SourceResolver sourceResolver, final Map objectModel,
			final String string, final Parameters param) {

		Request request = ObjectModelHelper.getRequest(objectModel);

		String nonce = request.getParameter("nonce");
		String systemUserId = request.getParameter("system_user_id");

		UserInfo userInfo = this.userInfoHelper.getUserInfo(request);
		LDAPPerson person = userInfo.getLdapPerson();
		Map<String, String> result = new HashMap<String, String>(1);
		StringBuffer url = new StringBuffer();
		url.append(systemUserId == null ? REGISTRATION_URL : LOGIN_URL);
		try {
			url.append(this.webDashLogin.getQueryString(person, nonce));
			result.put(RESULT_KEY, url.toString());
		} catch (IllegalArgumentException e) {
			getLogger().error(e.getMessage(), e);
			result.put(RESULT_KEY, ERROR_URL);
		}
		return result;
	}

	public void service(final ServiceManager manager) throws ServiceException {
		this.webDashLogin = (WebdashLogin) manager.lookup(WebdashLogin.ROLE);
		this.userInfoHelper = (UserInfoHelper) manager
				.lookup(UserInfoHelper.ROLE);

	}

}

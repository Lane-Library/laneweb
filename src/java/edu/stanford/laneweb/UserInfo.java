package edu.stanford.laneweb;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.cocoon.environment.Context;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

public class UserInfo {
	
	private Boolean proxyLinks;
	
	private String sunetId;
	
	private Affiliation affiliation;
	
	private Ticket ticket;
	
	public void update(final Map objectModel) {
		if (objectModel == null) {
			throw new IllegalArgumentException("null objectModel");
		}
		Request request = ObjectModelHelper.getRequest(objectModel);
		if (this.affiliation == null) {
			String ip = request.getRemoteAddr();
			// mod_proxy puts the real remote address in an x-forwarded-for
			// header
			// Load balancer also does this
			String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
			if (header != null) {
				ip = header;
			}
			try {
				this.affiliation = Affiliation.getAffiliationForIP(ip);
			} catch (Exception e) {
				this.affiliation = Affiliation.ERR;
			}
		}
		if (this.sunetId == null) {
			String requestSunetId = (String) request.getAttribute(LanewebConstants.WEBAUTH_USER);
			if (!LanewebConstants.UNSET.equals(requestSunetId)) {
				this.sunetId = requestSunetId;
			}
		}
		if (null != request.getParameter(LanewebConstants.PROXY_LINKS)) {
			this.proxyLinks = new Boolean(request.getParameter(LanewebConstants.PROXY_LINKS));
		}
		if (this.sunetId != null) {
			Context context = ObjectModelHelper.getContext(objectModel);
			String key = (String) context.getInitParameter(LanewebConstants.EZPROXY_KEY);
			this.ticket = new Ticket(key, this.sunetId);
		}
	}

	public Affiliation getAffiliation() {
		return this.affiliation;
	}

	public Boolean getProxyLinks() {
		return this.proxyLinks;
	}

	public String getSunetId() {
		return this.sunetId;
	}
	
	public Ticket getTicket() {
		return this.ticket;
	}

}

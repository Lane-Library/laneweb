/*
 * Created on Aug 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.laneweb;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ceyates
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class LanewebHTTPSFilter implements Filter {

	public void init(FilterConfig arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *           javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String queryString = req.getQueryString();
		String url = queryString == null ? req.getRequestURL().toString() : req
				.getRequestURL().append('?').append(queryString).toString();
		int colonIndex = url.indexOf(':');
		String userName = req.getRemoteUser();
		req.getSession().setAttribute("USER_NAME", userName);
		//if (req.getRequestURI().indexOf("/secure") > -1) {
			if (req.getHeader("gohttps") != null
					|| req.getScheme().equals("https")) {
				chain.doFilter(request, response);
			} else {
				resp.sendRedirect("https" + url.substring(colonIndex));
			}
//		} else if (req.getHeader("gohttps") != null
//				|| req.getScheme().equals("https")) {
//			resp.sendRedirect("http" + url.substring(colonIndex));
//		} else {
//			chain.doFilter(request, response);
//		}
	}

	public void destroy() {
	}

}
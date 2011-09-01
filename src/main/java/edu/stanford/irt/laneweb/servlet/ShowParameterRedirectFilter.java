package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter checks for a show parameter and redirects to a url without one as
 * well as removing redundant proxy-link parameters from those requests. It also
 * removes parameters that have names starting with amp;.
 * 
 * @author ceyates
 */
public class ShowParameterRedirectFilter extends AbstractLanewebFilter {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {
        String queryString = request.getQueryString();
        if (queryString != null && queryString.indexOf("show=") > -1) {
            StringBuffer sb = request.getRequestURL();
            boolean addedParameter = false;
            for (Iterator it = request.getParameterMap().entrySet().iterator(); it.hasNext();) {
                Entry<String, String[]> entry = (Entry<String, String[]>) it.next();
                String name = entry.getKey();
                if (!"show".equals(name) && name.indexOf("amp;") != 0) {
                    sb.append(addedParameter ? '&' : '?').append(name).append('=').append(entry.getValue()[0]);
                    addedParameter = true;
                }
            }
            response.setHeader("Location", sb.toString());
            response.setStatus(301);
            return;
        }
        chain.doFilter(request, response);
    }
}

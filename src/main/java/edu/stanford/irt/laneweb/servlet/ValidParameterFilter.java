package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a Filter that responds with a 400 response if there is an invalid parameter present. It was created to stop
 * robots from making multiple requests for expensive resources with various bogus parameters.
 */
public class ValidParameterFilter extends AbstractLanewebFilter {

    private static class ParameterNameValidator implements Validator<Enumeration<String>> {

        private Set<String> validParameters;

        private ParameterNameValidator() {
            this.validParameters = new HashSet<String>();
            this.validParameters.add("a");
            this.validParameters.add("page");
            this.validParameters.add("m");
            this.validParameters.add("proxy-links");
            this.validParameters.add("sourceid");
            this.validParameters.add("r");
            this.validParameters.add("bn");
            this.validParameters.add("t");
            this.validParameters.add("pageNumber");
            this.validParameters.add("page-number");
            this.validParameters.add("q");
            this.validParameters.add("laneNav");
            this.validParameters.add("template");
            this.validParameters.add("site_preference");
            this.validParameters.add("source");
            // next three get put into google search results
            this.validParameters.add("ved");
            this.validParameters.add("sa");
            this.validParameters.add("usg");
        }

        @Override
        public Validity isValid(final Enumeration<String> names) {
            Validity validity = Validity.VALID;
            if (names != null) {
                for (String name : Collections.list(names)) {
                    if (!this.validParameters.contains(name)) {
                        validity = new Validity(false, name + " parameter not accepted");
                        break;
                    }
                }
            }
            return validity;
        }
    }

    @FunctionalInterface
    private static interface Validator<T> {

        Validity isValid(T object);
    }

    private static class Validity {

        public static final Validity VALID = new Validity(true, "");

        private boolean isValid;

        private String reason;

        private Validity(final boolean isValid, final String reason) {
            this.isValid = isValid;
            this.reason = reason;
        }

        public String getReason() {
            return this.reason;
        }

        public boolean isValid() {
            return this.isValid;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(ValidParameterFilter.class);

    private Validator<Enumeration<String>> validator;

    @Override
    public void init(final FilterConfig filterConfig) {
        this.validator = new ParameterNameValidator();
    }

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        Validity validity = this.validator.isValid(request.getParameterNames());
        if (validity.isValid()) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
            StringBuilder sb = new StringBuilder(request.getServletPath()).append('?').append(request.getQueryString())
                    .append(' ').append(validity.getReason());
            LOG.warn(sb.toString());
            chain.doFilter(new HttpServletRequestWrapper(request) {

                @Override
                public String getServletPath() {
                    return "/error.html";
                }
            }, response);
        }
    }
}

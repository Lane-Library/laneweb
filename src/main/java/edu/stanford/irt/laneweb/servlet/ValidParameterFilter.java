package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a Filter that responds with a 400 response if there is an invalid parameter present. It was created to stop
 * robots from making multiple requests for expensive resources with various bogus parameters.
 */
@WebFilter({ "/biomed-resources/*", "/search.html" })
public class ValidParameterFilter extends AbstractLanewebFilter {

    private static class ParameterMapEntryValidator implements Validator<Entry<String, String[]>> {

        private Map<String, Validator<String>> parameterValidators;

        private ParameterMapEntryValidator() {
            Validator<String> valid = (final String value) -> Validity.VALID;
            this.parameterValidators = new HashMap<>();
            this.parameterValidators.put("a", new ParameterValueValidator("a", Pattern.compile("^([a-z#]|all)$")));
            this.parameterValidators.put("page", new ParameterValueValidator("page", Pattern.compile("^(\\d+|all)$")));
            this.parameterValidators.put("m", new ParameterValueValidator("m", Pattern.compile("^[\\w '\\-/,]*$")));
            this.parameterValidators.put("proxy-links",
                    new ParameterValueValidator("proxy-links", Pattern.compile("^(true|false)$")));
            this.parameterValidators.put("sourceid",
                    new ParameterValueValidator("sourceid", Pattern.compile("^[\\w.\\-]+$")));
            this.parameterValidators.put("r", valid);
            this.parameterValidators.put("bn", valid);
            this.parameterValidators.put("t", valid);
            this.parameterValidators.put("q", valid);
            this.parameterValidators.put("laneNav", valid);
            this.parameterValidators.put("template", valid);
            this.parameterValidators.put("source", valid);
            this.parameterValidators.put("id", new ParameterValueValidator("id",
                    Pattern.compile("^(\\d+|[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})$")));
            // search.html
            this.parameterValidators.put("facet", valid);
            this.parameterValidators.put("facets",
                    new ParameterValueValidator("facets", Pattern.compile("^[a-z].+[\"\\]]$")));
            this.parameterValidators.put("laneSpellCorrected", valid);
            this.parameterValidators.put("sort",
                    new ParameterValueValidator("sort", Pattern.compile("((title_sort|date|year) (asc|desc),?)+$")));
            this.parameterValidators.put("track", valid);
            this.parameterValidators.put("p", valid);
            this.parameterValidators.put("i", valid);
            this.parameterValidators.put("c", valid);
            this.parameterValidators.put("o", valid);
            // next three get put into google search results
            this.parameterValidators.put("ved", valid);
            this.parameterValidators.put("sa", valid);
            this.parameterValidators.put("usg", valid);
        }

        @Override
        public Validity isValid(final Entry<String, String[]> entry) {
            String name = entry.getKey();
            if (entry.getValue().length != 1) {
                return new Validity(false, name + " has multiple values");
            } else if (!this.parameterValidators.containsKey(entry.getKey())) {
                return new Validity(false, name + " parameter not allowed");
            }
            return this.parameterValidators.get(name).isValid(entry.getValue()[0]);
        }
    }

    private static class ParameterNameValidator implements Validator<Map<String, String[]>> {

        private ParameterMapEntryValidator validator = new ParameterMapEntryValidator();

        @Override
        public Validity isValid(final Map<String, String[]> parameterMap) {
            Validity validity = Validity.VALID;
            if (parameterMap != null) {
                for (Entry<String, String[]> entry : parameterMap.entrySet()) {
                    validity = this.validator.isValid(entry);
                    if (!validity.isValid()) {
                        break;
                    }
                }
            }
            return validity;
        }
    }

    private static class ParameterValueValidator implements Validator<String> {

        private String name;

        private Pattern pattern;

        private ParameterValueValidator(final String name, final Pattern pattern) {
            this.name = name;
            this.pattern = pattern;
        }

        @Override
        public Validity isValid(final String value) {
            if (this.pattern.matcher(value).matches()) {
                return Validity.VALID;
            }
            return new Validity(false, value + " is not a valid value for " + this.name);
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

    private static final Logger log = LoggerFactory.getLogger(ValidParameterFilter.class);

    private Validator<Map<String, String[]>> validator;

    public ValidParameterFilter() {
        this.validator = new ParameterNameValidator();
    }

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        Validity validity = this.validator.isValid(request.getParameterMap());
        if (validity.isValid()) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
            if (log.isWarnEnabled()) {
                StringBuilder sb = new StringBuilder(request.getServletPath()).append('?')
                        .append(request.getQueryString()).append(' ').append(validity.getReason());
                log.warn(sb.toString());
            }
            chain.doFilter(new HttpServletRequestWrapper(request) {

                @Override
                public String getServletPath() {
                    return "/error.html";
                }
            }, response);
        }
    }
}

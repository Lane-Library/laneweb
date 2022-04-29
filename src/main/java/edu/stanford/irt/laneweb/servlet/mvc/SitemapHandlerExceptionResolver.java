package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import edu.stanford.irt.laneweb.BrowzineException;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;

public class SitemapHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger("error handler");

    private Set<Class<? extends Throwable>> noStackTraceThrowables;

    private SitemapController sitemapController;

    public SitemapHandlerExceptionResolver(final SitemapController sitemapController) {
        this.sitemapController = sitemapController;
        this.noStackTraceThrowables = new HashSet<>();
        this.noStackTraceThrowables.add(ResourceNotFoundException.class);
        this.noStackTraceThrowables.add(ClientAbortException.class);
        this.noStackTraceThrowables.add(URISyntaxException.class);
    }

    @Override
    public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler, final Exception ex) {
        logException(ex);
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            HttpServletRequest wrapped = new HttpServletRequestWrapper(request) {

                @Override
                public String getServletPath() {
                    return "/error.html";
                }

                @Override
                public String getMethod() {
                    return "GET";
                }
            };
            try {
                this.sitemapController.handleRequest(wrapped, response);
            } catch (IOException | LanewebException e) {
                log.error("Exception while handling exception", e);
            }
        }
        return new ModelAndView();
    }

    private void logException(final Throwable throwable) {
        if (BrowzineException.class.equals(throwable.getClass())) {
            log.info("{} {}", throwable.getClass().getName(), throwable.getMessage());
        } else if (this.noStackTraceThrowables.contains(throwable.getClass())) {
            if (log.isWarnEnabled()) {
                log.warn(throwable.toString());
            }
        } else {
            Throwable cause = throwable.getCause();
            if (cause == null) {
                log.error(throwable.toString(), throwable);
            } else {
                logException(cause);
            }
        }
    }
}

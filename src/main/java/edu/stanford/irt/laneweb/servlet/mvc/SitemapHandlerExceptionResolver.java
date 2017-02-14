package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;

public class SitemapHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger("error handler");

    private SitemapController sitemapController;

    public SitemapHandlerExceptionResolver(final SitemapController sitemapController) {
        this.sitemapController = sitemapController;
    }

    private static void logException(final Throwable ex) {
        if (ex instanceof ResourceNotFoundException || ex instanceof FileNotFoundException
                || ex instanceof ClientAbortException) {
            if (log.isWarnEnabled()) {
                log.warn(ex.toString());
            }
        } else {
            Throwable cause = ex.getCause();
            if (cause == null) {
                log.error(ex.toString(), ex);
            } else {
                logException(cause);
            }
        }
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
            };
            try {
                this.sitemapController.handleRequest(wrapped, response);
            } catch (IOException | LanewebException e) {
                log.error("Exception while handling exception", e);
            }
        }
        return new ModelAndView();
    }
}

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

    private static final Logger LOG = LoggerFactory.getLogger("error handler");

    private SitemapController sitemapController;

    public SitemapHandlerExceptionResolver(final SitemapController sitemapController) {
        this.sitemapController = sitemapController;
    }

    @Override
    public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler, final Exception ex) {
        if (ex instanceof ResourceNotFoundException) {
            LOG.warn(ex.toString());
        } else {
            Throwable cause = ex.getCause();
            Throwable reportableCause = ex;
            while (cause != null && !(reportableCause instanceof ClientAbortException)) {
                reportableCause = cause;
                cause = cause.getCause();
            }
            if (reportableCause instanceof FileNotFoundException || reportableCause instanceof ClientAbortException) {
                LOG.warn(reportableCause.toString());
            } else {
                LOG.error(ex.toString(), reportableCause);
            }
        }
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
                LOG.error("Exception while handling exception", e);
            }
        }
        return new ModelAndView();
    }
}

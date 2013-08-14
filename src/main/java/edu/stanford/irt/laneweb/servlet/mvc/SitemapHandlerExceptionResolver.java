package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;

public abstract class SitemapHandlerExceptionResolver extends SitemapRequestHandler implements HandlerExceptionResolver {

    private final Logger log;

    public SitemapHandlerExceptionResolver(final ComponentFactory componentFactory,
            final SourceResolver sourceResolver, final Logger log) {
        super(componentFactory, sourceResolver);
        this.log = log;
    }

    public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler, final Exception ex) {
        if (ex instanceof ResourceNotFoundException) {
            this.log.warn(ex.toString());
        } else {
            Throwable cause = ex.getCause();
            Throwable reportableCause = ex;
            while (cause != null && !(reportableCause instanceof ClientAbortException)) {
                reportableCause = cause;
                cause = cause.getCause();
            }
            if (reportableCause instanceof FileNotFoundException) {
                this.log.warn(reportableCause.toString());
            } else if (reportableCause instanceof ClientAbortException) {
                this.log.warn(reportableCause.toString() + " ip=" + request.getRemoteAddr() + " url="
                        + request.getRequestURL().toString());
            } else {
                this.log.error(ex.toString(), reportableCause);
            }
        }
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                handleRequest(request, response);
            } catch (ServletException | IOException | LanewebException e) {
                this.log.error("Exception while handling exception", e);
            }
        }
        return new ModelAndView();
    }

    @Override
    protected String getSitemapURI(final HttpServletRequest request) {
        return "/error.html";
    }
}

package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

// TODO: simplify testing of this by injecting a RequestHandler instead of extending one
public class SitemapHandlerExceptionResolver extends SitemapRequestHandler implements HandlerExceptionResolver {

    private static final Logger LOG = LoggerFactory.getLogger("error handler");

    public SitemapHandlerExceptionResolver(final ComponentFactory componentFactory, final DataBinder dataBinder,
            final Set<String> methodsNotAllowed, final String prefix, final ServletContext servletContext,
            final Sitemap sitemap, final SourceResolver sourceResolver) {
        super(componentFactory, dataBinder, methodsNotAllowed, prefix, servletContext, sitemap, sourceResolver);
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
            if (reportableCause instanceof FileNotFoundException) {
                LOG.warn(reportableCause.toString());
            } else if (reportableCause instanceof ClientAbortException) {
                LOG.warn(reportableCause.toString() + " ip=" + request.getRemoteAddr() + " url="
                        + request.getRequestURL().toString());
            } else {
                LOG.error(ex.toString(), reportableCause);
            }
        }
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                handleRequest(request, response);
            } catch (IOException | LanewebException e) {
                LOG.error("Exception while handling exception", e);
            }
        }
        return new ModelAndView();
    }

    @Override
    protected String getSitemapURI(final HttpServletRequest request) {
        return "/error.html";
    }
}

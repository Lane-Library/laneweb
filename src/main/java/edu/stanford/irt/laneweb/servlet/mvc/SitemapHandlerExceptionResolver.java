package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;

public abstract class SitemapHandlerExceptionResolver extends SitemapRequestHandler implements HandlerExceptionResolver {

    public SitemapHandlerExceptionResolver(ComponentFactory componentFactory, SourceResolver sourceResolver) {
        super(componentFactory, sourceResolver);
    }

    private final Logger log = LoggerFactory.getLogger("error handler");

    public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler, final Exception ex) {
        if (ex instanceof ResourceNotFoundException) {
            this.log.error(ex.toString());
        } else {
            Throwable maybeNull = ex.getCause();
            Throwable ultimateCause = ex;
            while (maybeNull != null) {
                ultimateCause = maybeNull;
                maybeNull = maybeNull.getCause();
            }
            if (ultimateCause instanceof FileNotFoundException) {
                this.log.error(ultimateCause.toString());
            } else if (ultimateCause instanceof SocketException && "Broken pipe".equals(ultimateCause.getMessage())) {
                this.log.error(ultimateCause.toString() + " ip=" + request.getRemoteAddr() + " url=" + request.getRequestURL().toString());
            } else {
                this.log.error(ex.toString(), ultimateCause);
            }
        }
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                handleRequest(request, response);
            } catch (ServletException e) {
                logMessage(e);
            } catch (IOException e) {
                logMessage(e);
            } catch (LanewebException e) {
                logMessage(e);
            }
        }
        return new ModelAndView();
    }

    @Override
    protected String getSitemapURI(final HttpServletRequest request) {
        return "/error.html";
    }

    private void logMessage(final Exception e) {
        this.log.error("Exception while handling exception: " + e.toString());
    }
}

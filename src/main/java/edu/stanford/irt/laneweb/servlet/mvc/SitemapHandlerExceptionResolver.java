package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;

public abstract class SitemapHandlerExceptionResolver extends SitemapRequestHandler implements HandlerExceptionResolver {

    private final Logger log = LoggerFactory.getLogger(SitemapHandlerExceptionResolver.class);

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
            } else {
                this.log.error(ex.getMessage(), ex);
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        try {
            handleRequest(request, response);
        } catch (ServletException e) {
            throw new LanewebException(e);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        return new ModelAndView();
    }

    @Override
    protected String getSitemapURI(final HttpServletRequest request) {
        return "/error.html";
    }
}

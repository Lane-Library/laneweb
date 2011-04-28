package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public abstract class SitemapHandlerExceptionResolver extends SitemapRequestHandler implements HandlerExceptionResolver {

    //TODO: log the exception
    public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler, final Exception ex) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        try {
            handleRequest(request, response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView();
    }

    @Override
    protected String getSitemapURI(HttpServletRequest request) {
        return "/error.html";
    }
}

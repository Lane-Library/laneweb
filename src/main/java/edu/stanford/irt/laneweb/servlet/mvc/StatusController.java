package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.cocoon.sitemap.SitemapException;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.suggest.SuggestionManager;

@Controller
public class StatusController {

    private AbstractSitemapController sitemapController;

    private SuggestionManager suggestionManager;

    private String version;

    @Autowired
    public StatusController(final SitemapController sitemapController,
            @Qualifier("edu.stanford.irt.suggest.SuggestionManager/eresource") final SuggestionManager suggestionManager,
            final ServletContext servletContext) {
        this.sitemapController = sitemapController;
        this.suggestionManager = suggestionManager;
        this.version = new StringBuilder("laneweb-").append(servletContext.getInitParameter("laneweb.context.version"))
                .append('\n').toString();
    }

    @RequestMapping(value = "/status.txt", produces = "text/plain; charset=utf-8")
    @ResponseBody
    public String getStatus(final HttpServletRequest request, final HttpServletResponse response) {
        StringBuilder sb = new StringBuilder(this.version);
        getSuggestionStatus(sb);
        sb.append('\n');
        getIndexStatus(sb, request, response);
        return sb.toString();
    }

    private void getIndexStatus(final StringBuilder sb, final HttpServletRequest request,
            final HttpServletResponse response) {
        long time = System.currentTimeMillis();
        try {
            HttpServletRequest req = new HttpServletRequestWrapper(request) {

                @Override
                public String getServletPath() {
                    return "index.html";
                }
            };
            HttpServletResponse resp = new HttpServletResponseWrapper(response) {

                @Override
                public ServletOutputStream getOutputStream() {
                    return new ServletOutputStream() {

                        @Override
                        public void write(final int b) {
                            // do nothing
                        }
                    };
                }

                @Override
                public void setContentType(final String type) {
                    // do nothing
                }
            };
            this.sitemapController.handleRequest(req, resp);
            time = System.currentTimeMillis() - time;
            sb.append('[').append(time < 250 ? "OK" : "WARN").append("] index.html took ").append(time).append("ms.");
        } catch (IOException | SitemapException e) {
            sb.append("[ERROR] index.html failed: ").append(e);
        }
    }

    private void getSuggestionStatus(final StringBuilder sb) {
        long time = System.currentTimeMillis();
        try {
            this.suggestionManager.getSuggestionsForTerm("cardio");
            time = System.currentTimeMillis() - time;
            sb.append('[').append(time < 250 ? "OK" : "WARN").append("] suggestions took ").append(time).append("ms.");
        } catch (LanewebException e) {
            sb.append("[ERROR] suggestions failed: ").append(e);
        }
    }
}

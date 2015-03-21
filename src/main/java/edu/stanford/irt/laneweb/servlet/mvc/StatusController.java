package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
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

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.SitemapException;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.suggest.SuggestionManager;

@Controller
public class StatusController {

    private SitemapRequestHandler requestHandler;

    private SuggestionManager suggestionManager;

    @Autowired
    public StatusController(
            @Qualifier("edu.stanford.irt.laneweb.servlet.mvc.SitemapRequestHandler/sitemap") final SitemapRequestHandler requestHandler,
            final ComponentFactory componentFactory, final SourceResolver sourceResolver,
            @Qualifier("edu.stanford.irt.suggest.SuggestionManager/eresource") final SuggestionManager suggestionManager) {
        this.requestHandler = requestHandler;
        this.suggestionManager = suggestionManager;
    }

    @RequestMapping(value = "/status", produces = "text/plain; charset=utf-8")
    @ResponseBody
    public String getStatus(final HttpServletRequest request, final HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
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
                public ServletOutputStream getOutputStream() throws IOException {
                    return new ServletOutputStream() {

                        @Override
                        public void write(final int b) throws IOException {
                        }
                    };
                }

                @Override
                public void setContentType(final String type) {
                }
            };
            this.requestHandler.handleRequest(req, resp);
            time = System.currentTimeMillis() - time;
            sb.append('[').append(time < 250 ? "OK" : "WARN").append("] index.html took ").append(time).append("ms.");
        } catch (ServletException | IOException | SitemapException e) {
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

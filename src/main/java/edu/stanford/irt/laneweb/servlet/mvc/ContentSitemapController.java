package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

@Controller
public class ContentSitemapController extends AbstractSitemapController {

    public ContentSitemapController(final ComponentFactory componentFactory,
            @Qualifier("edu.stanford.irt.laneweb.servlet.binding.DataBinder") final DataBinder dataBinder,
            @Qualifier("edu.stanford.irt.cocoon.sitemap.Sitemap/content") final Sitemap sitemap,
            final SourceResolver sourceResolver) {
        super(componentFactory, dataBinder, sitemap, sourceResolver);
    }

    @Override
    @RequestMapping(value = {"/content/**/*.html"}, method = { RequestMethod.GET, RequestMethod.HEAD })
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        doHandleRequest(request, response, "/content");
    }
}

package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CachedResponse;
import org.apache.cocoon.caching.CachingOutputStream;
import org.apache.excalibur.source.SourceValidity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.WebContentGenerator;

/**
 * This class mimics the yui combo server
 */
// TODO: maybe proxy the real combo server for gallery content
@Controller
public class YUIComboController extends WebContentGenerator implements ResourceLoaderAware {

    private static final SourceValidity[] EMPTY_VALIDITY = new SourceValidity[0];

    @Autowired
    private Cache cache;

    /** where to start looking for the yui build files */
    private Resource yuiBase;

    /**
     * Reads the request string and parses out the file names, sending each one
     * to the output.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws ProcessingException
     */
    @RequestMapping(value = "/yui")
    public void getCombo(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException, ProcessingException {
        checkAndPrepare(request, response, true);
        OutputStream output = response.getOutputStream();
        String queryString = request.getQueryString();
        CachedResponse cachedResponse = this.cache.get(queryString);
        response.addHeader("Content-Type", "text/javascript");
        if (cachedResponse == null) {
            CachingOutputStream cachingOutput = new CachingOutputStream(output);
            StringTokenizer names = new StringTokenizer(queryString, "&");
            try {
                while (names.hasMoreTokens()) {
                    Resource resource = this.yuiBase.createRelative(names.nextToken());
                    copy(resource.getInputStream(), cachingOutput);
                }
                cachingOutput.flush();
            } finally {
                cachingOutput.close();
            }
            this.cache.store(queryString, new CachedResponse(EMPTY_VALIDITY, cachingOutput.getContent()));
        } else {
            output.write(cachedResponse.getResponse());
            output.flush();
            output.close();
        }
    }

    /**
     * Get the ResoureLoader. There may be a better way to get the yuiBase
     * value, but I haven't thought of it yet.
     */
    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.yuiBase = resourceLoader.getResource("/resources/javascript/yui/");
    }

    /**
     * copy input to output, keeping output open but closing input
     * 
     * @param input
     * @param output
     * @throws IOException
     */
    private void copy(final InputStream input, final OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.write('\n');
        } finally {
            input.close();
        }
    }
}

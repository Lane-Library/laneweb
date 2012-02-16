package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.WebContentGenerator;

/**
 * This class mimicks the yui combo server
 */
@Controller
public class YUIComboController extends WebContentGenerator implements ResourceLoaderAware {

    /** where to start looking for the yui build files */
    private Resource yuiBase;

    /**
     * Reads the request string and parses out the file names, sending each one to the output.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    //TODO: cache the combined result for future requests.
    @RequestMapping(value = "/yui")
    public void getCombo(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        checkAndPrepare(request, response, true);
        OutputStream output = response.getOutputStream();
        StringTokenizer names = new StringTokenizer(request.getQueryString(), "&");
        try {
            while (names.hasMoreTokens()) {
                Resource resource = this.yuiBase.createRelative(names.nextToken());
                copy(resource.getInputStream(), output);
            }
            output.flush();
        } finally {
            output.close();
        }
    }

    /**
     * Get the ResoureLoader.  There may be a better way to get the yuiBase value, but I haven't
     * thought of it yet.
     */
    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.yuiBase = resourceLoader.getResource("/resources/javascript/yui/");
    }

    /**
     * copy input to output, keeping output open but closing input
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

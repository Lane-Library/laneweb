package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CachedResponse;
import org.apache.excalibur.source.SourceValidity;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public abstract class BasePathSubstitutingRequestHandler extends ResourceHttpRequestHandler {

    private static final String SUBSTITUTE = "/./.";

    private static final Pattern PATTERN = Pattern.compile(SUBSTITUTE, Pattern.LITERAL);

    private Cache cache;

    private DataBinder dataBinder;

    @Override
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        this.dataBinder.bind(getModel(), request);
        super.handleRequest(request, response);
    }
    
    public void setCache(final Cache cache) {
        this.cache = cache;
    }

    public void setDataBinder(final DataBinder dataBinder) {
        this.dataBinder = dataBinder;
    }

    protected abstract Map<String, Object> getModel();

    @Override
    protected void setHeaders(final HttpServletResponse response, final Resource resource, final MediaType mediaType)
            throws IOException {
        response.setContentType(mediaType.toString());
    }

    @Override
    protected void writeContent(final HttpServletResponse response, final Resource resource) throws IOException {
        InputStream input = null;
        PrintWriter writer = null;
        OutputStream out = null;
        String basePath = ModelUtil.getString(getModel(), Model.BASE_PATH);
        String key = resource.getURI().toString() + '?' + basePath;
        CachedResponse cachedResponse = this.cache.get(key);
        long resourceLastModified = resource.lastModified();
        boolean usingCache = false;
        if (cachedResponse != null && resourceLastModified < cachedResponse.getLastModified()) {
            usingCache = true;
        }
        try {
            if (usingCache) {
                out = response.getOutputStream();
                input = new ByteArrayInputStream(cachedResponse.getResponse());
                byte[] buffer = new byte[1024];
                int i;
                while (true) {
                    i = input.read(buffer);
                    if (i == -1) {
                        break;
                    }
                    out.write(buffer, 0, i);
                }
            } else {
                input = resource.getInputStream();
                writer = response.getWriter();
                StringWriter cacheWriter = new StringWriter();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (line.indexOf(SUBSTITUTE) > -1) {
                        line = PATTERN.matcher(line).replaceAll(basePath);
                    }
                    writer.println(line);
                    cacheWriter.write(line);
                    cacheWriter.write('\n');
                }
                cachedResponse = new CachedResponse((SourceValidity) null, cacheWriter.toString().getBytes());
                try {
                    this.cache.store(key, cachedResponse);
                } catch (ProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            if (input != null) {
                input.close();
            }
            if (writer != null) {
            writer.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}

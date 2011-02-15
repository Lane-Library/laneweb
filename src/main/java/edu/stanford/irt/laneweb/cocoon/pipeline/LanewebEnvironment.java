package edu.stanford.irt.laneweb.cocoon.pipeline;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.environment.Context;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.http.HttpEnvironment;

public class LanewebEnvironment implements Environment {
    
    private Map<String, Object> model;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private ServletContext servletContext;

    
    public void startingProcessing() {
    }

    
    public void finishingProcessing() {
    }

    
    public String getURI() {
        return null;
    }

    
    public String getURIPrefix() {
        return null;
//        throw new UnsupportedOperationException();
    }

    
    public void setURI(String prefix, String value) {
        throw new UnsupportedOperationException();
        
    }

    
    public String getView() {
        return null;
    }

    
    public String getAction() {
        return null;
    }

    
    public void redirect(String url, boolean global, boolean permanent) throws IOException {
        throw new UnsupportedOperationException();
        
    }

    
    public void setContentType(String mimeType) {
        this.response.setContentType(mimeType);
        
    }

    
    public String getContentType() {
        return this.servletContext.getMimeType(this.request.getRequestURI());
    }

    
    public void setContentLength(int length) {
        this.response.setContentLength(length);
    }

    
    public void setStatus(int statusCode) {
        this.response.setStatus(statusCode);
        
    }

    
    public OutputStream getOutputStream(int bufferSize) throws IOException {
        return this.response.getOutputStream();
    }

    
    public Map getObjectModel() {
        return this.model;
    }

    
    public boolean isResponseModified(long lastModified) {
        if (lastModified != 0) {
            long if_modified_since = this.request.getDateHeader("If-Modified-Since");
            this.response.setDateHeader("Last-Modified", lastModified);
            return (if_modified_since / 1000 < lastModified  / 1000);
        }
        return true;
    }

    
    public void setResponseIsNotModified() {
        this.response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
    }

    
    public void setAttribute(String name, Object value) {
        throw new UnsupportedOperationException();
        
    }

    
    public Object getAttribute(String name) {
        return null;
    }

    
    public void removeAttribute(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    
    public boolean tryResetResponse() throws IOException {
        try {
            this.response.reset();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    
    public void commitResponse() throws IOException {
    }

    
    public boolean isExternal() {
        return true;
    }

    
    public boolean isInternalRedirect() {
        throw new UnsupportedOperationException();
    }


    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
    
    public void setHttpServletResponse(HttpServletResponse response) {
        this.response = response;
    }
    
    public void setHttpServletRequest(HttpServletRequest request) {
        this.request  = request;
    }


    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}

package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.environment.Environment;

public class LanewebEnvironment implements Environment {

    private boolean isExternal;

    private Map<String, Object> model;

    private OutputStream outputStream;
    
    public LanewebEnvironment() {}

    public LanewebEnvironment(final Map<String, Object> model, final OutputStream outputStream, final boolean isExternal) {
        this.model = model;
        this.outputStream = outputStream;
        this.isExternal = isExternal;
    }
    
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
    
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    
    public void setIsExternal(boolean isExternal) {
        this.isExternal = isExternal;
    }

    public void commitResponse() throws IOException {
        throw new UnsupportedOperationException();
    }

    public void finishingProcessing() {
        throw new UnsupportedOperationException();
    }

    public String getAction() {
        throw new UnsupportedOperationException();
    }

    public Object getAttribute(final String name) {
        return null;
    }

    @SuppressWarnings("rawtypes")
    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("rawtypes")
    public Map getObjectModel() {
        return this.model;
    }

    public OutputStream getOutputStream(final int bufferSize) throws IOException {
        return this.outputStream;
    }

    public String getURI() {
        return null;
    }

    public String getURIPrefix() {
        return null;
    }

    public String getView() {
        return null;
    }

    public boolean isExternal() {
        return this.isExternal;
    }

    public boolean isInternalRedirect() {
//        throw new UnsupportedOperationException();
        return false;
    }

    public boolean isResponseModified(final long lastModified) {
        throw new UnsupportedOperationException();
    }

    public void redirect(final String url, final boolean global, final boolean permanent) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void removeAttribute(final String name) {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    public void setContentLength(final int length) {
        throw new UnsupportedOperationException();
    }

    public void setContentType(final String mimeType) {
    }

    public void setHttpServletRequest(final HttpServletRequest request) {
        throw new UnsupportedOperationException();
    }

    public void setHttpServletResponse(final HttpServletResponse response) {
        throw new UnsupportedOperationException();
    }

    public void setResponseIsNotModified() {
        throw new UnsupportedOperationException();
    }

    public void setServletContext(final ServletContext servletContext) {
        throw new UnsupportedOperationException();
    }

    public void setStatus(final int statusCode) {
    }

    public void setURI(final String prefix, final String uri) {
        throw new UnsupportedOperationException();
    }

    public void startingProcessing() {
        throw new UnsupportedOperationException();
    }

    public boolean tryResetResponse() throws IOException {
        throw new UnsupportedOperationException();
    }
}

package edu.stanford.irt.laneweb.cocoon.source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;

import org.apache.cocoon.environment.Environment;

public class LanewebSitemapSourceEnvironment implements Environment {

    private Map<Object, Object> model;

    private ByteArrayOutputStream outputStream;

    public LanewebSitemapSourceEnvironment(final Map<Object, Object> model) {
        this.model = model;
        this.outputStream = new ByteArrayOutputStream();
    }

    public void commitResponse() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(final Object obj) {
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
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean isExternal() {
        return false;
    }

    public boolean isInternalRedirect() {
        return false;
    }

    public boolean isResponseModified(final long lastModified) {
        return false;
    }

    public void redirect(final String newURL, final boolean global, final boolean permanent) throws IOException {
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

    public void setContentType(final String contentType) {
    }

    public void setResponseIsNotModified() {
        throw new UnsupportedOperationException();
    }

    public void setStatus(final int statusCode) {
    }

    public void setURI(final String prefix, final String value) {
        throw new UnsupportedOperationException();
    }

    public void startingProcessing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    public boolean tryResetResponse() throws IOException {
        throw new UnsupportedOperationException();
    }

    InputStream getInputStream() {
        return new ByteArrayInputStream(this.outputStream.toByteArray());
    }
}

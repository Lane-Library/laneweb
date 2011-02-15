package edu.stanford.irt.laneweb.cocoon.expression;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.environment.Response;


public class LanewebResponse implements Response {

    
    private HttpServletResponse response;


    public LanewebResponse(HttpServletResponse response) {
        this.response = response;
    }


    public String encodeRedirectURL(String url) {
        throw new UnsupportedOperationException();
        
    }

    
    public String encodeUrl(String url) {
        throw new UnsupportedOperationException();
        
    }

    
    public String encodeRedirectUrl(String url) {
        throw new UnsupportedOperationException();
        
    }

    
    public void sendError(int sc, String msg) throws IOException {
        throw new UnsupportedOperationException();
    }

    
    public void sendError(int sc) throws IOException {
        throw new UnsupportedOperationException();
    }

    
    public void sendRedirect(String location) throws IOException {
        throw new UnsupportedOperationException();
    }

    
    public void setStatus(int sc) {
        throw new UnsupportedOperationException();
    }

    
    public void setStatus(int sc, String sm) {
        throw new UnsupportedOperationException();
    }

    
    public String getContentType() {
        throw new UnsupportedOperationException();
        
    }

    
    public ServletOutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
        
    }

    
    public PrintWriter getWriter() throws IOException {
        throw new UnsupportedOperationException();
        
    }

    
    public void setCharacterEncoding(String charset) {
        throw new UnsupportedOperationException();
    }

    
    public void setContentLength(int len) {
        throw new UnsupportedOperationException();
    }

    
    public void setContentType(String type) {
        throw new UnsupportedOperationException();
    }

    
    public void setBufferSize(int size) {
        throw new UnsupportedOperationException();
    }

    
    public int getBufferSize() {
        throw new UnsupportedOperationException();
        
    }

    
    public void flushBuffer() throws IOException {
        throw new UnsupportedOperationException();
    }

    
    public void resetBuffer() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isCommitted() {
        throw new UnsupportedOperationException();
        
    }

    
    public void reset() {
        throw new UnsupportedOperationException();
    }

    
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
        
    }

    
    public void setLocale(Locale loc) {
        throw new UnsupportedOperationException();
    }

    
    public Locale getLocale() {
        throw new UnsupportedOperationException();
        
    }

    
    public Cookie createCookie(String name, String value) {
        throw new UnsupportedOperationException();
        
    }

    
    public org.apache.cocoon.environment.Cookie createCocoonCookie(String name, String value) {
        throw new UnsupportedOperationException();
        
    }

    
    public void addCookie(Cookie cookie) {
        throw new UnsupportedOperationException();
    }

    
    public void addCookie(org.apache.cocoon.environment.Cookie cookie) {
        throw new UnsupportedOperationException();
    }

    
    public boolean containsHeader(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public String encodeURL(String url) {
        throw new UnsupportedOperationException();
        
    }

    
    public void setDateHeader(String name, long date) {
        this.response.setDateHeader(name, date);
    }

    
    public void addDateHeader(String name, long date) {
        throw new UnsupportedOperationException();
    }

    
    public void setHeader(String name, String value) {
        this.response.setHeader(name, value);
    }

    
    public void addHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }

    
    public void setIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }

    
    public void addIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }
}

package edu.stanford.irt.laneweb.cocoon.expression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;


public class LanewebRequest implements Request {

    
    private String sitemapURI;
    
    private HttpServletRequest request;
    
    public LanewebRequest(String sitemapURI, HttpServletRequest request) {
        this.sitemapURI = sitemapURI;
        this.request = request;
    }


    public int getIntHeader(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException();
        
    }

    
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException();
        
    }

    
    public Map getParameterMap() {
        throw new UnsupportedOperationException();
        
    }

    
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException();
        
    }

    
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
        
    }

    
    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
        
    }

    
    public int getRemotePort() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getLocalName() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getLocalAddr() {
        throw new UnsupportedOperationException();
        
    }

    
    public int getLocalPort() {
        throw new UnsupportedOperationException();
        
    }

    
    public Object get(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public Object getAttribute(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public Object getLocalAttribute(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException();
        
    }

    
    public Enumeration getLocalAttributeNames() {
        throw new UnsupportedOperationException();
        
    }

    
    public void setAttribute(String name, Object o) {
        throw new UnsupportedOperationException();
    }

    
    public void setLocalAttribute(String name, Object o) {
        throw new UnsupportedOperationException();
    }

    
    public void removeAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    
    public void removeLocalAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    
    public Object searchAttribute(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public Map getAttributes() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getAuthType() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
        
    }

    
    public void setCharacterEncoding(String enc) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException();
    }

    
    public int getContentLength() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getContentType() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getParameter(String name) {
        return this.request.getParameter(name);
    }

    
    public Enumeration getParameterNames() {
        throw new UnsupportedOperationException();
        
    }

    
    public String[] getParameterValues(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public Map getParameters() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getProtocol() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getScheme() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getServerName() {
        throw new UnsupportedOperationException();
        
    }

    
    public int getServerPort() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getRemoteAddr() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getRemoteHost() {
        throw new UnsupportedOperationException();
        
    }

    
    public Locale getLocale() {
        throw new UnsupportedOperationException();
        
    }

    
    public Enumeration getLocales() {
        throw new UnsupportedOperationException();
        
    }

    
    public boolean isSecure() {
        throw new UnsupportedOperationException();
        
    }

    
    public Cookie[] getCookies() {
        throw new UnsupportedOperationException();
        
    }

    
    public Map getCookieMap() {
        throw new UnsupportedOperationException();
        
    }

    
    public org.apache.cocoon.environment.Cookie[] getCocoonCookies() {
        throw new UnsupportedOperationException();
        
    }

    
    public Map getCocoonCookieMap() {
        throw new UnsupportedOperationException();
        
    }

    
    public long getDateHeader(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public String getHeader(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public Enumeration getHeaders(String name) {
        throw new UnsupportedOperationException();
        
    }

    
    public Enumeration getHeaderNames() {
        throw new UnsupportedOperationException();
        
    }

    
    public Map getHeaders() {
        throw new UnsupportedOperationException();
        
    }

    
    public ServletInputStream getInputStream() throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
        
    }

    
    public String getMethod() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getPathInfo() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getPathTranslated() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getContextPath() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getQueryString() {
        return this.request.getQueryString();
        
    }

    
    public String getRemoteUser() {
        throw new UnsupportedOperationException();
        
    }

    
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException();
        
    }

    
    public boolean isUserInRole(String role) {
        throw new UnsupportedOperationException();
        
    }

    
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException();
        
    }

    
    public String getRequestURI() {
        return this.request.getRequestURI();
        
    }

    
    public String getSitemapURI() {
        return this.sitemapURI;
    }

    
    public String getSitemapURIPrefix() {
        throw new UnsupportedOperationException();
    }

    
    public String getSitemapPath() {
        throw new UnsupportedOperationException();
    }

    
    public String getServletPath() {
        throw new UnsupportedOperationException();
    }

    
    public HttpSession getSession(boolean create) {
        throw new UnsupportedOperationException();
    }

    
    public HttpSession getSession() {
        throw new UnsupportedOperationException();
    }

    
    public Session getCocoonSession(boolean create) {
        throw new UnsupportedOperationException();
    }

    
    public Session getCocoonSession() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException();
    }
}

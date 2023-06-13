package edu.stanford.irt.laneweb.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

public class BasicAuthRESTService {

  private RestOperations restOperations;

  private HttpEntity<HttpHeaders> httpEntity;
  
  
  
  public BasicAuthRESTService(final RestOperations restOperations, String userInfo) {
    this.restOperations = restOperations;
    String base64Creds = Base64.getEncoder().encodeToString(userInfo.getBytes());
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Basic " + base64Creds);
    this.httpEntity = new HttpEntity<>(headers);
  }

  public InputStream getInputStream(final URI uri) throws RESTException {
    try {
      return getObject(uri, Resource.class).getInputStream();
    } catch (IOException e) {
      throw new RESTException(e);
    }
  }

  public <T> T getObject(final URI uri, final Class<T> type) throws RESTException {
    try {
      return this.restOperations.exchange(uri, HttpMethod.GET, this.httpEntity, type).getBody();
    } catch (RestClientException e) {
      throw new RESTException(e);
    }
  }

  public <T> T getObject(final URI uri, final TypeReference<T> type) throws RESTException {
    try {
      return this.restOperations.exchange(uri, HttpMethod.GET, this.httpEntity, type).getBody();
    } catch (RestClientException e) {
      throw new RESTException(e);
    }
  }
  
  
}

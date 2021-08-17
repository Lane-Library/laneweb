package edu.stanford.irt.laneweb.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

public class RESTService {

  private RestOperations restOperations;

  public RESTService(final RestOperations restOperations) {
    this.restOperations = restOperations;
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
      return this.restOperations.getForObject(uri, type);
    } catch (RestClientException e) {
      throw new RESTException(e);
    }
  }

  public <T> T getObject(final URI uri, final TypeReference<T> type) throws RESTException {
    try {
      return this.restOperations.exchange(uri, HttpMethod.GET, null, type).getBody();
    } catch (RestClientException e) {
      throw new RESTException(e);
    }
  }

  public int postURLEncodedString(final URI uri, final String object) throws RESTException {
    RequestEntity<String> request = RequestEntity.post(uri).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(object);
    try {
      return this.restOperations.exchange(request, String.class).getStatusCodeValue();
    } catch (RestClientException e) {
      throw new RESTException(e);
    }
  }

  public <T> T postObject(final URI uri, final Object object, Class<T> responseType) throws RESTException {
    RequestEntity<Object> request = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON).body(object);
    try {
      return this.restOperations.exchange(request, responseType).getBody();
    } catch (RestClientException e) {
      throw new RESTException(e);
    }
  }

  public void putObject(final URI uri, final Object object) throws RESTException {
    try {
      this.restOperations.put(uri, object);
    } catch (RestClientException e) {
      throw new RESTException(e);
    }
  }
}

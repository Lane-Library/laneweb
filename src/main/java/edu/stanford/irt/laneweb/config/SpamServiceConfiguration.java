package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.spam.SpamService;
import edu.stanford.irt.laneweb.spam.SpamServiceImpl;

@Configuration
public class SpamServiceConfiguration {

  @Bean
  public SpamService spamService(final RESTService restService,
          @Value("${edu.stanford.irt.laneweb.spam.scheme}") final String scheme,
          @Value("${edu.stanford.irt.laneweb.spam.host}") final String host,
          @Value("${edu.stanford.irt.laneweb.spam.port}") final int port,
          @Value("${edu.stanford.irt.laneweb.spam.path}") final String path) throws URISyntaxException {
      URI metaSearchURI = new URI(scheme, null, host, port, path, null, null);
      return new SpamServiceImpl(metaSearchURI, restService);
  }

}

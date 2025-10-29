package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import edu.stanford.irt.laneweb.catalog.CatalogStatusService;
import edu.stanford.irt.laneweb.rest.Oauth.OauthRESTService;
import edu.stanford.irt.laneweb.rest.Oauth.OauthTokenService;
import edu.stanford.irt.status.StatusService;

@Configuration
public class CatalogServiceConfiguration {

    @Bean("java.net.URI/catalog-service")
    public URI catalogServiceURI(@Value("${edu.stanford.irt.laneweb.catalog-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.catalog-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.catalog-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.catalog-service.path}") final String path) throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }

    @Bean
    public StatusService catalogStatusService(@Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            @Qualifier("restService/catalog-service") final OauthRESTService restService) {
        return new CatalogStatusService(catalogServiceURI, restService);
    }

    @Bean("restService/catalog-service")
    public OauthRESTService getMetasearchOauthRestService(RestClient restClient,
            @Value("${edu.stanford.irt.laneweb.catalog-service.userInfo}") final String userInfo,
            @Qualifier("java.net.URI/oauth2-server") final URI oauth2Endpoint) throws URISyntaxException {
        URI tokenEndpoint = URI.create(oauth2Endpoint.toString() + "oauth2/token");
        OauthTokenService oauthTokenService = new OauthTokenService(restClient, tokenEndpoint, userInfo);
        return new OauthRESTService(restClient, oauthTokenService);
    }
}

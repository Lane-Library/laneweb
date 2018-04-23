package edu.stanford.irt.laneweb.cocoon;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.FactoryBean;

public class HttpClientFactoryBean implements FactoryBean<HttpClient> {

    private HttpClient httpClient;

    public HttpClientFactoryBean(int maxConnPerRoute, int maxConnTotal) {
        this.httpClient = HttpClientBuilder.create()
                .setMaxConnPerRoute(maxConnPerRoute)
                .setMaxConnTotal(maxConnTotal)
                .build();
    }

    @Override
    public HttpClient getObject() throws Exception {
        return this.httpClient;
    }

    @Override
    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

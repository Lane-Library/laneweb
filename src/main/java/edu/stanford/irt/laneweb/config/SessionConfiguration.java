package edu.stanford.irt.laneweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

@Configuration
public class SessionConfiguration extends RedisHttpSessionConfiguration {

    @Bean
    public JedisConnectionFactory connectionFactory(
            @Value("${edu.stanford.irt.laneweb.redis.host}") final String hostName) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(hostName);
        return factory;
    }

    @Override
    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RedisOperationsSessionRepository sessionRepository(
            final RedisOperations<Object, Object> sessionRedisTemplate,
            final ApplicationEventPublisher applicationEventPublisher) {
        return super.sessionRepository(sessionRedisTemplate, applicationEventPublisher);
    }
}

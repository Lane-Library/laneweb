package edu.stanford.irt.laneweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class SessionConfiguration {

    @Bean
    public LettuceConnectionFactory connectionFactory(
            @Value("${edu.stanford.irt.laneweb.redis.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.redis.port}") final int port) {
        return new LettuceConnectionFactory(host, port);
    }
}

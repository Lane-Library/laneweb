package edu.stanford.irt.laneweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
@Profile("gce")
@Import({ RedisAutoConfiguration.class })
public class GCESessionConfiguration {
 
    @Bean
    static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Primary
    @Bean
    LettuceConnectionFactory connectionFactory(
             @Value("${edu.stanford.irt.laneweb.redis.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.redis.port}") final int port) {
        return new LettuceConnectionFactory(host, port);
    }
}

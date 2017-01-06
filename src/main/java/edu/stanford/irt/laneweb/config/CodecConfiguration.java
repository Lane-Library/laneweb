package edu.stanford.irt.laneweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.codec.SHCCodec;

@Configuration
public class CodecConfiguration {

    @Bean
    public SHCCodec shcCodec(@Value("%{edu.stanford.irt.laneweb.shccodec.key}") final String key,
            @Value("%{edu.stanford.irt.laneweb.shccodec.vector}") final String vector) {
        return new SHCCodec(key, vector);
    }
}

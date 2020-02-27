package edu.stanford.irt.laneweb.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class MultipartResolverConfig {
    
    
    int MAX_UPLOAD_FILE =  13107200;

    @Bean
    public CommonsMultipartResolver multipartResolver() throws IOException {
        Resource tmp = new FileSystemResource("/tmp");
        CommonsMultipartResolver multipart = new CommonsMultipartResolver();
        multipart.setUploadTempDir(tmp);
        multipart.setMaxUploadSize(MAX_UPLOAD_FILE);
        return multipart;
    }
}

package edu.stanford.irt.laneweb.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.laneweb.flickr.FlickrPhotoGenerator;
import edu.stanford.irt.laneweb.flickr.FlickrPhotoList;
import edu.stanford.irt.laneweb.flickr.FlickrPhotoListService;
import edu.stanford.irt.laneweb.flickr.FlickrPhotoSAXStrategy;

@Configuration
public class FlickrConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/flickr-photo")
    @Scope("prototype")
    public Generator flickrPhotoGenerator() throws IOException {
        return new FlickrPhotoGenerator(flickrPhotoListService(), new FlickrPhotoSAXStrategy());
    }

    @Bean
    public FlickrPhotoListService flickrPhotoListService() throws IOException {
        return new FlickrPhotoListService(new FlickrPhotoList(
                getClass().getResourceAsStream("/edu/stanford/irt/laneweb/flickr/flickr-photos.txt")));
    }
}

package edu.stanford.irt.laneweb.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.laneweb.history.HistoryPhotoGenerator;
import edu.stanford.irt.laneweb.history.HistoryPhotoList;
import edu.stanford.irt.laneweb.history.HistoryPhotoListService;
import edu.stanford.irt.laneweb.history.HistoryPhotoSAXStrategy;

@Configuration
public class HistoryPhotoConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/history-photo")
    @Scope("prototype")
    public Generator historyPhotoGenerator() throws IOException {
        return new HistoryPhotoGenerator(historyPhotoListService(), new HistoryPhotoSAXStrategy());
    }

    @Bean
    public HistoryPhotoListService historyPhotoListService() throws IOException {
        return new HistoryPhotoListService(new HistoryPhotoList(
                getClass().getResourceAsStream("/edu/stanford/irt/laneweb/history/history-photos.txt")));
    }
}

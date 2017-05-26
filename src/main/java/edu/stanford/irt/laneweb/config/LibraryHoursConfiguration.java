package edu.stanford.irt.laneweb.config;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.hours.HoursGenerator;
import edu.stanford.irt.laneweb.hours.HoursListSAXStrategy;
import edu.stanford.irt.libraryhours.CalendarFactory;
import edu.stanford.irt.libraryhours.CredentialFactory;
import edu.stanford.irt.libraryhours.Hours;
import edu.stanford.irt.libraryhours.LibraryHoursService;

@Configuration
public class LibraryHoursConfiguration {

    private String accountId;

    private String calendarId;

    private String privateKeyFile;

    public LibraryHoursConfiguration(
            @Value("${edu.stanford.irt.laneweb.hours.privateKeyFile}") final String privateKeyFile,
            @Value("${edu.stanford.irt.laneweb.hours.accountId}") final String accountId,
            @Value("${edu.stanford.irt.laneweb.hours.calendarId}") final String calendarId) {
        this.privateKeyFile = privateKeyFile;
        this.accountId = accountId;
        this.calendarId = calendarId;
    }

    @Bean
    public Calendar calendar() {
        return new CalendarFactory(credential(), httpTransport(), jsonFactory(), "laneweb").createCalendar();
    }

    @Bean
    public Credential credential() {
        return new CredentialFactory(httpTransport(), jsonFactory(), this.accountId, new File(this.privateKeyFile))
                .createCredential();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/hours")
    @Scope("prototype")
    public HoursGenerator hoursGenerator() {
        return new HoursGenerator(libraryHoursService(), hoursSAXStrategy());
    }

    @Bean
    public SAXStrategy<List<List<Hours>>> hoursSAXStrategy() {
        return new HoursListSAXStrategy();
    }

    @Bean
    public HttpTransport httpTransport() {
        try {
            return GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new LanewebException(e);
        }
    }

    @Bean
    public JsonFactory jsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    @Bean
    public LibraryHoursService libraryHoursService() {
        return new LibraryHoursService(calendar(), this.calendarId);
    }
}

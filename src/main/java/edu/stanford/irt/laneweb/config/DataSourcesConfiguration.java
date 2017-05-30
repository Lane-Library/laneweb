package edu.stanford.irt.laneweb.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourcesConfiguration {

    @Bean(name = { "javax.sql.DataSource/eresources", "javax.sql.DataSource/bookmarks",
            "javax.sql.DataSource/bookcovers" })
    public DataSource eresourcesDataSource(@Value("${edu.stanford.irt.laneweb.db.eresources.url}") final String url,
            @Value("${edu.stanford.irt.laneweb.db.eresources.user}") final String user,
            @Value("${edu.stanford.irt.laneweb.db.eresources.password}") final String password)
            throws SQLException, ClassNotFoundException {
        HikariConfig config = new HikariConfig();
        config.setInitializationFailFast(false);
        config.setConnectionTimeout(5000L);
        config.setUsername(user);
        config.setPassword(password);
        config.setJdbcUrl(url);
        return new HikariDataSource(config);
    }
}

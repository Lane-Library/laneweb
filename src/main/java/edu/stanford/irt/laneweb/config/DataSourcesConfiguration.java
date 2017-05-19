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

    @Bean(name = { "javax.sql.DataSource/grandrounds", "javax.sql.DataSource/catalog" })
    public DataSource grandroundsDataSource(@Value("${edu.stanford.irt.laneweb.db.grandrounds.url}") final String url,
            @Value("${edu.stanford.irt.laneweb.db.grandrounds.user}") final String user,
            @Value("${edu.stanford.irt.laneweb.db.grandrounds.password}") final String password)
            throws SQLException, ClassNotFoundException {
        HikariConfig config = new HikariConfig();
        config.setInitializationFailFast(false);
        config.setConnectionTimeout(5000L);
        config.setUsername(user);
        config.setPassword(password);
        config.setJdbcUrl(url);
        return new HikariDataSource(config);
    }

    @Bean(name = { "javax.sql.DataSource/voyager", "javax.sql.DataSource/voyager-login" })
    public DataSource voyagerDataSource(@Value("${edu.stanford.irt.laneweb.db.voyager.url}") final String url,
            @Value("${edu.stanford.irt.laneweb.db.voyager.user}") final String user,
            @Value("${edu.stanford.irt.laneweb.db.voyager.password}") final String password)
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

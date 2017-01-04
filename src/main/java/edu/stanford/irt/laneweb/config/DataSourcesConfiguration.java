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


    @Bean(name = "javax.sql.DataSource/eresources")
    public DataSource eresourcesDataSource(@Value("%{edu.stanford.irt.laneweb.db.eresources.dataSource.url}") final String url,
            @Value("%{edu.stanford.irt.laneweb.db.eresources.dataSource.user}") final String user,
            @Value("%{edu.stanford.irt.laneweb.db.eresources.dataSource.password}") final String password)
            throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.setInitializationFailFast(false);
        config.setConnectionTimeout(5000L);
        config.setUsername(user);
        config.setJdbcUrl(url);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    @Bean(name = "javax.sql.DataSource/grandrounds")
    public DataSource grandroundsDataSource(@Value("%{edu.stanford.irt.laneweb.db.grandrounds.dataSource.url}") final String url,
            @Value("%{edu.stanford.irt.laneweb.db.grandrounds.dataSource.user}") final String user,
            @Value("%{edu.stanford.irt.laneweb.db.grandrounds.dataSource.password}") final String password)
            throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.setInitializationFailFast(false);
        config.setConnectionTimeout(5000L);
        config.setUsername(user);
        config.setJdbcUrl(url);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    @Bean(name = "javax.sql.DataSource/voyager")
    public DataSource voyagerDataSource(@Value("%{edu.stanford.irt.laneweb.db.voyager.dataSource.url}") final String url,
            @Value("%{edu.stanford.irt.laneweb.db.voyager.dataSource.user}") final String user,
            @Value("%{edu.stanford.irt.laneweb.db.voyager.dataSource.password}") final String password)
                    throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.setInitializationFailFast(false);
        config.setConnectionTimeout(5000L);
        config.setUsername(user);
        config.setJdbcUrl(url);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}

package edu.stanford.irt.laneweb.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

@Configuration
public class DataSourcesConfiguration {

    @Bean(name = "javax.sql.DataSource/eresources")
    public DataSource eresourcesDataSource(@Value("%{edu.stanford.irt.laneweb.db.eresources.url}") final String url,
            @Value("%{edu.stanford.irt.laneweb.db.eresources.user}") final String user,
            @Value("%{edu.stanford.irt.laneweb.db.eresources.password}") final String password,
            @Value("%{edu.stanford.irt.laneweb.db.eresources.maxPoolSize}") final int maxPoolSize,
            @Value("%{edu.stanford.irt.laneweb.db.eresources.onsConfiguration}") final String onsConfiguration,
            @Value("%{edu.stanford.irt.laneweb.db.eresources.fastConnectionFailoverEnabled}") final boolean failoverEnabled)
            throws SQLException {
        PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setONSConfiguration(onsConfiguration);
        dataSource.setFastConnectionFailoverEnabled(failoverEnabled);
        return dataSource;
    }

    @Bean(name = "javax.sql.DataSource/grandrounds")
    public DataSource grandroundsDataSource(@Value("%{edu.stanford.irt.laneweb.db.grandrounds.url}") final String url,
            @Value("%{edu.stanford.irt.laneweb.db.grandrounds.user}") final String user,
            @Value("%{edu.stanford.irt.laneweb.db.grandrounds.password}") final String password,
            @Value("%{edu.stanford.irt.laneweb.db.grandrounds.maxPoolSize}") final int maxPoolSize)
            throws SQLException {
        PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setMaxPoolSize(maxPoolSize);
        return dataSource;
    }

    @Bean(name = "javax.sql.DataSource/voyager")
    public DataSource voyagerDataSource(@Value("%{edu.stanford.irt.laneweb.db.voyager.url}") final String url,
            @Value("%{edu.stanford.irt.laneweb.db.voyager.user}") final String user,
            @Value("%{edu.stanford.irt.laneweb.db.voyager.password}") final String password,
            @Value("%{edu.stanford.irt.laneweb.db.voyager.maxPoolSize}") final int maxPoolSize) throws SQLException {
        PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setMaxPoolSize(maxPoolSize);
        return dataSource;
    }
}

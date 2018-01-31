package edu.stanford.irt.laneweb.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

@Configuration
public class DataSourcesConfiguration {

    private static final String ORACLE_DATASOURCE = "oracle.jdbc.pool.OracleDataSource";

    private static final int THREE_SECONDS = 3;

    @Bean
    @Profile("!gce")
    public DataSource onPremiseDataSource(@Value("${edu.stanford.irt.laneweb.db.eresources.url}") final String url,
            @Value("${edu.stanford.irt.laneweb.db.eresources.user}") final String user,
            @Value("${edu.stanford.irt.laneweb.db.eresources.password}") final String password,
            @Value("${edu.stanford.irt.laneweb.db.eresources.maxPoolSize}") final int maxPoolSize,
            @Value("${edu.stanford.irt.laneweb.db.eresources.onsConfiguration}") final String onsConfiguration,
            @Value("${edu.stanford.irt.laneweb.db.eresources.fastConnectionFailoverEnabled}") final boolean failoverEnabled)
            throws SQLException {
        PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        dataSource.setConnectionFactoryClassName(ORACLE_DATASOURCE);
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setONSConfiguration(onsConfiguration);
        dataSource.setFastConnectionFailoverEnabled(failoverEnabled);
        dataSource.setLoginTimeout(THREE_SECONDS);
        return dataSource;
    }
}

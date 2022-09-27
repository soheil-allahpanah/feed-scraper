package ir.sooall.feedscraper.config;

import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class DBConfig {

    @Autowired
    private Environment environment;

    @Bean
    @Profile("test")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driverClassName"));
        return dataSource;
    }

    @Bean
    @Profile("test")
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource());
    }

    @Bean
    @Profile("test")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    @Profile("test")
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }

    @Bean
    @Profile("test")
    public ExceptionTranslator exceptionTransformer() {
        return new ExceptionTranslator();
    }

    @Bean
    @Profile("test")
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(new DefaultExecuteListenerProvider(exceptionTransformer()));

        String sqlDialectName = environment.getRequiredProperty("jooq.sql.dialect");
        SQLDialect dialect = SQLDialect.valueOf(sqlDialectName);
        jooqConfiguration.set(dialect);

        return jooqConfiguration;
    }

    @Bean
    @Profile("test")
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }
}

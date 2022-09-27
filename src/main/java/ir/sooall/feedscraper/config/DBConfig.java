package ir.sooall.feedscraper.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
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
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(environment.getRequiredProperty("spring.datasource.url"));
    config.setUsername(environment.getRequiredProperty("spring.datasource.username"));
    config.setPassword(environment.getRequiredProperty("spring.datasource.password"));
    config.setDriverClassName(environment.getRequiredProperty("spring.datasource.driverClassName"));
    return new HikariDataSource(config);
  }

  @Bean
  public TransactionAwareDataSourceProxy transactionAwareDataSource() {
    return new TransactionAwareDataSourceProxy(dataSource());
  }

  @Bean
  public DataSourceTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }

  @Bean
  public DataSourceConnectionProvider connectionProvider() {
    return new DataSourceConnectionProvider(transactionAwareDataSource());
  }

  @Bean
  public ExceptionTranslator exceptionTransformer() {
    return new ExceptionTranslator();
  }

  @Bean
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
  public DefaultDSLContext dsl() {
    return new DefaultDSLContext(configuration());
  }
}

package funfun.gsuaki.configs;

import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"production", "stage"})
public class DatabaseConfig {

  @Value("${db.fury.hostenv}")
  private String hostEnvironmentVariable;

  @Value("${db.fury.passwordenv}")
  private String passwordEnvironmentVariable;

  @Value("${db.fury.schema}")
  private String schemaName;

  @Value("${db.fury.user}")
  private String dbUser;

  @Value("${db.datasource.minIdle}")
  private Integer minIdle;

  @Value("${db.datasource.maxPoolSize}")
  private Integer maxPoolSize;

  @Value("${db.datasource.idleTimeout}")
  private Integer idleTimeout;

  @Bean
  @SneakyThrows
  public HikariDataSource sqlConnection() {

    final String host = "jdbc:mysql://" + hostEnvironmentVariable + "/" + schemaName;
    final String password = passwordEnvironmentVariable;

    final HikariDataSource dataSource = DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .url(host)
        .username(dbUser)
        .password(password)
        .build();

    dataSource.setIdleTimeout(idleTimeout);
    dataSource.setMinimumIdle(minIdle);
    dataSource.setMaximumPoolSize(maxPoolSize);

    return dataSource;
  }
}

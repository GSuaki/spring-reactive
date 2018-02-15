package funfun.gsuaki.application;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.TimeZone;

@EnableWebFlux
@EntityScan(basePackages = "funfun.gsuaki")
@EnableJpaRepositories(basePackages = "funfun.gsuaki")
@SpringBootApplication(scanBasePackages = "funfun.gsuaki")
public class ReactiveApplication {

  public static void main(String[] args) {
    setupEnvironment();
    SpringApplication.run(ReactiveApplication.class, args);
  }

  private static void setupEnvironment() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, currentEnv());
  }

  private static String currentEnv() {
    final String scope = System.getenv("SCOPE");

    if (scope == null) return "development";

    if (StringUtils.containsIgnoreCase(scope, "consumer") &&
        StringUtils.containsIgnoreCase(scope, "stage")) {
      return "stage";
    }

    if (StringUtils.containsIgnoreCase(scope, "consumer") &&
        StringUtils.containsIgnoreCase(scope, "production")) {
      return "production";
    }

    return scope;
  }
}

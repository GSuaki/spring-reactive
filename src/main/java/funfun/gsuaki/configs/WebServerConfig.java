package funfun.gsuaki.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import funfun.gsuaki.api.routers.ArtistRouter;
import funfun.gsuaki.api.routers.HealthRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class WebServerConfig {

  @Autowired
  private ArtistRouter artistRouter;

  @Autowired
  private HealthRouter healthRouter;

  @Bean
  public RouterFunction<ServerResponse> serverRoutes() {
    return artistRouter.createPostRoute()
        .and(healthRouter.createHealthRoute());
  }

  @Configuration
  public static class WebConfig implements WebFluxConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
      configurer.defaultCodecs().jackson2JsonDecoder(
          new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
      configurer.defaultCodecs().jackson2JsonEncoder(
          new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
    }
  }
}

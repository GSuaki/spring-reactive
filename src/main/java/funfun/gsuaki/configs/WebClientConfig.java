package funfun.gsuaki.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    public ExchangeStrategies exchangeStrategiesFactory(final ObjectMapper objectMapper) {
        return ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(
                            new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                    clientCodecConfigurer.defaultCodecs().jackson2JsonEncoder(
                            new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                })
                .build();
    }

    @Bean
    public WebClient webClientFactory(final ExchangeStrategies strategies) {
        ClientHttpConnector connector = new ReactorClientHttpConnector(
                builder -> builder
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2250)
                        .afterNettyContextInit(context -> {
                            context.addHandlerLast(new ReadTimeoutHandler(2250, TimeUnit.MILLISECONDS));
                        })
        );
        return WebClient.builder().clientConnector(connector).exchangeStrategies(strategies).build();
    }
}

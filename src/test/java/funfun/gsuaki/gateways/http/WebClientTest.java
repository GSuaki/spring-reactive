package funfun.gsuaki.gateways.http;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import funfun.gsuaki.application.ReactiveApplication;
import io.netty.handler.timeout.ReadTimeoutException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.ConnectException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReactiveApplication.class)
@ActiveProfiles("test")
public class WebClientTest {

  @Autowired
  private WebClient webClient;

  @ClassRule
  public static WireMockClassRule wireMockRule = new WireMockClassRule(9092);

  @Rule
  public WireMockClassRule instanceRule = wireMockRule;

  @Test
  public void testConnectionTimeout() {
    instanceRule.stop();

    final Mono<String> publisher = webClient.get().uri("http://localhost:9092").retrieve().bodyToMono(String.class);

    StepVerifier
        .create(publisher)
        .expectError(ConnectException.class)
        .verify();
  }

  @Test
  public void testReadTimeout() {
    stubFor(
        get(urlEqualTo("/timeout"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/plain")
                .withBody("dummy text")
                .withFixedDelay(3000)
            )
    );

    StepVerifier.create(webClient.get().uri("http://localhost:9092/timeout")
        .retrieve().bodyToMono(String.class))
        .expectError(ReadTimeoutException.class).verify();
  }
}

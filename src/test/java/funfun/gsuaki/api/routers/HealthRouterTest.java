package funfun.gsuaki.api.routers;

import funfun.gsuaki.application.ReactiveApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReactiveApplication.class)
@ActiveProfiles("test")
public class HealthRouterTest {
  private static final String PING_URI = "/ping";

  private WebTestClient webTestClient;

  @Before
  public void setUp() {
    HealthRouter router = new HealthRouter();
    webTestClient = WebTestClient.bindToRouterFunction(router.createHealthRoute()).build();
  }

  @Test
  public void shouldRespondPontToPingRequest() {
    webTestClient.get().uri(PING_URI).exchange()
        .expectStatus().isOk().expectBody(String.class).isEqualTo("pong");
  }
}

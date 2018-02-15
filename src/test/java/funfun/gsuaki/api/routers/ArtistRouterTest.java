package funfun.gsuaki.api.routers;

import funfun.gsuaki.api.handlers.ArtistHandler;
import funfun.gsuaki.application.ReactiveApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.MockServerConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReactiveApplication.class)
public class ArtistRouterTest {

  private static final String ARTIST_URI = "/artist";

  private WebTestClient webTestClient;

  @Mock
  private ArtistHandler handler;

  @Autowired
  private List<WebExceptionHandler> exceptionHandlerList;

  @Before
  public void setUp() {
    initMocks(this);

    ArtistRouter router = new ArtistRouter(handler);

    webTestClient =
        WebTestClient
            .bindToRouterFunction(
                router.createPostRoute())
            .apply(new MockServerConfigurer() {
              @Override
              public void beforeServerCreated(WebHttpHandlerBuilder builder) {
                exceptionHandlerList.forEach(builder::exceptionHandler);
              }
            }).build();
  }

  @Test
  public void routerShouldAcceptPostRequestForArtistPath() {

    doReturn(ServerResponse.created(URI.create("http://localhost:8080/dummy")).build())
        .when(handler)
        .createArtist(any());

    webTestClient.post()
        .uri(ARTIST_URI)
        .contentType(MediaType.APPLICATION_JSON).exchange()
        .expectStatus()
        .isCreated();

    verify(handler, times(1)).createArtist(any());
  }

  @Test
  public void routerShouldRejectPutAndDeleteOnArtistPath() {

    webTestClient.put()
        .uri(ARTIST_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();

    webTestClient.delete().uri(ARTIST_URI).exchange().expectStatus().isNotFound();

    verify(handler, never()).createArtist(any());
  }
}

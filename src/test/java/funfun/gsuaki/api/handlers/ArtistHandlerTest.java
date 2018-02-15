package funfun.gsuaki.api.handlers;

import funfun.gsuaki.api.exception.ValidationException;
import funfun.gsuaki.api.validations.ArtistValidation;
import funfun.gsuaki.domains.Artist;
import funfun.gsuaki.models.api.ArtistResponse;
import funfun.gsuaki.services.NewArtistService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ArtistHandlerTest {

  @Mock
  private NewArtistService newArtistService;

  @Mock
  private ArtistValidation artistValidation;

  private ArtistHandler handler;

  @Before
  public void setUp() {
    initMocks(this);
    handler = new ArtistHandler(newArtistService, artistValidation);
  }

  @Test
  public void createArtistWhenVerificationFailsShouldEmitValidationException() {

    final Artist ti = new Artist();

    final ServerRequest serverRequest = buildMockServerRequestWithJsonBody(ti);

    doReturn(Mono.error(new ValidationException(""))).when(artistValidation)
        .validateBody(any(ServerRequest.class));

    StepVerifier.create(handler.createArtist(serverRequest))
        .expectError(ValidationException.class)
        .verify();

    verify(newArtistService, never()).createArtistFor(any(Artist.class));
  }

  @Test
  public void createArtistForValidRequestShouldReturnCreatedIfSucceeded() {

    final Artist ii = new Artist(9L, "Bandao");
    final ArtistResponse response = new ArtistResponse(1L, "Banda");

    final ServerRequest serverRequest = buildMockServerRequestWithJsonBody(ii);
    doReturn(Mono.just(response))
        .when(newArtistService).createArtistFor(any(Artist.class));
    doReturn(Mono.just(ii)).when(artistValidation)
        .validateBody(any(ServerRequest.class));

    StepVerifier.create(handler.createArtist(serverRequest))
        .assertNext(sr -> {
          assertThat(sr.statusCode().value()).isEqualTo(201);
          assertThat(sr.headers().getLocation()).isNotNull();
        })
        .expectComplete()
        .verify();
  }

  private ServerRequest buildMockServerRequestWithJsonBody(final Artist ti) {
    return MockServerRequest.builder()
        .header("Content-Type", "application/json")
        .body(Mono.just(ti));
  }
}

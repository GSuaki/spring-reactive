package funfun.gsuaki.services.impl;

import funfun.gsuaki.domains.Artist;
import funfun.gsuaki.gateways.ArtistGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static funfun.gsuaki.testdata.Models.dummyArtist;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class NewArtistServiceTest {

  private final static Integer ONCE = 1;

  private final static Long ID = 2L;
  private final static String NAME = "Banda";

  private NewArtistServiceImpl newArtistService;

  @Mock
  private ArtistGateway artistGateway;

  @Before
  public void setUp() {
    initMocks(this);
    newArtistService = new NewArtistServiceImpl(artistGateway);
  }

  @Test
  public void givenInputWithArtistIdNotFoundShouldEmitError() {

    final Artist artist = dummyArtist(ID, NAME);

    final WebClientResponseException error =
        new WebClientResponseException("service returned 404", 404, "Resource not found", null, null, null);

    doReturn(Mono.error(error)).when(artistGateway).getArtist(eq(ID));

    StepVerifier.create(newArtistService.createArtistFor(artist))
        .expectError(WebClientResponseException.class)
        .verify();

    verify(artistGateway, never()).saveArtist(any());
  }

  @Test
  public void givenValidArtistIdShouldEmitCompletionWithoutError() {

    final Artist artist = new Artist(1l, NAME);

    doReturn(Mono.just(new Artist())).when(artistGateway).getArtist(any());
    doReturn(Mono.just(artist)).when(artistGateway).saveArtist(any());

    StepVerifier.create(newArtistService.createArtistFor(artist))
        .expectNextCount(ONCE)
        .expectComplete()
        .verify();

    verify(artistGateway, times(ONCE)).saveArtist(any(Artist.class));
  }
}

package funfun.gsuaki.gateways.data;

import funfun.gsuaki.application.ReactiveApplication;
import funfun.gsuaki.domains.Artist;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReactiveApplication.class)
public class ArtistDbGatewayTest {

  private static final Long ID = 2L;

  private static final String NAME = "Badinhas";

  @Autowired
  @Qualifier("artistRepositoryMock")
  private ArtistRepository repository;

  private ArtistDbGateway artistDbGateway;

  @Before
  public void setUp() {
    artistDbGateway = new ArtistDbGateway(repository);
    repository.deleteAll();
    reset(repository);
  }

  @Test
  public void givenValidArtistSavedDataShouldBeEmitted() {
    StepVerifier.create(artistDbGateway.saveArtist(new Artist(ID, NAME)))
        .assertNext(ii -> assertThat(ii.getId()).isGreaterThan(0L)).expectComplete().verify();
  }

  @Test
  public void givenRepeatedArtistErrorShouldBeEmitted() {

    StepVerifier.create(artistDbGateway.saveArtist(new Artist(ID, NAME)))
        .expectNextCount(1)
        .expectComplete()
        .verify();

    StepVerifier.create(artistDbGateway.saveArtist(new Artist(ID, NAME)))
        .expectError(DataIntegrityViolationException.class)
        .verify();
  }

  @Test
  public void givenValidArtistWhenDatabaseFailsTreeTimesThenDataShouldStillBeSavedAndEmitted() {

    final Artist request = new Artist(ID, NAME);
    final Artist response = new Artist(ID, NAME);
    response.setId(1L);

    doAnswer(new Answer() {
      private Integer times = 0;

      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        times++;
        if (times >= 3) {
          return response;
        }
        throw new QueryTimeoutException("query timeout");
      }
    }).when(repository).save(any(Artist.class));

    StepVerifier.create(artistDbGateway.saveArtist(request))
        .expectNext(response)
        .expectComplete()
        .verify();
  }

  @Test
  public void givenValidArtistWhenDatabaseFailsMoreThanTreeTimesThenErrorShouldBeEmitted() {

    doThrow(new QueryTimeoutException("query timeout")).when(repository).save(any(Artist.class));

    StepVerifier.create(artistDbGateway.saveArtist(new Artist(ID, NAME)))
        .expectError(QueryTimeoutException.class)
        .verify();
  }
}

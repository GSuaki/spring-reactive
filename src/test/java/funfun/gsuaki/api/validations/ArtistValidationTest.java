package funfun.gsuaki.api.validations;

import funfun.gsuaki.api.exception.ValidationException;
import funfun.gsuaki.application.ReactiveApplication;
import funfun.gsuaki.domains.Artist;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.validation.Validator;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReactiveApplication.class)
public class ArtistValidationTest {

  private ArtistValidation artistValidation;

  @Autowired
  private Validator validator;

  @Before
  public void setUp() {
    artistValidation = new ArtistValidation(validator);
  }

  @Test
  public void missingArtistIdInputShouldEmitValidationExceptionError() {

    final Artist ii = new Artist(null, null);
    final ServerRequest serverRequest = buildMockServerRequestWithJsonBody(ii);

    StepVerifier.create(artistValidation.validateBody(serverRequest))
        .expectError(ValidationException.class)
        .verify();
  }

  @Test
  public void negativeArtistIdInputShouldEmitValidationExceptionError() {
    final Artist ii = new Artist(-10L, "Banda");
    final ServerRequest serverRequest = buildMockServerRequestWithJsonBody(ii);
    StepVerifier.create(artistValidation.validateBody(serverRequest))
        .expectError(ValidationException.class)
        .verify();
  }

  @Test
  public void validInputShouldEmitItself() {
    final Artist ii = new Artist(1l, "banda");
    final ServerRequest serverRequest = buildMockServerRequestWithJsonBody(ii);
    StepVerifier.create(artistValidation.validateBody(serverRequest))
        .expectNext(ii)
        .expectComplete()
        .verify();
  }

  @Test
  public void getArtistForInvalidArtistIdShouldEmitValidationException() {
    final ServerRequest serverRequest1 = buildMockServerRequestWithBothQueryParams("not_a_number", "homologation");
    StepVerifier.create(artistValidation.validateQueryParams(serverRequest1))
        .expectError(ValidationException.class)
        .verify();

    final ServerRequest serverRequest2 = buildMockServerRequestWithBothQueryParams("-2", "homologation");
    StepVerifier.create(artistValidation.validateQueryParams(serverRequest2))
        .expectError(ValidationException.class)
        .verify();

    final ServerRequest serverRequest3 = buildMockServerRequestWithOneQueryParam("environment", "homologation");
    StepVerifier.create(artistValidation.validateQueryParams(serverRequest3))
        .expectError(ValidationException.class)
        .verify();
  }

  @Test
  public void getArtistForInvalidEnvironmentShouldNotEmitValidationException() {
    final ServerRequest serverRequest1 = buildMockServerRequestWithOneQueryParam("artist_id", "12345");
    StepVerifier.create(artistValidation.validateQueryParams(serverRequest1))
        .expectNextCount(1)
        .expectComplete()
        .verify();

    final ServerRequest serverRequest2 = buildMockServerRequestWithBothQueryParams("12345", "not_an_enum_value");
    StepVerifier.create(artistValidation.validateQueryParams(serverRequest2))
        .expectNextCount(1)
        .expectComplete()
        .verify();
  }

  @Test
  public void getArtistForInvalidEnvironmentAndArtistIdShouldEmitValidationException() {
    final ServerRequest serverRequest = MockServerRequest.builder().build();
    StepVerifier.create(artistValidation.validateQueryParams(serverRequest))
        .expectError(ValidationException.class)
        .verify();
  }

  @Test
  public void getArtistForValidEnvironmentAndArtistIdShouldEmitItself() {
    final ServerRequest serverRequest = buildMockServerRequestWithBothQueryParams("12345", "production");
    StepVerifier.create(artistValidation.validateQueryParams(serverRequest))
        .expectNextCount(1)
        .expectComplete()
        .verify();
  }

  private ServerRequest buildMockServerRequestWithJsonBody(final Artist ti) {
    return MockServerRequest.builder()
        .header("Content-Type", "application/json")
        .body(Mono.just(ti));
  }

  private ServerRequest buildMockServerRequestWithBothQueryParams(final String artistId, final String environment) {
    return MockServerRequest.builder()
        .queryParam("artist_id", artistId)
        .queryParam("environment", environment)
        .build();
  }

  private ServerRequest buildMockServerRequestWithOneQueryParam(final String name, final String value) {
    return MockServerRequest.builder()
        .queryParam(name, value)
        .build();
  }
}

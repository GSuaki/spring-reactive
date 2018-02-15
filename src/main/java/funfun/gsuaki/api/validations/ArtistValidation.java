package funfun.gsuaki.api.validations;

import funfun.gsuaki.api.exception.ValidationException;
import funfun.gsuaki.domains.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import javax.validation.Validator;
import java.util.Optional;

@Component
public class ArtistValidation {

  private final JavaxValidation<Artist> javaxValidation;

  @Autowired
  ArtistValidation(Validator validator) {
    javaxValidation = new JavaxValidation<>(validator);
  }

  public Mono<Artist> validateBody(final ServerRequest request) {
    return request
        .bodyToMono(Artist.class)
        .flatMap(javaxValidation::validate);
  }

  public Mono<Artist> validateQueryParams(final ServerRequest request) {
    return Mono.create(sink -> {
      final Optional<Long> id = artistIdFromRequest(request);

      if (!id.isPresent()) {
        sink.error(new ValidationException("artist_id query parameter invalid"));
      } else {
        final Artist a = new Artist();
        a.setId(id.get());

        sink.success(a);
      }
    });
  }

  private static Optional<Long> artistIdFromRequest(final ServerRequest request) {
    return request.queryParam("artist_id")
        .filter(s -> s.matches("\\d+")).map(Long::valueOf);
  }
}

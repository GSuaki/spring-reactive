package funfun.gsuaki.api.handlers;

import funfun.gsuaki.api.validations.ArtistValidation;
import funfun.gsuaki.services.NewArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.jooq.lambda.Seq.seq;

@Component
public class ArtistHandler {

  private final NewArtistService newArtistService;

  private final ArtistValidation artistValidation;

  @Autowired
  public ArtistHandler(final NewArtistService newArtistService, final ArtistValidation artistValidation) {
    this.newArtistService = newArtistService;
    this.artistValidation = artistValidation;
  }

  @NonNull
  public Mono<ServerResponse> createArtist(@NonNull final ServerRequest request) {

    final Mono<ServerResponse> source = artistValidation.validateBody(request)
        .flatMap(newArtistService::createArtistFor)
        .flatMap(i -> ServerResponse.created(URI.create(i.getName())).syncBody(i));

    return monoWithChainErrorMappings(source, Stream.of());
  }

  private Mono<ServerResponse> monoWithChainErrorMappings(Mono<ServerResponse> source,
                                                          Stream<Function<Throwable, Throwable>> chain) {
    return seq(chain).foldLeft(
        source,
        Mono::onErrorMap
    );
  }
}

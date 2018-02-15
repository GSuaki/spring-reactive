package funfun.gsuaki.api.routers;

import funfun.gsuaki.api.handlers.ArtistHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Component
public class ArtistRouter {

  private static final String ARTIST_URI = "/artist";
  private static final String ARTIST_VALIDATION_URI = "/artist/validation";

  private final ArtistHandler artistHandler;

  @Autowired
  public ArtistRouter(final ArtistHandler artistHandler) {
    this.artistHandler = artistHandler;
  }

  public RouterFunction<ServerResponse> createPostRoute() {

    final RequestPredicate predicate = POST(ARTIST_URI).and(contentType(APPLICATION_JSON));

    return RouterFunctions.route(predicate, artistHandler::createArtist);
  }
}

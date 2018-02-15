package funfun.gsuaki.services;

import funfun.gsuaki.domains.Artist;
import funfun.gsuaki.models.api.ArtistResponse;
import reactor.core.publisher.Mono;

public interface NewArtistService {

  Mono<ArtistResponse> createArtistFor(final Artist artistArtist);

}

package funfun.gsuaki.gateways;

import funfun.gsuaki.domains.Artist;
import reactor.core.publisher.Mono;

public interface ArtistGateway {

  Mono<Artist> getArtist(final Long artistId);

  Mono<Artist> saveArtist(final Artist artist);
}

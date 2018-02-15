package funfun.gsuaki.services.impl;

import funfun.gsuaki.domains.Artist;
import funfun.gsuaki.gateways.ArtistGateway;
import funfun.gsuaki.models.api.ArtistError;
import funfun.gsuaki.models.api.ArtistResponse;
import funfun.gsuaki.services.NewArtistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NewArtistServiceImpl implements NewArtistService {

  private final ArtistGateway gateway;

  @Autowired
  public NewArtistServiceImpl(final ArtistGateway gateway) {
    this.gateway = gateway;
  }

  @Override
  public Mono<ArtistResponse> createArtistFor(final Artist artist) {
    log.info("create artist: " + artist);

    return gateway.getArtist(artist.getId())
        .flatMap(result -> result.getId() == null ?
            doCreateArtist(artist) : emitAlreadyBeenCreatedError(artist));
  }

  private Mono<ArtistResponse> doCreateArtist(final Artist artist) {
    return gateway.saveArtist(artist)
        .flatMap(result -> Mono.just(new ArtistResponse(result)));
  }

  private Mono<ArtistResponse> emitAlreadyBeenCreatedError(final Artist artist) {
    return Mono.error(new ArtistError.ArtistAlreadyCreatedException(artist));
  }
}

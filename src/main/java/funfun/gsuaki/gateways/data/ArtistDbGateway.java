package funfun.gsuaki.gateways.data;

import funfun.gsuaki.domains.Artist;
import funfun.gsuaki.gateways.ArtistGateway;
import funfun.gsuaki.gateways.RetryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Component
public class ArtistDbGateway implements ArtistGateway {

  private final RetryConfig retryConfig;

  private final ArtistRepository repository;

  @Autowired
  public ArtistDbGateway(final ArtistRepository repository) {
    this.repository = repository;
    this.retryConfig = new RetryConfig(Duration.ofSeconds(1), 2);
  }

  @Override
  public Mono<Artist> getArtist(Long artistId) {
    return MonoHavingDataAccessRetry
        .of(Mono.fromCallable(() -> repository.findById(artistId).orElse(new Artist())), retryConfig)
        .subscribeOn(Schedulers.elastic())
        .publishOn(Schedulers.parallel());
  }

  @Override
  public Mono<Artist> saveArtist(final Artist artist) {
    return MonoHavingDataAccessRetry
        .of(Mono.fromCallable(() -> repository.save(artist)), retryConfig)
        .subscribeOn(Schedulers.elastic())
        .publishOn(Schedulers.parallel());
  }
}

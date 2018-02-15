package funfun.gsuaki.gateways.data;

import funfun.gsuaki.gateways.RetryConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.dao.TransientDataAccessException;
import reactor.core.publisher.Mono;
import reactor.retry.Backoff;
import reactor.retry.Retry;
import reactor.retry.RetryExhaustedException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MonoHavingDataAccessRetry {

  static <T> Mono<T> of(final Mono<T> source, final RetryConfig retryConfig) {
    return source
        .retryWhen(transientDataAccessFailure(retryConfig))
        .onErrorMap(t -> t instanceof RetryExhaustedException, Throwable::getCause);
  }

  private static <T> Retry<T> transientDataAccessFailure(final RetryConfig retryConfig) {
    return Retry.<T>anyOf(TransientDataAccessException.class)
        .backoff(Backoff.fixed(retryConfig.getBackoffDelay()))
        .retryMax(retryConfig.getTimes());
  }
}

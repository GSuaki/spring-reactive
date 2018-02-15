package funfun.gsuaki.api.exception.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
class ExceptionToStatusHandler<T extends Throwable> implements WebExceptionHandler {

    private final HttpStatus status;
    private final Class<T> throwableClass;

    ExceptionToStatusHandler(final HttpStatus status, final Class<T> throwableClass) {
        this.status = status;
        this.throwableClass = throwableClass;
    }

    @Override
    @NonNull
    public final  Mono<Void> handle(final @NonNull ServerWebExchange exchange, final @NonNull Throwable ex) {
        if(throwableClass.isInstance(ex)) {
            //noinspection unchecked
            effect(exchange, (T)ex);
            exchange.getResponse().setStatusCode(status);
            return exchange.getResponse().setComplete();
        }
        return Mono.error(ex);
    }

    void effect(final ServerWebExchange exchange, final T ex) {
        log.warn("Exception handled for following request uri: " + exchange.getRequest().getURI().toString(), ex);
    }
}

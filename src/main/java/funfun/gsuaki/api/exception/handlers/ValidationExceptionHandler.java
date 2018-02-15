package funfun.gsuaki.api.exception.handlers;

import funfun.gsuaki.api.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@Order(-4)
@Slf4j
public class ValidationExceptionHandler extends ExceptionToStatusHandler<ValidationException> {
    public ValidationExceptionHandler() {
        super(HttpStatus.BAD_REQUEST, ValidationException.class);
    }

    @Override
    public void effect(final ServerWebExchange exchange, final ValidationException ex) {
        log.info("Invalid data handled for following request uri: " + exchange.getRequest().getURI().toString(), ex);
    }
}

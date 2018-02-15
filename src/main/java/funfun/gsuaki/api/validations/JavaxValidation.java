package funfun.gsuaki.api.validations;

import funfun.gsuaki.api.exception.ValidationException;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

class JavaxValidation<T> {

  private final Validator validator;

  JavaxValidation(final Validator validator) {
    this.validator = validator;
  }

  Mono<T> validate(final T entity) {
    return Mono.create(sink -> {
      final Set<ConstraintViolation<T>> violations = validator.validate(entity);
      if (violations != null && !violations.isEmpty()) {
        sink.error(new ValidationException("Validation Exception"));
      } else {
        sink.success(entity);
      }
    });
  }
}

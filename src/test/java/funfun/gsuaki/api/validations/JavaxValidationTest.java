package funfun.gsuaki.api.validations;

import funfun.gsuaki.api.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class JavaxValidationTest {

  @Mock
  private Validator validator;
  private JavaxValidation<Dummy> customValidaton;

  @Before
  public void setUp() {
    initMocks(this);
    customValidaton = new JavaxValidation<Dummy>(validator) {
      @Override
      public Mono<Dummy> validate(Dummy entity) {
        return super.validate(entity);
      }
    };
  }

  @Test
  public void shouldEmitValidationExceptionIfValidatorReturnedConstraintViolation() {
    doReturn(Collections.singleton(ConstraintViolationImpl.forBeanValidation(null, null, null,
        null, null, null, null, null, null, null, null, null)))
        .when(validator).validate(any());
    StepVerifier.create(customValidaton.validate(new Dummy())).expectError(ValidationException.class).verify();
  }

  @Test
  public void shouldEmitItselfIfValidatorReturnedEmptySet() {
    doReturn(Collections.emptySet()).when(validator).validate(any());
    assertThat(customValidaton.validate(new Dummy("not_null")).block()).isEqualTo(new Dummy("not_null"));
  }

  @Test
  public void shouldEmitItselfIfValidatorReturnedNull() {
    doReturn(null).when(validator).validate(any());
    StepVerifier.create(customValidaton.validate(new Dummy("not_null")));
    assertThat(customValidaton.validate(new Dummy("not_null")).block()).isEqualTo(new Dummy("not_null"));
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  private static class Dummy {
    @NotNull
    private String aString;
  }
}

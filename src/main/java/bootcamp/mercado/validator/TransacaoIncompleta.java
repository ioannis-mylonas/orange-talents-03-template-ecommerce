package bootcamp.mercado.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TransacaoIncompletaValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface TransacaoIncompleta {
    String message() default "Transação já está completa.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

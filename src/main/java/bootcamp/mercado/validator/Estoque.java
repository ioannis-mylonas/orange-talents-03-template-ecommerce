package bootcamp.mercado.validator;

import bootcamp.mercado.produto.Produto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EstoqueValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Estoque {
    String message() default "Estoque insuficiente";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Class<?> target() default Produto.class;
    String field() default "id";
}

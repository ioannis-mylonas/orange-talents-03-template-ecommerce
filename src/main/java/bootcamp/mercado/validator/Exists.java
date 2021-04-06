package bootcamp.mercado.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy=ExistsValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Exists {
	String message() default "Elemento não existe";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
	Class<?> target();
	String field();
}

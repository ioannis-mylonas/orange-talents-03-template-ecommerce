package bootcamp.mercado.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
	String message() default "Precisa ser único";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
	Class<?> target();
	String field();
}

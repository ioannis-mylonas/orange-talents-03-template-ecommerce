package bootcamp.mercado.validator;

import bootcamp.mercado.gateway.GatewayList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GatewayExistsValidator implements ConstraintValidator<GatewayExists, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return GatewayList.exists(value);
    }
}

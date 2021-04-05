package bootcamp.mercado.validator;

import bootcamp.mercado.gateway.GatewayService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GatewayExistsValidator implements ConstraintValidator<GatewayExists, String> {
    private final GatewayService gatewayService;

    public GatewayExistsValidator(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return gatewayService.exists(value);
    }
}

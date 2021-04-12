package bootcamp.mercado.validator;

import bootcamp.mercado.produto.compra.gateway.GatewayList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GatewayExistsValidator implements ConstraintValidator<GatewayExists, String> {
    private final GatewayList gatewayList;

    public GatewayExistsValidator(GatewayList gatewayList) {
        this.gatewayList = gatewayList;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return gatewayList.exists(value);
    }
}

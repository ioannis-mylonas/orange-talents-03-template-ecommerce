package bootcamp.mercado.validator;

import bootcamp.mercado.compra.Compra;
import bootcamp.mercado.compra.CompraStatus;
import bootcamp.mercado.gateway.response.Pagamento;
import bootcamp.mercado.gateway.response.TransacaoRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public class TransacaoIncompletaValidator implements ConstraintValidator<TransacaoIncompleta, Long> {
    private EntityManager entityManager;

    public TransacaoIncompletaValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Compra compra = entityManager.find(Compra.class, value);
        if (compra != null && compra.getStatus() == CompraStatus.SUCESSO)
            return false;

        return true;
    }
}

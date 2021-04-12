package bootcamp.mercado.validator;

import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraStatus;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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

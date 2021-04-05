package bootcamp.mercado.validator;

import bootcamp.mercado.compra.CompraRequest;
import bootcamp.mercado.produto.Produto;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class EstoqueValidator implements ConstraintValidator<Estoque, CompraRequest> {
    private final EntityManager entityManager;
    private final String tag = "[EstoqueValidator] ";

    private Class<?> target;
    private String field;

    public EstoqueValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(Estoque constraint) {
        this.target = constraint.target();
        this.field = constraint.field();
    }

    @Override
    public boolean isValid(CompraRequest value, ConstraintValidatorContext context) {
        Integer quantidade = value.getQuantidade();
        Long produtoId = value.getProdutoId();

        String q = String.format("SELECT s FROM %s s WHERE %s=:value",
                target.getName(), field);

        Query query = entityManager.createQuery(q);
        query.setParameter("value", produtoId);

        List<?> res = query.getResultList();
        Assert.isTrue(res.size() <= 1, tag + "Estado inválido: Id do produto duplicado!");

        if (res.isEmpty()) return false;
        Produto produto = (Produto) res.stream().findFirst().get();

        return produto.getQuantidade() >= quantidade;
    }
}

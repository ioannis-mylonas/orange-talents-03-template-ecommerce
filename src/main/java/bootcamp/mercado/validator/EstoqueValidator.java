package bootcamp.mercado.validator;

import bootcamp.mercado.produto.compra.CompraRequest;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

public class EstoqueValidator implements Validator {
    private ProdutoRepository produtoRepository;

    public EstoqueValidator(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public boolean supports(Class<?> target) {
        return target.isAssignableFrom(CompraRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompraRequest request = (CompraRequest) target;
        Optional<Produto> produto = produtoRepository.findById(request.getProdutoId());
        if (produto.isEmpty()) return;

        if (request.getQuantidade() > produto.get().getQuantidade())
            errors.rejectValue("quantidade",
                    "EstoqueValidator",
                    "Estoque insuficiente para compra.");
    }
}

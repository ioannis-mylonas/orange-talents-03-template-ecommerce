package bootcamp.mercado.compra;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.validator.Exists;
import bootcamp.mercado.validator.GatewayExists;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CompraRequest {
    @NotBlank @GatewayExists
    private final String gateway;
    @NotNull @Exists(target = Produto.class, field = "id")
    private final Long produtoId;
    @NotNull @Min(value = 1)
    private final Integer quantidade;

    public CompraRequest(@NotBlank String gateway,
                         @NotNull Long produtoId,
                         @NotNull @Min(value = 1) Integer quantidade) {

        this.gateway = gateway;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public String getGateway() {
        return gateway;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public Integer getQuantidade() { return quantidade; }

    public Compra converte(Usuario cliente, Produto produto) {
        return new Compra(cliente, produto, gateway, quantidade, produto.getPreco(), CompraStatus.INICIADA);
    }
}

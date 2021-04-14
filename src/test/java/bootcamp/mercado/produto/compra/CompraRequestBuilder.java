package bootcamp.mercado.produto.compra;

public class CompraRequestBuilder {
    private String gateway;
    private Long produto;
    private Integer quantidade;

    public CompraRequestBuilder setGateway(String gateway) {
        this.gateway = gateway;
        return this;
    }

    public CompraRequestBuilder setProduto(Long produto) {
        this.produto = produto;
        return this;
    }

    public CompraRequestBuilder setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public CompraRequest build() {
        return new CompraRequest(gateway, produto, quantidade);
    }
}

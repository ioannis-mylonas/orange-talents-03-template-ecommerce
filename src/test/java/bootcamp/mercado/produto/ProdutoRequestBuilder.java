package bootcamp.mercado.produto;

import bootcamp.mercado.produto.caracteristica.CaracteristicaRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProdutoRequestBuilder {
    private String nome, descricao, categoria;
    private BigDecimal preco;
    private Integer quantidade;
    private List<CaracteristicaRequest> caracteristicas = new ArrayList<>();

    public ProdutoRequestBuilder setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public ProdutoRequestBuilder setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public ProdutoRequestBuilder setCategoria(String categoria) {
        this.categoria = categoria;
        return this;
    }

    public ProdutoRequestBuilder setPreco(Double preco) {
        this.preco = BigDecimal.valueOf(preco);
        return this;
    }

    public ProdutoRequestBuilder setPreco(BigDecimal preco) {
        this.preco = preco;
        return this;
    }

    public ProdutoRequestBuilder setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public ProdutoRequestBuilder addCaracteristica(CaracteristicaRequest caracteristica) {
        this.caracteristicas.add(caracteristica);
        return this;
    }

    public ProdutoRequestBuilder addAllCaracteristica(Collection<CaracteristicaRequest> caracteristicas) {
        this.caracteristicas.addAll(caracteristicas);
        return this;
    }

    public ProdutoRequestBuilder setCaracteristicas(ArrayList<CaracteristicaRequest> caracteristicas) {
        this.caracteristicas = caracteristicas;
        return this;
    }

    public ProdutoRequest build() {
        return new ProdutoRequest(nome, preco, quantidade, caracteristicas, descricao, categoria);
    }
}

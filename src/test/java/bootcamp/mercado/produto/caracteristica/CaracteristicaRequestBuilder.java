package bootcamp.mercado.produto.caracteristica;

public class CaracteristicaRequestBuilder {
    private String nome, descricao;

    public CaracteristicaRequestBuilder setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public CaracteristicaRequestBuilder setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public CaracteristicaRequest build() {
        return new CaracteristicaRequest(nome, descricao);
    }
}

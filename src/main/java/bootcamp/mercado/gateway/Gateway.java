package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;

public abstract class Gateway {
    String nome;
    String baseUri;

    public Gateway(String nome, String baseUri) {
        this.nome = nome;
        this.baseUri = baseUri;
    }

    public boolean isGateway(String nome) {
        return nome.trim().equalsIgnoreCase(this.nome);
    }
    public abstract String gerarURI(Compra compra, String redirectUri);
}

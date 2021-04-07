package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;

public abstract class Gateway {
    protected String nome;
    protected String baseUri;

    public Gateway(String nome, String baseUri) {
        this.nome = nome;
        this.baseUri = baseUri;
    }

    public boolean isGateway(String value) {
        return value.trim().equalsIgnoreCase(nome);
    }
    public abstract String gerarURI(Compra compra, String redirectUri);

    public String getNome() {
        return nome;
    }
}

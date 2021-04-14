package bootcamp.mercado.produto.compra.gateway;

import bootcamp.mercado.produto.compra.Compra;

import java.net.URI;

public abstract class Gateway {
    protected String nome;
    protected String baseUri;
    protected String redirectPath;

    public Gateway(String nome, String baseUri, String redirectPath) {
        this.nome = nome;
        this.baseUri = baseUri;
        this.redirectPath = redirectPath;
    }

    public boolean isGateway(String value) {
        if (value == null) return false;
        return value.trim().equalsIgnoreCase(nome);
    }
    public abstract URI gerarURI(Compra compra, String redirectUri);

    public String getNome() {
        return nome;
    }

    public String getRedirectPath() { return redirectPath; }
}

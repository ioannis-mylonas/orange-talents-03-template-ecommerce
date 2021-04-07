package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;

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
        return value.trim().equalsIgnoreCase(nome);
    }
    public abstract String gerarURI(Compra compra, String redirectUri);

    public String getNome() {
        return nome;
    }

    public String getRedirectPath() { return redirectPath; }
}

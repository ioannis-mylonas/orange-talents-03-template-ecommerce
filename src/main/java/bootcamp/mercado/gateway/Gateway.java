package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;

public interface Gateway {
    Boolean isGateway(String nome);
    String gerarURI(Compra compra, String redirectUri);
}

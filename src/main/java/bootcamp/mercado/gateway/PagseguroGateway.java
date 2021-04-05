package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;
import org.springframework.web.util.UriComponentsBuilder;

public class PagseguroGateway implements Gateway {
    @Override
    public Boolean isGateway(String nome) {
        return nome.trim().equalsIgnoreCase("pagseguro");
    }

    @Override
    public String gerarURI(Compra compra, String redirectUri) {
        String uri = "pagseguro.com";
        return UriComponentsBuilder.fromUriString(uri)
                .queryParam("returnId", compra.getId())
                .queryParam("redirectUrl", redirectUri)
                .encode()
                .toUriString();
    }
}

package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PagseguroGateway extends Gateway {

    public PagseguroGateway() {
        super("Pagseguro", "pagseguro.com");
    }

    @Override
    public String gerarURI(Compra compra, String redirectUri) {
        return UriComponentsBuilder.fromUriString(baseUri)
                .queryParam("returnId", compra.getId())
                .queryParam("redirectUrl", redirectUri)
                .encode()
                .toUriString();
    }
}

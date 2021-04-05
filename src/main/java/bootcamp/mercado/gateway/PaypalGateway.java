package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;
import org.springframework.web.util.UriComponentsBuilder;

public class PaypalGateway implements Gateway {

    @Override
    public Boolean isGateway(String nome) {
        return nome.trim().equalsIgnoreCase("paypal");
    }

    @Override
    public String gerarURI(Compra compra, String redirectUri) {
        String uri = "paypal.com";
        return UriComponentsBuilder.fromUriString(uri)
                .queryParam("buyerId", compra.getId())
                .queryParam("redirectUrl", redirectUri)
                .encode()
                .toUriString();
    }
}

package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;
import org.springframework.web.util.UriComponentsBuilder;

public class PaypalGateway extends Gateway {

    public PaypalGateway() {
        super("Paypal", "paypal.com");
    }

    @Override
    public String gerarURI(Compra compra, String redirectUri) {
        return UriComponentsBuilder.fromUriString(baseUri)
                .queryParam("buyerId", compra.getId())
                .queryParam("redirectUrl", redirectUri)
                .encode()
                .toUriString();
    }
}

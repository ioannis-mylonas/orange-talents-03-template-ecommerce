package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PaypalGateway extends Gateway {
    public PaypalGateway() {
        super("Paypal",
                "paypal.com", "/pagamentos/paypal");
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

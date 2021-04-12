package bootcamp.mercado.produto.compra.gateway;

import bootcamp.mercado.produto.compra.Compra;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class PaypalGateway extends Gateway {
    public PaypalGateway() {
        super("Paypal",
                "www.paypal.com", "/pagamentos/paypal");
    }

    @Override
    public URI gerarURI(Compra compra, String redirectUri) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host(baseUri)
                .queryParam("buyerId", compra.getId())
                .queryParam("redirectUrl", redirectUri)
                .build().encode().toUri();
    }
}

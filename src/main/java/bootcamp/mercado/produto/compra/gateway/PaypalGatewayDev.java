package bootcamp.mercado.produto.compra.gateway;

import bootcamp.mercado.produto.compra.Compra;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Profile("dev")
public class PaypalGatewayDev extends Gateway {
    public PaypalGatewayDev() {
        super("Paypal",
                "localhost", "/pagamentos/paypal");
    }

    @Override
    public URI gerarURI(Compra compra, String redirectUri) {
        return UriComponentsBuilder.newInstance()
                .scheme("http").host(baseUri).port("8080").path("/paypal-endpointfake")
                .queryParam("buyerId", compra.getId())
                .queryParam("redirectUrl", redirectUri)
                .build().encode().toUri();
    }
}

package bootcamp.mercado.produto.compra.gateway;

import bootcamp.mercado.produto.compra.Compra;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Profile({"dev", "test"})
public class PagseguroGatewayDev extends Gateway {
    public PagseguroGatewayDev() {
        super("Pagseguro",
                "localhost", "/pagamentos/pagseguro");
    }

    @Override
    public URI gerarURI(Compra compra, String redirectUri) {
        return UriComponentsBuilder.newInstance()
                .scheme("http").host(baseUri).port("8080").path("/pagseguro-endpointfake")
                .queryParam("returnId", compra.getId())
                .queryParam("redirectUrl", redirectUri)
                .build().encode().toUri();
    }
}

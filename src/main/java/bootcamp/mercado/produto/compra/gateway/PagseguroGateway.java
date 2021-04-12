package bootcamp.mercado.produto.compra.gateway;

import bootcamp.mercado.produto.compra.Compra;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class PagseguroGateway extends Gateway {
    public PagseguroGateway() {
        super("Pagseguro",
                "www.pagseguro.com", "/pagamentos/pagseguro");
    }

    @Override
    public URI gerarURI(Compra compra, String redirectUri) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host(baseUri)
                .queryParam("returnId", compra.getId())
                .queryParam("redirectUrl", redirectUri)
                .build().encode().toUri();
    }
}

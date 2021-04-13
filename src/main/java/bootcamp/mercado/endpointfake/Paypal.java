package bootcamp.mercado.endpointfake;

import bootcamp.mercado.produto.compra.CompraRepository;
import bootcamp.mercado.produto.compra.gateway.response.PaypalStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/paypal-endpointfake")
public class Paypal {
    private CompraRepository compraRepository;
    private PaypalClient http;

    public Paypal(CompraRepository compraRepository,
                  PaypalClient http) {

        this.compraRepository = compraRepository;
        this.http = http;
    }

    @GetMapping()
    public ResponseEntity<?> processa(
            @RequestParam(name = "buyerId", required = true) Long buyerId,
            @RequestParam(name = "redirectUrl", required = true) String stringUri) {

        System.out.println("Paypal endpoint fake processando...");
        System.out.printf("Buyer ID: %s | redirect URL: %s\n", buyerId, stringUri);

        Random random = new Random();

        URI uri = URI.create(stringUri);
        PaypalStatus status = random.nextBoolean() ?
                PaypalStatus.SUCESSO : PaypalStatus.ERRO;

        http.send(uri,
                new PaypalResponse(
                        buyerId,
                        UUID.randomUUID(),
                        status
                ));

        return ResponseEntity.ok(String.format(
                "Pagamento processado com status %s!", status.name()
        ));
    }
}

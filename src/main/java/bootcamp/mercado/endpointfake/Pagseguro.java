package bootcamp.mercado.endpointfake;

import bootcamp.mercado.produto.compra.CompraRepository;
import bootcamp.mercado.produto.compra.gateway.response.PagseguroStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/pagseguro-endpointfake")
public class Pagseguro {
    private final PagseguroClient http;
    private final CompraRepository compraRepository;

    public Pagseguro(PagseguroClient http,
                     CompraRepository compraRepository) {

        this.http = http;
        this.compraRepository = compraRepository;
    }

    @GetMapping
    public ResponseEntity<?> processa(
            @RequestParam(name = "returnId", required = true) Long returnId,
            @RequestParam(name = "redirectUrl", required = true) String stringUri) {

        System.out.println("Pagseguro endpoint fake processando...");
        System.out.printf("Return ID: %s | redirect URL: %s\n", returnId, stringUri);

        Random random = new Random();

        URI uri = URI.create(stringUri);
        PagseguroStatus status = random.nextBoolean() ?
                PagseguroStatus.SUCESSO : PagseguroStatus.ERRO;

        http.send(uri,
                new PagseguroResponse(
                        returnId,
                        UUID.randomUUID(),
                        status
                ));

        return ResponseEntity.ok(String.format(
                "Pagamento processado com status %s!", status.name()
        ));
    }
}

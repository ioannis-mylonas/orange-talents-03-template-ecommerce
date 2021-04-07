package bootcamp.mercado.endpointfake;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/ranking")
public class VendedorRankingController {
    @PostMapping
    public void compra(@RequestBody @Valid VendedorRankingRequest request) {
        System.out.println("=========== VENDEDOR RANKING CONTROLLER ===========");
        System.out.println(request);
        System.out.println("=========== VENDEDOR RANKING CONTROLLER ===========");
    }
}

package bootcamp.mercado.endpointfake;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/nf")
public class NotaFiscalController {
    @PostMapping
    public void compra(@RequestBody @Valid NotaFiscalRequest request) {
        System.out.println("=========== NOTA FISCAL CONTROLLER ===========");
        System.out.println(request);
        System.out.println("=========== NOTA FISCAL CONTROLLER ===========");
    }
}

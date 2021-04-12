package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.gateway.PagseguroGateway;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping("/pagamentos/pagseguro")
public class PagseguroGatewayController extends PagamentoController<PagseguroRequest> {

    public PagseguroGatewayController(EntityManager entityManager,
                                      ProcessaPagamento processaPagamento,
                                      PagseguroGateway gateway) {

        super(entityManager, gateway,
                processaPagamento, "[PagseguroGatewayController] ");
    }
}

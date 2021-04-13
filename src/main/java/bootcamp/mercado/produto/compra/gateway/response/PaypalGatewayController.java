package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.gateway.GatewayList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping("/pagamentos/paypal")
public class PaypalGatewayController extends PagamentoController<PaypalRequest> {
    public PaypalGatewayController(EntityManager entityManager,
                                   GatewayList gatewayList,
                                   ProcessaPagamento processaPagamento) {

        super(entityManager, gatewayList, "Paypal",
                processaPagamento, "[PaypalGatewayController] ");
    }
}

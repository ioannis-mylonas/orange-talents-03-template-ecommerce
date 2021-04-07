package bootcamp.mercado.gateway.response;

import bootcamp.mercado.compra.Compra;
import bootcamp.mercado.email.MailSender;
import bootcamp.mercado.gateway.PagseguroGateway;
import io.jsonwebtoken.lang.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

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

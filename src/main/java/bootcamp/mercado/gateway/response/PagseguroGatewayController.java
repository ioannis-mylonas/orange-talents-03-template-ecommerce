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
public class PagseguroGatewayController {

    private EntityManager entityManager;
    private MailSender mailSender;
    private PagseguroGateway gateway;
    private static final String tag = "[PagseguroGatewayController] ";

    public PagseguroGatewayController(EntityManager entityManager, MailSender mailSender, PagseguroGateway gateway) {
        this.entityManager = entityManager;
        this.mailSender = mailSender;
        this.gateway = gateway;
    }

    @PostMapping
    @Transactional
    public void cadastra(@RequestBody @Valid PagseguroRequest request,
                         UriComponentsBuilder uriComponentsBuilder) {

        Compra compra = entityManager.find(Compra.class, request.getCompraId());
        Assert.isTrue(compra != null, tag + "Compra é null!");

        Pagamento pagamento = request.converte(gateway);

        entityManager.persist(pagamento);
        compra.mudaStatus(request.getStatus().getValue());

        if (request.getStatus() == PagseguroStatus.SUCESSO) {
            // TODO : Comunicar endpoint nota fiscal
            // TODO : Comunicar endpoint ranking vendedores
            mailSender.envia("Compra concluída com sucesso!",
                    compra.getUsuario().getLogin());

        } else if(request.getStatus() == PagseguroStatus.ERRO) {
            String redirectUri = uriComponentsBuilder
                    .path("/pagamentos/pagseguro")
                    .toUriString();

            String uri = gateway.gerarURI(compra, redirectUri);
            mailSender.envia(String.format(
                    "Compra malsucedida, tente novamente no link %s", uri
            ), compra.getUsuario().getLogin());
        }
    }
}

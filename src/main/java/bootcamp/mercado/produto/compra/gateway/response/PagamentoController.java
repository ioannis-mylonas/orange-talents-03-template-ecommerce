package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.produto.compra.gateway.Gateway;
import io.jsonwebtoken.lang.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

public abstract class PagamentoController<T extends TransacaoRequest> {
    private final EntityManager entityManager;
    private final Gateway gateway;
    private final ProcessaPagamento processaPagamento;
    private final String tag;

    public PagamentoController(EntityManager entityManager,
                               Gateway gateway,
                               ProcessaPagamento processaPagamento,
                               String tag) {

        this.entityManager = entityManager;
        this.gateway = gateway;
        this.processaPagamento = processaPagamento;
        this.tag = tag;
    }

    @PostMapping
    @Transactional
    public void processa(@RequestBody @Valid T request,
                         UriComponentsBuilder uriComponentsBuilder) {

        Compra compra = entityManager.find(Compra.class, request.getCompraId());
        Assert.isTrue(compra != null, tag + "Compra é null!");

        Pagamento pagamento = request.converte(gateway);

        if (request.getStatus() == CompraStatus.SUCESSO) {
            processaPagamento.sucesso(compra,
                    pagamento, request.getStatus(),
                    gateway, uriComponentsBuilder);

        } else if(request.getStatus() == CompraStatus.FALHA) {
            String redirectUri = uriComponentsBuilder
                    .path(gateway.getRedirectPath())
                    .toUriString();

            processaPagamento.falha(compra,
                    pagamento,
                    request.getStatus(),
                    gateway,
                    redirectUri);
        }
    }
}

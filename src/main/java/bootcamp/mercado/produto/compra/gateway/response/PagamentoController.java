package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.produto.compra.Pagamento;
import bootcamp.mercado.produto.compra.gateway.Gateway;
import bootcamp.mercado.produto.compra.gateway.GatewayList;
import io.jsonwebtoken.lang.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

public abstract class PagamentoController<T extends TransacaoRequest> {
    private final EntityManager entityManager;
    private final GatewayList gatewayList;
    private final ProcessaPagamento processaPagamento;
    private final String gatewayId;
    private final String tag;

    public PagamentoController(EntityManager entityManager,
                               GatewayList gatewayList, String gatewayId,
                               ProcessaPagamento processaPagamento,
                               String tag) {

        this.entityManager = entityManager;
        this.gatewayList = gatewayList;
        this.gatewayId = gatewayId;
        this.processaPagamento = processaPagamento;
        this.tag = tag;
    }

    @PostMapping
    @Transactional
    public void processa(@RequestBody @Valid T request,
                         UriComponentsBuilder uriComponentsBuilder) {

        Compra compra = entityManager.find(Compra.class, request.getCompraId());
        Assert.isTrue(compra != null, tag + "Compra é null!");

        Gateway gateway = gatewayList.getGateway(gatewayId).get();
        Pagamento pagamento = request.converte(gateway, compra);

        if (request.getStatus() == CompraStatus.SUCESSO) {
            processaPagamento.sucesso(compra,
                    pagamento, gateway, uriComponentsBuilder);

        } else if(request.getStatus() == CompraStatus.FALHA) {
            String redirectUri = uriComponentsBuilder
                    .path(gateway.getRedirectPath())
                    .toUriString();

            processaPagamento.falha(compra,
                    pagamento, gateway, redirectUri);
        }
    }
}

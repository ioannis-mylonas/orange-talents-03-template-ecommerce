package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.produto.compra.Pagamento;
import bootcamp.mercado.produto.compra.gateway.Gateway;
import bootcamp.mercado.validator.Exists;
import bootcamp.mercado.validator.TransacaoIncompleta;
import bootcamp.mercado.validator.Unique;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PagseguroRequest implements TransacaoRequest<PagseguroStatus> {
    @NotNull @Exists(target = Compra.class, field = "id") @TransacaoIncompleta
    private final Long compraId;
    @NotBlank @Unique(target = Pagamento.class, field = "pagamentoId")
    private final String pagamentoId;
    @NotNull
    private final PagseguroStatus status;

    public PagseguroRequest(Long compraId, @NotBlank String pagamentoId, PagseguroStatus status) {
        this.compraId = compraId;
        this.pagamentoId = pagamentoId;
        this.status = status;
    }

    public Pagamento converte(Gateway gateway, Compra compra) {
        return new Pagamento(compra,
                gateway.getNome(),
                pagamentoId,
                status.getCompraStatus());
    }

    public PagseguroStatus getStatus() {
        return status;
    }

    public Long getCompraId() {
        return compraId;
    }

    public String getPagamentoId() {
        return pagamentoId;
    }
}

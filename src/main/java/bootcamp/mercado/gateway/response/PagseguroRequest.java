package bootcamp.mercado.gateway.response;

import bootcamp.mercado.compra.Compra;
import bootcamp.mercado.compra.CompraStatus;
import bootcamp.mercado.gateway.Gateway;
import bootcamp.mercado.validator.Exists;
import bootcamp.mercado.validator.TransacaoIncompleta;
import bootcamp.mercado.validator.Unique;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PagseguroRequest implements TransacaoRequest {
    @NotNull @Exists(target = Compra.class, field = "id") @TransacaoIncompleta
    private Long compraId;
    @NotBlank @Unique(target = Pagamento.class, field = "pagamentoId")
    private String pagamentoId;
    @NotNull
    private PagseguroStatus status;

    public PagseguroRequest(Long compraId, @NotBlank String pagamentoId, PagseguroStatus status) {
        this.compraId = compraId;
        this.pagamentoId = pagamentoId;
        this.status = status;
    }

    public Pagamento converte(Gateway gateway) {
        return new Pagamento(compraId,
                gateway.getNome(),
                pagamentoId,
                status.getStatus());
    }

    public CompraStatus getStatus() {
        return status.getStatus();
    }

    public Long getCompraId() {
        return compraId;
    }
}

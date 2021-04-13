package bootcamp.mercado.endpointfake;

import bootcamp.mercado.produto.compra.gateway.response.PagseguroStatus;

import java.util.UUID;

public class PagseguroResponse {
    private final Long compraId;
    private final UUID pagamentoId;
    private final String status;

    public PagseguroResponse(Long compraId, UUID pagamentoId, PagseguroStatus status) {
        this.compraId = compraId;
        this.pagamentoId = pagamentoId;
        this.status = status.name();
    }

    public Long getCompraId() {
        return compraId;
    }

    public UUID getPagamentoId() {
        return pagamentoId;
    }

    public String getStatus() {
        return status;
    }
}

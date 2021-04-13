package bootcamp.mercado.endpointfake;

import bootcamp.mercado.produto.compra.gateway.response.PaypalStatus;

import java.util.UUID;

public class PaypalResponse {
    private final Long compraId;
    private final UUID pagamentoId;
    private final Integer status;

    public PaypalResponse(Long compraId, UUID pagamentoId, PaypalStatus status) {
        this.compraId = compraId;
        this.pagamentoId = pagamentoId;
        this.status = status.getCode();
    }

    public Long getCompraId() {
        return compraId;
    }

    public UUID getPagamentoId() {
        return pagamentoId;
    }

    public Integer getStatus() {
        return status;
    }
}

package bootcamp.mercado.gateway.response;

import bootcamp.mercado.compra.CompraStatus;
import bootcamp.mercado.gateway.Gateway;

public interface TransacaoRequest {
    Long getCompraId();
    Pagamento converte(Gateway gateway);
    CompraStatus getStatus();
}

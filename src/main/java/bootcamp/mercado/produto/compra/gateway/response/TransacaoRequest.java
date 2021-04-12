package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.produto.compra.gateway.Gateway;

public interface TransacaoRequest {
    Long getCompraId();
    Pagamento converte(Gateway gateway);
    CompraStatus getStatus();
}

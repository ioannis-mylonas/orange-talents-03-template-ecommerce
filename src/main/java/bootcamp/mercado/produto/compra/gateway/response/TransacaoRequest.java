package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.produto.compra.Pagamento;
import bootcamp.mercado.produto.compra.gateway.Gateway;

public interface TransacaoRequest<T extends TransacaoStatus> {
    Long getCompraId();
    Pagamento converte(Gateway gateway, Compra compra);
    T getStatus();
}

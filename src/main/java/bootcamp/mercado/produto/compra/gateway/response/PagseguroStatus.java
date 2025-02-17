package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.CompraStatus;

public enum PagseguroStatus implements TransacaoStatus {
    SUCESSO(CompraStatus.SUCESSO),
    ERRO(CompraStatus.FALHA);

    private CompraStatus value;

    PagseguroStatus(CompraStatus value) {
        this.value = value;
    }

    public CompraStatus getCompraStatus() {
        return value;
    }
}

package bootcamp.mercado.gateway.response;

import bootcamp.mercado.compra.CompraStatus;

public enum PagseguroStatus {
    SUCESSO(CompraStatus.SUCESSO),
    ERRO(CompraStatus.FALHA);

    private CompraStatus value;

    PagseguroStatus(CompraStatus value) {
        this.value = value;
    }

    public CompraStatus getStatus() {
        return value;
    }
}

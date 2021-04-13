package bootcamp.mercado.produto.compra.gateway.response;

import javax.validation.constraints.NotNull;

public class NotaFiscalRequest {
    private Long idCompra;
    private Long idUsuario;

    public NotaFiscalRequest(@NotNull Long idCompra, @NotNull Long idUsuario) {
        this.idCompra = idCompra;
        this.idUsuario = idUsuario;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }
}

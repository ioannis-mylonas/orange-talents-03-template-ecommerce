package bootcamp.mercado.produto.compra.gateway.response;

public class NotaFiscalRequest {
    private final Long idCompra;
    private final Long idUsuario;

    public NotaFiscalRequest(Long idCompra, Long idUsuario) {
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

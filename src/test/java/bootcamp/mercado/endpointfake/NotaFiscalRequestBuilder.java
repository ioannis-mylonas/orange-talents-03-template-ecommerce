package bootcamp.mercado.endpointfake;

public class NotaFiscalRequestBuilder {
    Long compra, usuario;

    public NotaFiscalRequestBuilder setCompra(Long compra) {
        this.compra = compra;
        return this;
    }

    public NotaFiscalRequestBuilder setUsuario(Long usuario) {
        this.usuario = usuario;
        return this;
    }

    public NotaFiscalRequest build() {
        return new NotaFiscalRequest(compra, usuario);
    }
}

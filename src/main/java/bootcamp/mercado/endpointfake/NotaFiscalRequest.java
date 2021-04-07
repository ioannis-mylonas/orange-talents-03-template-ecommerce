package bootcamp.mercado.endpointfake;

import bootcamp.mercado.compra.Compra;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.validator.Exists;

import javax.validation.constraints.NotNull;

public class NotaFiscalRequest {
    @NotNull @Exists(target = Compra.class, field = "id")
    private Long idCompra;
    @NotNull @Exists(target = Usuario.class, field = "id")
    private Long idUsuario;

    public NotaFiscalRequest(@NotNull Long idCompra, @NotNull Long idUsuario) {
        this.idCompra = idCompra;
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "NotaFiscalRequest{" +
                "idCompra=" + idCompra +
                ", idUsuario=" + idUsuario +
                '}';
    }
}

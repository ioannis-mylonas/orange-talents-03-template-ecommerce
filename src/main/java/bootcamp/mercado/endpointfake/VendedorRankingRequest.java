package bootcamp.mercado.endpointfake;

import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.validator.Exists;
import bootcamp.mercado.validator.GatewayExists;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VendedorRankingRequest {
    @NotNull @Exists(target = Compra.class, field = "id")
    private Long idCompra;
    @NotBlank @GatewayExists
    private String gateway;

    public VendedorRankingRequest(@NotNull Long idCompra, @NotBlank String gateway) {
        this.idCompra = idCompra;
        this.gateway = gateway;
    }

    @Override
    public String toString() {
        return "VendedorRankingRequest{" +
                "idCompra=" + idCompra +
                ", gateway='" + gateway + '\'' +
                '}';
    }
}

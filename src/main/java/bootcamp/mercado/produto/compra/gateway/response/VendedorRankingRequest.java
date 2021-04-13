package bootcamp.mercado.produto.compra.gateway.response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VendedorRankingRequest {
    private final Long idCompra;
    private final String gateway;

    public VendedorRankingRequest(@NotNull Long idCompra, @NotBlank String gateway) {
        this.idCompra = idCompra;
        this.gateway = gateway;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public String getGateway() {
        return gateway;
    }
}

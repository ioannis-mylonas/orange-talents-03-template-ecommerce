package bootcamp.mercado.gateway;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class GatewayList {
    private Collection<Gateway> gateways;

    public GatewayList(Collection<Gateway> gateways) {
        this.gateways = gateways;
    }

    public Gateway getGateway(String nome) {
        for (Gateway gateway : gateways) {
            if (gateway.isGateway(nome)) return gateway;
        }
        return null;
    }

    public Boolean exists(String nome) {
        for (Gateway gateway : gateways) {
            if (gateway.isGateway(nome)) return true;
        }
        return false;
    }
}

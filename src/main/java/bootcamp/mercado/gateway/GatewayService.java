package bootcamp.mercado.gateway;

import bootcamp.mercado.compra.Compra;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GatewayService {
    List<Gateway> gateways = new ArrayList<>(Arrays.asList(
            new PaypalGateway(),
            new PagseguroGateway()
    ));

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

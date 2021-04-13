package bootcamp.mercado.produto.compra.gateway.response;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

@FeignClient(name = "VendedorRankingFeignClient", url = "localhost")
public interface VendedorRankingClient {
    @RequestMapping(method = RequestMethod.POST)
    void send(URI uri, @RequestBody VendedorRankingRequest request);
}

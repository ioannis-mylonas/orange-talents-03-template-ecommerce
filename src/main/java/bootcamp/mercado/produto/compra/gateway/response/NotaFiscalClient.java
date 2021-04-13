package bootcamp.mercado.produto.compra.gateway.response;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

@FeignClient(name = "NotaFiscalFeignClient", url = "localhost")
public interface NotaFiscalClient {
    @RequestMapping(method = RequestMethod.POST)
    void send(URI uri, @RequestBody NotaFiscalRequest request);
}

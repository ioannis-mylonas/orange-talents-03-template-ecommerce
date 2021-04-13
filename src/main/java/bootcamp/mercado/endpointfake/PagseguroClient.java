package bootcamp.mercado.endpointfake;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

@FeignClient(name = "PagseguroFeignClient", url = "localhost")
public interface PagseguroClient {
    @RequestMapping(method = RequestMethod.POST)
    void send(URI baseUri, @RequestBody PagseguroResponse response);
}

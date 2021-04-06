package bootcamp.mercado.compra;

import bootcamp.mercado.email.MailSender;
import bootcamp.mercado.gateway.Gateway;
import bootcamp.mercado.gateway.GatewayList;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.usuario.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/compras")
public class CompraController {
    private final ProdutoRepository produtoRepository;
    private final CompraRepository compraRepository;
    private final MailSender mailSender;
    private final GatewayList gatewayList;

    private final String tag = "[CompraController] ";

    public CompraController(ProdutoRepository produtoRepository,
                            CompraRepository compraRepository,
                            MailSender mailSender,
                            GatewayList gatewayList) {

        this.produtoRepository = produtoRepository;
        this.compraRepository = compraRepository;
        this.mailSender = mailSender;
        this.gatewayList = gatewayList;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> comprar(@RequestBody @Valid CompraRequest request,
                                          UriComponentsBuilder uriComponentsBuilder,
                                          @AuthenticationPrincipal Usuario usuario) {

        Assert.isTrue(usuario != null, tag + "Usuario null!");

        Optional<Produto> produto = produtoRepository.findById(request.getProdutoId());
        Assert.isTrue(produto.isPresent(), tag + "Produto inexistente!");

        Gateway gateway = gatewayList.getGateway(request.getGateway());
        Assert.isTrue(gateway != null, tag + "Gateway inexistente!");

        Compra compra = request.converte(usuario, produto.get());
        compraRepository.save(compra);

        produto.get().diminuirQuantidade(compra.getQuantidade());

        String returnUrl = uriComponentsBuilder
                .path("/").toUriString();

        String targetUrl = gateway.gerarURI(compra, returnUrl);

        mailSender.envia("Compra efetuada!", compra.getUsuario().getLogin());
        return ResponseEntity.status(HttpStatus.FOUND).body(targetUrl);
    }
}

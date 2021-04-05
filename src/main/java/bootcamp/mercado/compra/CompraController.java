package bootcamp.mercado.compra;

import bootcamp.mercado.config.AutenticacaoService;
import bootcamp.mercado.email.EmailService;
import bootcamp.mercado.gateway.Gateway;
import bootcamp.mercado.gateway.GatewayService;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.usuario.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/compras")
public class CompraController {
    private final ProdutoRepository produtoRepository;
    private final CompraRepository compraRepository;
    private final GatewayService gatewayService;
    private final EmailService emailService;

    private final String tag = "[CompraController] ";

    public CompraController(ProdutoRepository produtoRepository,
                            CompraRepository compraRepository,
                            GatewayService gatewayService,
                            EmailService emailService) {

        this.produtoRepository = produtoRepository;
        this.compraRepository = compraRepository;
        this.gatewayService = gatewayService;
        this.emailService = emailService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> comprar(@RequestBody @Valid CompraRequest request,
                                          UriComponentsBuilder uriComponentsBuilder,
                                          @AuthenticationPrincipal Usuario usuario) {

        Assert.isTrue(usuario != null, tag + "Usuario null!");

        Optional<Produto> produto = produtoRepository.findById(request.getProdutoId());
        Assert.isTrue(produto.isPresent(), tag + "Produto inexistente!");

        Gateway gateway = gatewayService.getGateway(request.getGateway());
        Assert.isTrue(gateway != null, tag + "Gateway inexistente!");

        Compra compra = request.converte(usuario, produto.get());
        compraRepository.save(compra);

        produto.get().diminuirQuantidade(compra.getQuantidade());

        String returnUrl = uriComponentsBuilder
                .path("/").toUriString();

        String targetUrl = gateway.gerarURI(compra, returnUrl);

        emailService.envia("Compra efetuada!", compra.getUsuario().getLogin());
        return ResponseEntity.status(HttpStatus.FOUND).body(targetUrl);
    }
}

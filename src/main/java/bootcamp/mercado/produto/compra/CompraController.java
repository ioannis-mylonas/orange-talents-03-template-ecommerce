package bootcamp.mercado.produto.compra;

import bootcamp.mercado.email.MailSender;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.produto.compra.gateway.Gateway;
import bootcamp.mercado.produto.compra.gateway.GatewayList;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.validator.EstoqueValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
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
    public ResponseEntity<?> comprar(@RequestBody @Valid CompraRequest request,
                                          UriComponentsBuilder uriComponentsBuilder,
                                          @AuthenticationPrincipal Usuario usuario) {

        Assert.isTrue(usuario != null, tag + "Usuario null!");

        Optional<Produto> produto = produtoRepository.findById(request.getProdutoId());
        Assert.isTrue(produto.isPresent(), tag + "Produto inexistente!");

        Optional<Gateway> gateway = gatewayList.getGateway(request.getGateway());
        Assert.isTrue(gateway.isPresent(), tag + "Gateway inexistente!");

        Compra compra = request.converte(usuario, produto.get());
        compraRepository.save(compra);

        produto.get().diminuirQuantidade(compra.getQuantidade());

        String returnUrl = uriComponentsBuilder
                .path(gateway.get().getRedirectPath()).toUriString();

        URI targetUrl = gateway.get().gerarURI(compra, returnUrl);

        mailSender.envia("Compra efetuada!", compra.getUsuario().getLogin());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(targetUrl)
                .build();
    }

    @InitBinder
    public void addValidators(WebDataBinder binder) {
        binder.addValidators(
                new EstoqueValidator(produtoRepository)
        );
    }
}

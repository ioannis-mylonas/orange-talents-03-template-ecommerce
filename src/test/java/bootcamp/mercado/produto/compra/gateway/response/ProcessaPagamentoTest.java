package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.email.MailSenderDev;
import bootcamp.mercado.email.MercadoMailSender;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.produto.ProdutoRequestBuilder;
import bootcamp.mercado.produto.categoria.Categoria;
import bootcamp.mercado.produto.categoria.CategoriaRepository;
import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraRepository;
import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.produto.compra.Pagamento;
import bootcamp.mercado.produto.compra.gateway.PagseguroGatewayDev;
import bootcamp.mercado.produto.compra.gateway.PaypalGatewayDev;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Rollback
class ProcessaPagamentoTest {
    @Autowired CategoriaRepository categoriaRepository;
    @Autowired CompraRepository compraRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Mock MercadoMailSender mailSender;

    @Mock PagseguroGatewayDev pagseguro;
    @Mock PaypalGatewayDev paypal;

    @PersistenceContext EntityManager entityManager;
    private ProcessaPagamento processaPagamento;

    @Mock NotaFiscalClient nfClient;
    @Mock VendedorRankingClient vrClient;
    @Mock UriComponentsBuilder uriComponentsBuilder;

    private Usuario usuario;
    private Produto produto;

    @BeforeEach
    public void setup() {
        usuario = new UsuarioRequestBuilder()
                .setNome("usuario@usuario.com")
                .setSenha("123456").build().converte(passwordEncoder);

        entityManager.persist(usuario);

        Categoria categoria = new Categoria("Categoria");

        entityManager.persist(categoria);

        produto = new ProdutoRequestBuilder()
                .setNome("Produto")
                .setDescricao("Descrição")
                .setCategoria("Categoria")
                .setPreco(BigDecimal.valueOf(20.13))
                .setQuantidade(500).build()
                .converte(categoriaRepository, usuario);

        entityManager.persist(produto);

        processaPagamento = new ProcessaPagamento(
                compraRepository, mailSender, nfClient, vrClient);

        UriComponents mockComponents = Mockito.mock(UriComponents.class);
        URI uri = URI.create("");

        Mockito.when(
                uriComponentsBuilder.replacePath(Mockito.anyString())
        ).thenReturn(uriComponentsBuilder);

        Mockito.when(
                uriComponentsBuilder.build()
        ).thenReturn(mockComponents);

        Mockito.when(
                mockComponents.toUri()
        ).thenReturn(uri);
    }

    @Test
    public void testaPagamentoValidoPagseguro() {
        Compra compra = new Compra(usuario,
                produto, "Pagseguro",
                1, BigDecimal.valueOf(20.13),
                CompraStatus.INICIADA);

        Pagamento pagamento = new Pagamento(
                compra, "Pagseguro",
                "Pagamento ID", CompraStatus.SUCESSO
        );

        processaPagamento.sucesso(compra, pagamento, pagseguro, uriComponentsBuilder);

        Compra resultado = entityManager.find(Compra.class, compra.getId());
        assertEquals(resultado.getStatus(), CompraStatus.SUCESSO);
    }

    @Test
    public void testaPagamentoValidoPaypal() {
        Compra compra = new Compra(usuario,
                produto, "Paypal",
                1, BigDecimal.valueOf(20.13),
                CompraStatus.INICIADA);

        Pagamento pagamento = new Pagamento(
                compra, "Paypal",
                "Paypal ID", CompraStatus.SUCESSO
        );

        processaPagamento.sucesso(compra, pagamento, paypal, uriComponentsBuilder);

        Compra resultado = entityManager.find(Compra.class, compra.getId());
        assertEquals(resultado.getStatus(), CompraStatus.SUCESSO);
    }

    @Test
    public void testaPagamentoInvalidoPagseguro() {
        Compra compra = new Compra(usuario,
                produto, "Pagseguro",
                1, BigDecimal.valueOf(20.13),
                CompraStatus.INICIADA);

        Pagamento pagamento = new Pagamento(
                compra, "Pagseguro",
                "Pagseguro ID", CompraStatus.FALHA
        );

        processaPagamento.falha(compra, pagamento, pagseguro, "");

        Compra resultado = entityManager.find(Compra.class, compra.getId());
        assertEquals(resultado.getStatus(), CompraStatus.INICIADA);
    }

    @Test
    public void testaPagamentoInvalidoPaypal() {
        Compra compra = new Compra(usuario,
                produto, "Paypal",
                1, BigDecimal.valueOf(20.13),
                CompraStatus.INICIADA);

        Pagamento pagamento = new Pagamento(
                compra, "Paypal",
                "Paypal ID", CompraStatus.FALHA
        );

        processaPagamento.falha(compra, pagamento, paypal, "");

        Compra resultado = entityManager.find(Compra.class, compra.getId());
        assertEquals(resultado.getStatus(), CompraStatus.INICIADA);
    }
}
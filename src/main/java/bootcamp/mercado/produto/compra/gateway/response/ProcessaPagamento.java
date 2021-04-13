package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.email.MercadoMailSender;
import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.produto.compra.gateway.Gateway;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import java.net.URI;

@Component
public class ProcessaPagamento {
    private final EntityManager entityManager;
    private final MercadoMailSender mailSender;
    private final NotaFiscalClient nfClient;
    private final VendedorRankingClient vrClient;

    public ProcessaPagamento(EntityManager entityManager,
                             MercadoMailSender mailSender,
                             NotaFiscalClient nfClient,
                             VendedorRankingClient vrClient) {

        this.entityManager = entityManager;
        this.mailSender = mailSender;
        this.nfClient = nfClient;
        this.vrClient = vrClient;
    }

    private void notaFiscal(Compra compra,
                            Pagamento pagamento,
                            UriComponentsBuilder uriBuilder) {

        URI uri = uriBuilder.replacePath("/nf").build().toUri();

        NotaFiscalRequest request = new NotaFiscalRequest(
                compra.getId(),
                compra.getUsuario().getId()
        );

        nfClient.send(uri, request);
    }

    private void rankingVendedores(Compra compra,
                                   Gateway gateway,
                                   UriComponentsBuilder uriBuilder) {
        URI uri = uriBuilder.replacePath("/ranking").build().toUri();

        VendedorRankingRequest request = new VendedorRankingRequest(
                compra.getId(),
                gateway.getNome()
        );

        vrClient.send(uri, request);
    }

    private void emailSucesso(Compra compra) {
        String mensagem = String.format("Compra de ID %s efetuada com sucesso!",
                compra.getId());

        String endereco = compra.getUsuario().getLogin();
        mailSender.envia(mensagem, "Pagamento efetuado com sucesso!", endereco);
    }

    private void emailFalha(Compra compra, Gateway gateway, String redirectUri) {
        URI uri = gateway.gerarURI(compra, redirectUri);
        mailSender.envia(String.format(
                "Compra malsucedida, tente novamente no link %s", uri
        ), "Pagamento inválido!", compra.getUsuario().getLogin());
    }

    private void save(Pagamento pagamento, Compra compra, CompraStatus status) {
        entityManager.persist(pagamento);
        compra.mudaStatus(status);
    }

    public void sucesso(Compra compra,
                        Pagamento pagamento,
                        CompraStatus status,
                        Gateway gateway,
                        UriComponentsBuilder uriBuilder) {

        save(pagamento, compra, status);

        notaFiscal(compra, pagamento, uriBuilder);
        rankingVendedores(compra, gateway, uriBuilder);
        emailSucesso(compra);
    }

    public void falha(Compra compra,
                      Pagamento pagamento,
                      CompraStatus status,
                      Gateway gateway,
                      String redirectUri) {

        save(pagamento, compra, status);
        emailFalha(compra, gateway, redirectUri);
    }
}

package bootcamp.mercado.gateway.response;

import bootcamp.mercado.compra.Compra;
import bootcamp.mercado.compra.CompraStatus;
import bootcamp.mercado.email.MailSender;
import bootcamp.mercado.gateway.Gateway;
import bootcamp.mercado.http.HttpRequestSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class ProcessaPagamento {
    private final EntityManager entityManager;
    private final MailSender mailSender;
    private final HttpRequestSender httpRequestSender;

    public ProcessaPagamento(EntityManager entityManager,
                             MailSender mailSender,
                             HttpRequestSender httpRequestSender) {
        this.entityManager = entityManager;
        this.mailSender = mailSender;
        this.httpRequestSender = httpRequestSender;
    }

    private void notaFiscal(Compra compra,
                            Pagamento pagamento,
                            UriComponentsBuilder uriBuilder) {

        URI uri = uriBuilder.replacePath("/nf").build().toUri();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("idCompra", compra.getId());
        node.put("idUsuario", compra.getUsuario().getId());

        try {
            String json = mapper.writeValueAsString(node);
            HttpResponse<String> response = httpRequestSender.postJson(json, uri);
            System.out.println("NF body: " + response.body());

            // Faria algo aqui se o status retornado não fosse 200...
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void rankingVendedores(Compra compra,
                                   Gateway gateway,
                                   UriComponentsBuilder uriBuilder) {
        URI uri = uriBuilder.replacePath("/ranking").build().toUri();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("idCompra", compra.getId());
        node.put("gateway", gateway.getNome());

        try {
            String json = mapper.writeValueAsString(node);
            HttpResponse<String> response = httpRequestSender.postJson(json, uri);
            System.out.println("Ranking body: " + response.body());

            // Faria algo se status fosse diferente de 200...
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void emailSucesso(Compra compra) {
        String mensagem = String.format("Compra de ID %s efetuada com sucesso!",
                compra.getId());

        String endereco = compra.getUsuario().getLogin();
        mailSender.envia(mensagem, endereco);
    }

    private void emailFalha(Compra compra, Gateway gateway, String redirectUri) {
        String uri = gateway.gerarURI(compra, redirectUri);
        mailSender.envia(String.format(
                "Compra malsucedida, tente novamente no link %s", uri
        ), compra.getUsuario().getLogin());
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

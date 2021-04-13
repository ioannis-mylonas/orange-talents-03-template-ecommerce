package bootcamp.mercado.email;

public interface MercadoMailSender {
    void envia(String mensagem, String assunto, String destinatario);
}

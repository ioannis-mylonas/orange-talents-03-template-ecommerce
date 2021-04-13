package bootcamp.mercado.email;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MailSenderDev implements MercadoMailSender {
	private void printLinha() {
		System.out.println("==================================");
	}

	@Override
	public void envia(String mensagem, String assunto, String destinatario) {
		printLinha();
		System.out.println(String.format(
				"Mensagem: %s | Enviada para: %s",
				mensagem, destinatario));
		printLinha();
	}
}

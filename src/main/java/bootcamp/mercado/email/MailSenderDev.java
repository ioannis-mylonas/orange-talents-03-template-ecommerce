package bootcamp.mercado.email;

import org.springframework.stereotype.Service;

@Service
public class MailSenderDev implements MailSender {

	private void printLinha() {
		System.out.println("==================================");
	}

	@Override
	public void envia(String mensagem, String destinatario) {
		printLinha();
		System.out.println(String.format(
				"Mensagem: %s | Enviada para: %s",
				mensagem, destinatario));
		printLinha();
	}
}

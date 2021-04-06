package bootcamp.mercado.email;

import org.springframework.stereotype.Service;

@Service
public class MailSenderDev implements MailSender {

	@Override
	public void envia(String mensagem, String destinatario) {
		System.out.println(String.format(
				"Mensagem: %s | Enviada para: %s",
				mensagem, destinatario));
	}
}

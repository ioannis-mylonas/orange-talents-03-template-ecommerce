package bootcamp.mercado.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Profile("release")
public class MailSenderRelease implements MercadoMailSender {
    @Value("${spring.mail.username}")
    private String username;
    private JavaMailSender mailSender;

    public MailSenderRelease(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void envia(String mensagem, String assunto, String destinatario) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(username);
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(mensagem);

        mailSender.send(message);
    }
}

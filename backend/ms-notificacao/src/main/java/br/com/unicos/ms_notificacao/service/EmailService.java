package br.com.unicos.ms_notificacao.service;

import br.com.unicos.ms_notificacao.exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender enviadorEmail;
    private static final String EMAIL_ORIGEM = "gustavo.assalin@hotmail.com";
    private static final String NOME_ENVIADOR = "Gustavo S Assalin";

    @Async
    public void enviarEmail(List<String> destinatario, String assunto, String mensagem) {
        for(String emailUsuario : destinatario) {
            MimeMessage message = enviadorEmail.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            try {
                helper.setFrom(EMAIL_ORIGEM, NOME_ENVIADOR);
                helper.setTo(emailUsuario);
                helper.setSubject(assunto);
                helper.setText(mensagem, true);
            } catch(MessagingException | UnsupportedEncodingException e){
                throw new EmailException("Erro ao enviar email");
            }

            enviadorEmail.send(message);
        }
    }

}

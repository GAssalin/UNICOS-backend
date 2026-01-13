package br.com.unicos.ms_notificacao.controller;

import br.com.unicos.ms_notificacao.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/notificacao")
@RequiredArgsConstructor
public class NotificacaoInternalController {

    private final EmailService emailService;

    @PostMapping("/sendEmail")
    public void enviarEmail(
            @RequestParam List<String> destinatario,
            @RequestParam String assunto,
            @RequestParam String mensagem) {
        emailService.enviarEmail(destinatario, assunto, mensagem);
    }

}

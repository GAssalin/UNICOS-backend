package br.com.unicos.ms_auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "ms-notificacao",
        contextId = "NotificacaoClient"
)
public interface EmailClient {

    @PostMapping("/internal/notificacao/sendEmail")
    void enviarEmail(@RequestParam List<String> destinatario,
                     @RequestParam String assunto,
                     @RequestParam String mensagem);

}

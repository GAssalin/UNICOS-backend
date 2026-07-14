package br.com.unicos.ms_autenticacao.client;

import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ms-pessoa",
        contextId = "pessoaClient",
        configuration = FeignConfig.class
)
public interface PessoaClient {
    @GetMapping("/internal/pessoas/buscarPorEmail")
    UsuarioAuthResponse buscarPorEmail(@RequestParam String email);
}

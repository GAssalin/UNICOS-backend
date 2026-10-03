package br.com.unicos.ms_autenticacao.service;

import br.com.unicos.core.usuario.dto.UsuarioAuthResponse;
import br.com.unicos.ms_autenticacao.client.PessoaClient;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {

    private final PessoaClient pessoaClient;

    @CircuitBreaker(name = "ms-pessoas", fallbackMethod = "fallbackBuscarPorEmail")
    public UsuarioAuthResponse buscarUsuarioPorEmail(String email) {
        return pessoaClient.buscarPorEmail(email);
    }

    @CircuitBreaker(name = "ms-pessoas", fallbackMethod = "fallbackBuscarPorId")
    public UsuarioAuthResponse buscarUsuarioPorId(Long id) {
        return pessoaClient.buscarPorId(id);
    }

    private UsuarioAuthResponse fallbackBuscarPorEmail(String email, Throwable ex) {
        return tratarFalha(ex);
    }

    private UsuarioAuthResponse fallbackBuscarPorId(Long id, Throwable ex) {
        return tratarFalha(ex);
    }

    /**
     * Erros 4xx (usuário inexistente, por exemplo) são repassados ao chamador; apenas falhas
     * de comunicação viram 503.
     */
    private UsuarioAuthResponse tratarFalha(Throwable ex) {
        if (ex instanceof FeignException.FeignClientException clientException)
            throw clientException;

        log.error("Falha ao consultar o ms-pessoas durante a autenticação. Causa: {}", ex.getMessage(), ex);

        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de busca do usuário temporariamente indisponível");
    }
}

package br.com.unicos.ms_pessoas.client;

import br.com.unicos.ms_pessoas.usuario.dto.permissao.RoleResumoResponse;
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
public class PermissaoService {

    private static final String MENSAGEM_INDISPONIVEL = "Serviço de busca da permissão temporariamente indisponível";

    private final PermissaoClient permissaoClient;

    @CircuitBreaker(name = "ms-permissao", fallbackMethod = "fallbackUsuarioPossuiPermissao")
    public boolean usuarioPossuiPermissao(String nomePermissao) {
        return permissaoClient.usuarioPossuiPermissao(nomePermissao);
    }

    @CircuitBreaker(name = "ms-permissao", fallbackMethod = "fallbackBuscarRolePorId")
    public RoleResumoResponse buscarRolePorId(Long id) {
        return permissaoClient.buscarRolePorId(id);
    }

    private boolean fallbackUsuarioPossuiPermissao(String nomePermissao, Throwable ex) {
        log.error("Falha ao verificar a permissão [{}] no ms-permissao. Causa: {}", nomePermissao, ex.getMessage(), ex);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, MENSAGEM_INDISPONIVEL);
    }

    /**
     * Erros 4xx (role inexistente, por exemplo) são repassados ao chamador; apenas falhas
     * de comunicação viram 503.
     */
    private RoleResumoResponse fallbackBuscarRolePorId(Long id, Throwable ex) {
        if (ex instanceof FeignException.FeignClientException clientException)
            throw clientException;

        log.error("Falha ao buscar a role [{}] no ms-permissao. Causa: {}", id, ex.getMessage(), ex);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, MENSAGEM_INDISPONIVEL);
    }
}

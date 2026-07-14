package br.com.unicos.ms_permissao.client;

import br.com.unicos.core.usuario.auth.dto.UsuarioRoleIdsResponse;
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
    private final UsuarioClient usuarioClient;

    @CircuitBreaker(name = "ms-usuario", fallbackMethod = "fallbackBuscarRoleIdsDoUsuario")
    public UsuarioRoleIdsResponse buscarRoleIdsDoUsuario(Long usuarioId) {
        return usuarioClient.buscarRoleDoUsuario(usuarioId);
    }
    private UsuarioRoleIdsResponse fallbackBuscarRoleIdsDoUsuario(Long usuarioId, Throwable ex) {
        log.error(
                "Fallback do CircuitBreaker acionado ao buscar role ids do usuário [{}]. Causa: {}",
                usuarioId,
                ex.getMessage(),
                ex
        );

        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de busca do usuário temporariamente indisponível");
    }
}

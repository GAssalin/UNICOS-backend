package br.com.unicos.ms_permissao.client;

import br.com.unicos.core.usuario.dto.UsuarioRoleIdsResponse;
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

    private final UsuarioClient usuarioClient;

    @CircuitBreaker(name = "ms-pessoas", fallbackMethod = "fallbackBuscarRoleIdsDoUsuario")
    public UsuarioRoleIdsResponse buscarRoleIdsDoUsuario(Long usuarioId) {
        return usuarioClient.buscarRoleDoUsuario(usuarioId);
    }

    /**
     * Usuário inexistente na empresa (removido após emitir o token, por exemplo) não possui role:
     * o resultado é a ausência de permissões, e não indisponibilidade do serviço.
     */
    private UsuarioRoleIdsResponse fallbackBuscarRoleIdsDoUsuario(Long usuarioId, Throwable ex) {
        if (ex instanceof FeignException.NotFound)
            return null;

        log.error(
                "Falha ao buscar a role do usuário [{}] no ms-pessoas. Causa: {}",
                usuarioId,
                ex.getMessage(),
                ex
        );

        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de busca do usuário temporariamente indisponível");
    }
}

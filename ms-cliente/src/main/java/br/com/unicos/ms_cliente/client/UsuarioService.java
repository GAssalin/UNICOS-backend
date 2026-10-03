package br.com.unicos.ms_cliente.client;

import br.com.unicos.core.usuario.dto.UsuarioResumoResponse;
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
    public UsuarioRoleIdsResponse buscarRoleIdsDoUsuario(Long idUsuario) {
        return usuarioClient.buscarRoleIdsDoUsuario(idUsuario);
    }

    /**
     * Nome de exibição do usuário. Informação acessória: falhas na consulta não impedem a
     * resposta principal e resultam em {@code null}.
     */
    @CircuitBreaker(name = "ms-pessoas", fallbackMethod = "fallbackBuscarNome")
    public String buscarNome(Long idUsuario) {
        UsuarioResumoResponse resumo = usuarioClient.buscarResumo(idUsuario);
        return resumo != null ? resumo.nome() : null;
    }

    private UsuarioRoleIdsResponse fallbackBuscarRoleIdsDoUsuario(Long idUsuario, Throwable ex) {
        if (ex instanceof FeignException.FeignClientException clientException)
            throw clientException;

        log.error("Falha ao buscar a role do usuário [{}] no ms-pessoas. Causa: {}", idUsuario, ex.getMessage(), ex);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de busca da role temporariamente indisponível");
    }

    private String fallbackBuscarNome(Long idUsuario, Throwable ex) {
        log.warn("Não foi possível obter o nome do usuário [{}] no ms-pessoas: {}", idUsuario, ex.getMessage());
        return null;
    }
}

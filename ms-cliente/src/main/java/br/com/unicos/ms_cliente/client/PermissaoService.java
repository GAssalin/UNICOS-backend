package br.com.unicos.ms_cliente.client;

import br.com.unicos.ms_cliente.dto.internal.RoleResumoResponse;
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

    private final PermissaoClient permissaoClient;

    @CircuitBreaker(name = "ms-permissao", fallbackMethod = "fallbackUsuarioPossuiPermissao")
    public boolean usuarioPossuiPermissao(String nomePermissao) {
        return permissaoClient.usuarioPossuiPermissao(nomePermissao);
    }

    @CircuitBreaker(name = "ms-permissao", fallbackMethod = "fallbackBuscarNomeRoleById")
    public RoleResumoResponse buscarNomeRoleById(Long idRole) {
        return permissaoClient.buscarNomeRoleById(idRole);
    }

    private boolean fallbackUsuarioPossuiPermissao(String nomePermissao, Throwable ex) {
        log.error("Falha ao verificar a permissão [{}] no ms-permissao. Causa: {}", nomePermissao, ex.getMessage(), ex);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de busca da permissão temporariamente indisponível");
    }

    private RoleResumoResponse fallbackBuscarNomeRoleById(Long idRole, Throwable ex) {
        if (ex instanceof FeignException.FeignClientException clientException)
            throw clientException;

        log.error("Falha ao buscar a role [{}] no ms-permissao. Causa: {}", idRole, ex.getMessage(), ex);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de busca da role temporariamente indisponível");
    }
}

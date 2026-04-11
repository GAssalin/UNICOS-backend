package br.com.unicos.ms_usuario.service;

import br.com.unicos.ms_usuario.client.PermissaoClient;
import br.com.unicos.ms_usuario.dto.permissao.RoleResumoResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleGateway {

    private final PermissaoClient permissaoClient;

    @CircuitBreaker(name = "ms-permissao-role", fallbackMethod = "fallbackBuscarRolePorId")
    public RoleResumoResponse buscarRolePorId(Long roleId) {
        try {
            RoleResumoResponse role = permissaoClient.buscarRolePorId(roleId);

            if (role == null || role.id() == null) {
                throw new EntityNotFoundException("Role não encontrada: " + roleId);
            }

            return role;
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("Role não encontrada: " + roleId);
        } catch (FeignException.BadRequest ex) {
            throw new IllegalArgumentException("Role inválida: " + roleId);
        }
    }

    public String buscarNomeRoleOuNull(Long roleId) {
        if (roleId == null) {
            return null;
        }

        try {
            return buscarRolePorId(roleId).nome();
        } catch (EntityNotFoundException ex) {
            log.warn("Role [{}] não encontrada ao montar resposta do usuário.", roleId);
            return null;
        }
    }

    private RoleResumoResponse fallbackBuscarRolePorId(Long roleId, Throwable ex) {
        log.error(
                "CircuitBreaker acionado ao buscar role [{}] no ms-permissao. Causa: {}",
                roleId,
                ex.getMessage(),
                ex
        );

        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço de permissões temporariamente indisponível"
        );
    }
}
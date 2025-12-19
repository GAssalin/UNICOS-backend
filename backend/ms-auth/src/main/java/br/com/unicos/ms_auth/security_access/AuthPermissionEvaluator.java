package br.com.unicos.ms_auth.security_access;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_auth.repository.RolePermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthPermissionEvaluator implements PermissionEvaluator {

    private final RolePermissaoRepository rolePermissaoRepository;

    @Override
    public boolean hasPermission(
            Authentication authentication,
            Object targetDomainObject,
            Object permission
    ) {

        if (authentication == null || permission == null)
            return false;

        Long empresaId = TenantContext.getEmpresaId();

        List<String> roles =
                authentication.getAuthorities()
                        .stream()
                        .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                        .toList();

        return rolePermissaoRepository.rolePossuiPermissao(
                empresaId,
                roles,
                permission.toString()
        );
    }

    @Override
    public boolean hasPermission(
            Authentication authentication,
            Serializable targetId,
            String targetType,
            Object permission
    ) {
        return hasPermission(authentication, null, permission);
    }
}

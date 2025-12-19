package br.com.unicos.ms_usuario.security_access;

import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_usuario.client.AuthPermissionClient;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class RemotePermissionEvaluator implements PermissionEvaluator {

    private final AuthPermissionClient authPermissionClient;

    public RemotePermissionEvaluator(AuthPermissionClient authPermissionClient) {
        this.authPermissionClient = authPermissionClient;
    }

    @Override
    public boolean hasPermission(
            Authentication authentication,
            Object targetDomainObject,
            Object permission
    ) {

        if (authentication == null || permission == null)
            return false;

        Long usuarioId = (Long) authentication.getPrincipal();
        Long empresaId = TenantContext.getEmpresaId();

        return authPermissionClient.usuarioPossuiPermissao(
                usuarioId,
                empresaId,
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
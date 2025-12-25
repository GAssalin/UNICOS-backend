package br.com.unicos.ms_usuario.service;

import br.com.unicos.ms_usuario.client.AuthPermissionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionCheckService {
    private final AuthPermissionClient authPermissionClient;

    public boolean hasPermission(String nomePermissao) {
        if (nomePermissao == null)
            return false;

        return authPermissionClient.usuarioPossuiPermissao(nomePermissao);
    }
}

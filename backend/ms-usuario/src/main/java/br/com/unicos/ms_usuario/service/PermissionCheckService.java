package br.com.unicos.ms_usuario.service;

import br.com.unicos.ms_usuario.client.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionCheckService {
    private final AuthClient authClient;

    public boolean hasPermission(String nomePermissao) {
        if (nomePermissao == null)
            return false;
        return authClient.usuarioPossuiPermissao(nomePermissao);
    }
}

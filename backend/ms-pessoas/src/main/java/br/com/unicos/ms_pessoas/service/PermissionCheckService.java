package br.com.unicos.ms_pessoas.service;

import br.com.unicos.ms_pessoas.client.AuthClient;
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

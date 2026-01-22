package br.com.unicos.ms_filial.service;

import br.com.unicos.ms_filial.client.PermissaoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilsService {
    private final PermissaoClient permissaoClient;

    public boolean verificarPermissao(String permissao) {
        return permissaoClient.usuarioPossuiPermissao(permissao);
    }
}

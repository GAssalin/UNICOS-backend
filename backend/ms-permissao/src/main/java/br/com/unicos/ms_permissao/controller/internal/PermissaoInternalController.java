package br.com.unicos.ms_permissao.controller.internal;

import br.com.unicos.core.auth.context.AuthContext;
import br.com.unicos.core.tenant.context.TenantContext;
import br.com.unicos.ms_permissao.repository.RolePermissaoRepository;
import br.com.unicos.ms_permissao.repository.RoleUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/internal/permissao")
@RequiredArgsConstructor
public class PermissaoInternalController {

    private final RolePermissaoRepository rolePermissaoRepository;
    private final RoleUsuarioRepository roleUsuarioRepository;

    @PostMapping("/check")
    public boolean usuarioPossuiPermissao(@RequestParam String nomePermissao) {
        return rolePermissaoRepository.rolePossuiPermissao(TenantContext.getEmpresaId(), AuthContext.getRoles().stream().toList(), nomePermissao);
    }

    @GetMapping("/find-roles-by-user-id")
    public Set<String> findRolesByUserId(@RequestParam Long id) {
        return roleUsuarioRepository.findRolesByUserId(id);
    }

}

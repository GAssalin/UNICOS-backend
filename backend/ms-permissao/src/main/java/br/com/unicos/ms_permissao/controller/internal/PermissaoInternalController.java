package br.com.unicos.ms_permissao.controller.internal;

import br.com.unicos.core.usuario.auth.context.UserContext;
import br.com.unicos.core.usuario.auth.dto.UsuarioRoleResponse;
import br.com.unicos.ms_permissao.dto.internal.RoleResumoResponse;
import br.com.unicos.ms_permissao.dto.role.RoleResponse;
import br.com.unicos.ms_permissao.service.PermissaoService;
import br.com.unicos.ms_permissao.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class PermissaoInternalController {

    private final PermissaoService permissaoService;
    private final RoleService roleService;

    @PostMapping("/permissao/check")
    public boolean usuarioPossuiPermissao(@RequestParam String nomePermissao) {
        return permissaoService.usuarioPossuiPermissao(nomePermissao);
    }

    @GetMapping("/roles/{id}")
    public RoleResumoResponse buscarRolePorId(@PathVariable Long id) {
        RoleResponse role = roleService.buscarPorId(id);
        return new RoleResumoResponse(role.id(), role.nome());
    }

    @GetMapping("/permissao/role/{id}")
    public UsuarioRoleResponse buscarNomeRoleById(@PathVariable Long id) {
        RoleResponse role = roleService.buscarPorId(id);
        return new UsuarioRoleResponse(UserContext.getUsuarioId(), role.id(), role.nome());
    }
}

package br.com.unicos.ms_permissao.controller.internal;

import br.com.unicos.ms_permissao.dto.internal.RoleResumoResponse;
import br.com.unicos.ms_permissao.dto.role.RoleResponse;
import br.com.unicos.ms_permissao.service.PermissaoService;
import br.com.unicos.ms_permissao.service.RoleService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints consumidos apenas por outros microserviços (exigem o token interno).
 * Usuário e empresa vêm do JWT repassado pelo serviço chamador.
 */
@Hidden
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class PermissaoInternalController {

    private final PermissaoService permissaoService;
    private final RoleService roleService;

    @PostMapping("/permissao/check")
    public boolean usuarioPossuiPermissao(@RequestParam("nomePermissao") String nomePermissao) {
        return permissaoService.usuarioPossuiPermissao(nomePermissao);
    }

    @GetMapping("/roles/{id}")
    public RoleResumoResponse buscarRolePorId(@PathVariable("id") Long id) {
        RoleResponse role = roleService.buscarPorId(id);
        return new RoleResumoResponse(role.id(), role.nome());
    }
}

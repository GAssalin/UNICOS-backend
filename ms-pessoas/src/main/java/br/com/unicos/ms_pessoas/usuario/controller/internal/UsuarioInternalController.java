package br.com.unicos.ms_pessoas.usuario.controller.internal;

import br.com.unicos.core.usuario.dto.UsuarioAuthResponse;
import br.com.unicos.core.usuario.dto.UsuarioResumoResponse;
import br.com.unicos.core.usuario.dto.UsuarioRoleIdsResponse;
import br.com.unicos.ms_pessoas.usuario.model.Usuario;
import br.com.unicos.ms_pessoas.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints consumidos apenas por outros microserviços (exigem o token interno).
 */
@Hidden
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class UsuarioInternalController {

    private final UsuarioService usuarioService;

    @GetMapping("/auth/by-email")
    public UsuarioAuthResponse buscarPorEmail(@RequestParam("email") String email) {
        return usuarioService.buscarParaAutenticacao(email);
    }

    @GetMapping("/auth/usuarios/{id}")
    public UsuarioAuthResponse buscarParaRenovacaoDeToken(@PathVariable("id") Long id) {
        return usuarioService.buscarParaAutenticacao(id);
    }

    /**
     * Nome de exibição do usuário na empresa do contexto atual (JWT repassado pelo serviço chamador).
     */
    @GetMapping("/usuarios/{id}/resumo")
    public UsuarioResumoResponse buscarResumo(@PathVariable("id") Long id) {
        return usuarioService.buscarResumo(id);
    }

    /**
     * Role do usuário na empresa do contexto atual (JWT repassado pelo serviço chamador).
     */
    @GetMapping("/usuarios/{id}/role")
    public UsuarioRoleIdsResponse buscarRoleDoUsuario(@PathVariable("id") Long id) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
        return new UsuarioRoleIdsResponse(usuario.getId(), usuario.getRoleId());
    }
}

package br.com.unicos.ms_usuario.controller.internal;

import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_usuario.dto.usuario.UsuarioRoleResponse;
import br.com.unicos.ms_usuario.model.Usuario;
import br.com.unicos.ms_usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class UsuarioInternalController {

    private final UsuarioService usuarioService;

    @GetMapping("/auth/by-email")
    public UsuarioAuthResponse buscarPorEmail(@RequestParam String email) {
        return usuarioService.buscarParaAutenticacao(email);
    }

    @GetMapping("/usuarios/{id}/role")
    public UsuarioRoleResponse buscarRoleDoUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
        return new UsuarioRoleResponse(usuario.getId(), usuario.getRoleId());
    }
}

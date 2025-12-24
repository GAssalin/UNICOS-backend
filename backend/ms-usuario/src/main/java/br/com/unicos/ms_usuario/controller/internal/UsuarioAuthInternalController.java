package br.com.unicos.ms_usuario.controller.internal;

import br.com.unicos.core.usuario.auth.dto.UsuarioAuthResponse;
import br.com.unicos.ms_usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
public class UsuarioAuthInternalController {

    private final UsuarioService usuarioService;

    @GetMapping("/by-email")
    public UsuarioAuthResponse buscarPorEmail(@RequestParam String email) {
        return usuarioService.buscarParaAutenticacao(email);
    }

}
package br.com.unicos.ms_usuario.controller;

import br.com.unicos.ms_usuario.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_usuario.service.UsuarioEmailVerificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/verificacao-email")
@RequiredArgsConstructor
public class UsuarioEmailVerificacaoController {

    private final UsuarioEmailVerificacaoService usuarioEmailVerificacaoService;

    @PostMapping("/usuario/{usuarioId}/gerar")
    public ResponseEntity<String> gerarToken(@PathVariable Long usuarioId) {
        String token = usuarioEmailVerificacaoService.gerarTokenParaUsuario(usuarioId);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/usuario/{usuarioId}/reenviar")
    public ResponseEntity<String> reenviarToken(@PathVariable Long usuarioId) {
        String token = usuarioEmailVerificacaoService.reenviarToken(usuarioId);
        return ResponseEntity.ok(token);
    }

    @PatchMapping("/confirmar")
    public ResponseEntity<Void> confirmarEmail(@RequestParam("token") String token) {
        usuarioEmailVerificacaoService.confirmarEmail(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/limpar-expirados")
    public ResponseEntity<Void> limparTokensExpirados() {
        usuarioEmailVerificacaoService.limparTokensExpirados();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pendentes")
    public ResponseEntity<Page<UsuarioEmailVerificacaoListDTO>> listarPendentes(
            @RequestParam("empresaId") Long empresaId,
            Pageable pageable
    ) {
        Page<UsuarioEmailVerificacaoListDTO> response =
                usuarioEmailVerificacaoService.listarPendentes(empresaId, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/expirados")
    public ResponseEntity<Page<UsuarioEmailVerificacaoListDTO>> listarExpirados(
            @RequestParam("empresaId") Long empresaId,
            Pageable pageable
    ) {
        Page<UsuarioEmailVerificacaoListDTO> response =
                usuarioEmailVerificacaoService.listarExpirados(empresaId, pageable);

        return ResponseEntity.ok(response);
    }
}
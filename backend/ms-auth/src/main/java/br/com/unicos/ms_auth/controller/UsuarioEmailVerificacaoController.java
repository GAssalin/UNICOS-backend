package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.verificacao.ConfirmarEmailVerificacaoRequest;
import br.com.unicos.ms_auth.dto.verificacao.ConfirmarEmailVerificacaoResponse;
import br.com.unicos.ms_auth.dto.verificacao.ReenviarEmailVerificacaoRequest;
import br.com.unicos.ms_auth.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_auth.repository.UsuarioEmailVerificacaoRepository;
import br.com.unicos.ms_auth.service.interfaces.UsuarioEmailVerificacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST responsável pelo fluxo de verificação de e-mail dos usuários.
 * <p>
 * Endpoints:
 * - Confirmar e-mail através do token
 * - Reenviar token de verificação
 * - Listar tokens pendentes
 * - Listar tokens expirados
 */
@RestController
@RequestMapping("/v1/auth/verificacao-email")
@RequiredArgsConstructor
public class UsuarioEmailVerificacaoController {

    private final UsuarioEmailVerificacaoService verificacaoService;
    private final UsuarioEmailVerificacaoRepository verificacaoRepository;

    @PostMapping("/confirmar")
    public ResponseEntity<ConfirmarEmailVerificacaoResponse> confirmar(
            @Valid @RequestBody ConfirmarEmailVerificacaoRequest request) {

        verificacaoService.confirmarEmail(request.token());

        return ResponseEntity.ok(
                new ConfirmarEmailVerificacaoResponse(
                        null,
                        null,
                        true,
                        "E-mail confirmado com sucesso."
                )
        );
    }

    @PostMapping("/reenviar")
    public ResponseEntity<String> reenviarToken(
            @Valid @RequestBody ReenviarEmailVerificacaoRequest request) {

        String novoToken = verificacaoService.reenviarToken(request.usuarioId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Novo link de verificação enviado para o e-mail cadastrado.");
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<UsuarioEmailVerificacaoListDTO>> listarPendentes() {

        List<UsuarioEmailVerificacaoListDTO> lista =
                verificacaoRepository.findByUtilizadoFalse().stream()
                        .map(t -> new UsuarioEmailVerificacaoListDTO(
                                t.getId(),
                                t.getUsuario().getId(),
                                false,
                                t.getExpiracao()
                        ))
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/expirados")
    public ResponseEntity<List<UsuarioEmailVerificacaoListDTO>> listarExpirados() {

        List<UsuarioEmailVerificacaoListDTO> lista =
                verificacaoRepository.findByExpiracaoBefore(LocalDateTime.now()).stream()
                        .map(t -> new UsuarioEmailVerificacaoListDTO(
                                t.getId(),
                                t.getUsuario().getId(),
                                t.isUtilizado(),
                                t.getExpiracao()
                        ))
                        .toList();

        return ResponseEntity.ok(lista);
    }
}

package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.verificacao.ConfirmarEmailVerificacaoRequest;
import br.com.unicos.ms_auth.dto.verificacao.ConfirmarEmailVerificacaoResponse;
import br.com.unicos.ms_auth.dto.verificacao.ReenviarEmailVerificacaoRequest;
import br.com.unicos.ms_auth.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_auth.repository.UsuarioEmailVerificacaoRepository;
import br.com.unicos.ms_auth.service.interfaces.UsuarioEmailVerificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Verificação de E-mail",
        description = "Endpoints responsáveis pela confirmação de e-mail, reenvio de tokens e listagem de verificações pendentes e expiradas."
)
public class UsuarioEmailVerificacaoController {

    private final UsuarioEmailVerificacaoService verificacaoService;
    private final UsuarioEmailVerificacaoRepository verificacaoRepository;

    // =====================================================================
    // 🔹 Confirmar e-mail
    // =====================================================================

    @Operation(
            summary = "Confirmar endereço de e-mail",
            description = """
                    Realiza a confirmação do e-mail de um usuário utilizando o token \
                    enviado no processo de cadastro.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "E-mail confirmado com sucesso",
                            content = @Content(schema = @Schema(implementation = ConfirmarEmailVerificacaoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Token inválido ou já utilizado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Token não encontrado",
                            content = @Content
                    )
            }
    )
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

    // =====================================================================
    // 🔹 Reenviar token de verificação
    // =====================================================================

    @Operation(
            summary = "Reenviar token de verificação",
            description = """
                    Reenvia um novo token de verificação para o e-mail do usuário \
                    caso o anterior tenha expirado ou não tenha sido utilizado.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Novo token gerado e enviado com sucesso",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content
                    )
            }
    )
    @PostMapping("/reenviar")
    public ResponseEntity<String> reenviarToken(
            @Valid @RequestBody ReenviarEmailVerificacaoRequest request) {

        String novoToken = verificacaoService.reenviarToken(request.usuarioId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Novo link de verificação enviado para o e-mail cadastrado.");
    }

    // =====================================================================
    // 🔹 Listar tokens pendentes
    // =====================================================================

    @Operation(
            summary = "Listar tokens pendentes",
            description = "Retorna os tokens de verificação que ainda não foram utilizados e que não expiraram.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioEmailVerificacaoListDTO.class)))
                    )
            }
    )
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

    // =====================================================================
    // 🔹 Listar tokens expirados
    // =====================================================================

    @Operation(
            summary = "Listar tokens expirados",
            description = "Retorna os tokens cujo prazo de validade já expirou.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioEmailVerificacaoListDTO.class)))
                    )
            }
    )
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

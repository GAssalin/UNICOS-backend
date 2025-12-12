package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.auditoria.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import br.com.unicos.ms_auth.service.interfaces.AuditoriaAcessoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST responsável pela consulta dos registros
 * de auditoria de acesso do sistema.
 * <p>
 * Permite filtros por usuário, ação e intervalo de datas,
 * fornecendo uma interface completa para auditorias e segurança.
 */
@RestController
@RequestMapping("/v1/auditorias")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Auditoria de Acesso",
        description = "Endpoints para consulta de auditorias de login, logout e outras ações do sistema."
)
public class AuditoriaAcessoController {

    private final AuditoriaAcessoService auditoriaAcessoService;

    // ================================================
    // 🔍 CONSULTA POR USUÁRIO
    // Permissão necessária: AUDITORIA_LISTAR
    // ================================================
    @PreAuthorize("hasAuthority('AUDITORIA_LISTAR')")
    @Operation(
            summary = "Listar auditorias por usuário",
            description = "Retorna todos os registros de auditoria vinculados ao usuário informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operação realizada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuditoriaAcessoResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content
                    )
            }
    )
    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<AuditoriaAcessoResponse>> listarPorUsuario(
            @PathVariable String username) {

        return ResponseEntity.ok(auditoriaAcessoService.listarPorUsuario(username));
    }

    // ================================================
    // 🔍 CONSULTA POR TIPO DE AÇÃO
    // Permissão necessária: AUDITORIA_LISTAR
    // ================================================
    @PreAuthorize("hasAuthority('AUDITORIA_LISTAR')")
    @Operation(
            summary = "Listar auditorias por tipo de ação",
            description = "Retorna registros filtrados por tipo de ação, como LOGIN_SUCESSO, LOGIN_FALHA ou LOGOUT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuditoriaAcessoResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Tipo de ação inválido",
                            content = @Content
                    )
            }
    )
    @GetMapping("/acao/{acao}")
    public ResponseEntity<List<AuditoriaAcessoResponse>> listarPorAcao(
            @PathVariable TipoAcaoAcesso acao) {

        return ResponseEntity.ok(auditoriaAcessoService.listarPorAcao(acao));
    }

    // ================================================
    // 🔍 CONSULTA POR PERÍODO
    // Permissão necessária: AUDITORIA_LISTAR
    // ================================================
    @PreAuthorize("hasAuthority('AUDITORIA_LISTAR')")
    @Operation(
            summary = "Listar auditorias por período",
            description = "Retorna registros de auditoria ocorridos entre as datas de início e fim informadas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuditoriaAcessoResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Formato inválido de datas ou intervalo inconsistente",
                            content = @Content
                    )
            }
    )
    @GetMapping("/periodo")
    public ResponseEntity<List<AuditoriaAcessoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        return ResponseEntity.ok(auditoriaAcessoService.listarPorPeriodo(inicio, fim));
    }
}

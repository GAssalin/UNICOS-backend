package br.com.unicos.ms_auth.controller;

import br.com.unicos.ms_auth.dto.auditoria.AuditoriaAcessoResponse;
import br.com.unicos.ms_auth.enums.TipoAcaoAcesso;
import br.com.unicos.ms_auth.service.AuditoriaAcessoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/auditorias")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Auditoria de Acesso",
        description = "Endpoints para consulta de auditorias de login, logout e demais ações do sistema."
)
public class AuditoriaAcessoController {

    private final AuditoriaAcessoService auditoriaAcessoService;

    // ============================================================
    // CONSULTA POR USUÁRIO
    // ============================================================

    @Operation(
            summary = "Listar auditorias por usuário",
            description = "Retorna registros de auditoria vinculados a um usuário, restritos à empresa.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = AuditoriaAcessoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'AUDITORIA_LISTAR')")
    @GetMapping("/usuario/{username}")
    public ResponseEntity<Page<AuditoriaAcessoResponse>> listarPorUsuario(
            @PathVariable String username,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(auditoriaAcessoService.listarPorUsuario(username, pageable));
    }

    // ============================================================
    // CONSULTA POR AÇÃO
    // ============================================================

    @Operation(
            summary = "Listar auditorias por tipo de ação",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'AUDITORIA_LISTAR')")
    @GetMapping("/acao/{acao}")
    public ResponseEntity<Page<AuditoriaAcessoResponse>> listarPorAcao(
            @PathVariable TipoAcaoAcesso acao,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(auditoriaAcessoService.listarPorAcao(acao, pageable));
    }

    // ============================================================
    // CONSULTA POR PERÍODO
    // ============================================================

    @Operation(
            summary = "Listar auditorias por período",
            description = "Retorna registros de auditoria dentro do intervalo de datas informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Período inválido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PreAuthorize("hasPermission(null, 'AUDITORIA_LISTAR')")
    @GetMapping("/periodo")
    public ResponseEntity<Page<AuditoriaAcessoResponse>> listarPorPeriodo(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,

            @RequestParam("fim")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fim,

            @ParameterObject Pageable pageable
    ) {
        if (inicio.isAfter(fim))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data/hora inicial não pode ser posterior à data/hora final");
        return ResponseEntity.ok(auditoriaAcessoService.listarPorPeriodo(inicio, fim, pageable));
    }
}

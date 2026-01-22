package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroUpdateRequest;
import br.com.unicos.ms_empresa.service.EmpresaParametroService;
import br.com.unicos.ms_empresa.service.UtilsService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo gerenciamento
 * dos parâmetros flexíveis da empresa.
 *
 * <p>
 * Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.
 * </p>
 */
@RestController
@RequestMapping("/v1/empresas/{empresaRefId}/parametros")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Parâmetros da Empresa",
        description = "Endpoints para gerenciamento dos parâmetros flexíveis da empresa (chave-valor)."
)
public class EmpresaParametroController {

    private final UtilsService utilsService;
    private final EmpresaParametroService empresaParametroService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar parâmetro da empresa",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Parâmetro criado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaParametroResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<EmpresaParametroResponse> criar(
            @PathVariable Long empresaRefId,
            @RequestBody @Validated EmpresaParametroCreateRequest request
    ) {
        if (utilsService.verificarPermissao("EMPRESA_PARAMETRO_CRIAR")) {
            EmpresaParametroCreateRequest normalized =
                    new EmpresaParametroCreateRequest(
                            request.empresaId(),
                            empresaRefId,
                            request.chave(),
                            request.valor()
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(empresaParametroService.criar(normalized));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar parâmetro da empresa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Parâmetro atualizado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{chave}")
    public ResponseEntity<EmpresaParametroResponse> atualizar(
            @PathVariable Long empresaRefId,
            @PathVariable String chave,
            @RequestBody @Validated EmpresaParametroUpdateRequest request
    ) {
        if (utilsService.verificarPermissao("EMPRESA_PARAMETRO_EDITAR"))
            return ResponseEntity.ok(empresaParametroService.atualizar(empresaRefId, chave, request));
        else
            return ResponseEntity.status(403).build();
    }

    // ============================================================
    // GET
    // ============================================================

    @Operation(
            summary = "Buscar parâmetro por chave",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaParametroResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{chave}")
    public ResponseEntity<EmpresaParametroResponse> buscarPorChave(
            @PathVariable Long empresaRefId,
            @PathVariable String chave
    ) {
        if (utilsService.verificarPermissao("EMPRESA_PARAMETRO_LISTAR"))
            return ResponseEntity.ok(empresaParametroService.buscarPorChave(empresaRefId, chave));
        else
            return ResponseEntity.status(403).build();
    }

    // ============================================================
    // LIST
    // ============================================================

    @Operation(
            summary = "Listar parâmetros da empresa",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EmpresaParametroResumoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<EmpresaParametroResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        if (utilsService.verificarPermissao("EMPRESA_PARAMETRO_LISTAR"))
            return ResponseEntity.ok(empresaParametroService.listar(empresaRefId, pageable));
        else
            return ResponseEntity.status(403).build();
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover parâmetro da empresa",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Parâmetro removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{chave}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable String chave
    ) {
        if (utilsService.verificarPermissao("EMPRESA_PARAMETRO_EXCLUIR")) {
            empresaParametroService.remover(empresaRefId, chave);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}

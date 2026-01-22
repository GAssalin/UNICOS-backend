package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoContatoEmpresa;
import br.com.unicos.ms_empresa.service.EmpresaContatoService;
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

@RestController
@RequestMapping("/v1/empresas/contatos/{empresaRefId}")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Contatos da Empresa",
        description = "Endpoints para gerenciamento dos contatos institucionais da empresa."
)
public class EmpresaContatoController {

    private final EmpresaContatoService empresaContatoService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar contato institucional",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Contato criado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaContatoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<EmpresaContatoResponse> criar(
            @PathVariable Long empresaRefId,
            @RequestBody @Validated EmpresaContatoCreateRequest request
    ) {
        EmpresaContatoCreateRequest normalized =
                new EmpresaContatoCreateRequest(
                        request.empresaId(),
                        empresaRefId,
                        request.tipoContato(),
                        request.valor(),
                        request.principal()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaContatoService.criar(normalized));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar contato institucional",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaContatoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaContatoResponse> atualizar(
            @PathVariable Long empresaRefId,
            @PathVariable Long id,
            @RequestBody @Validated EmpresaContatoUpdateRequest request
    ) {
        return ResponseEntity.ok(empresaContatoService.atualizar(id, request));
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Operation(
            summary = "Buscar contato institucional por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaContatoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaContatoResponse> buscarPorId(
            @PathVariable Long empresaRefId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(empresaContatoService.buscarPorId(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Operation(
            summary = "Listar contatos institucionais",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = EmpresaContatoResumoResponse.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<EmpresaContatoResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaContatoService.listar(empresaRefId, pageable));
    }

    // ============================================================
    // LIST BY TYPE
    // ============================================================

    @Operation(
            summary = "Listar contatos por tipo",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<EmpresaContatoResumoResponse>> listarPorTipo(
            @PathVariable Long empresaRefId,
            @PathVariable TipoContatoEmpresa tipo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaContatoService.listarPorTipo(empresaRefId, tipo, pageable));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover contato institucional",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Contato removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable Long id
    ) {
        empresaContatoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}

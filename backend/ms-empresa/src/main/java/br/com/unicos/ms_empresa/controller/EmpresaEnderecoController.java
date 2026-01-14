package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoUpdateRequest;
import br.com.unicos.ms_empresa.enums.TipoEnderecoEmpresa;
import br.com.unicos.ms_empresa.service.EmpresaEnderecoService;
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
@RequestMapping("/v1/empresas/{empresaRefId}/enderecos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Endereços da Empresa",
        description = "Endpoints para gerenciamento dos endereços institucionais da empresa."
)
public class EmpresaEnderecoController {

    private final EmpresaEnderecoService empresaEnderecoService;

    // ============================================================
    // CREATE
    // ============================================================

    @Operation(
            summary = "Criar endereço institucional",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Endereço criado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaEnderecoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<EmpresaEnderecoResponse> criar(
            @PathVariable Long empresaRefId,
            @RequestBody @Validated EmpresaEnderecoCreateRequest request
    ) {
        EmpresaEnderecoCreateRequest normalized =
                new EmpresaEnderecoCreateRequest(
                        request.empresaId(),
                        empresaRefId,
                        request.tipoEndereco(),
                        request.logradouro(),
                        request.numero(),
                        request.complemento(),
                        request.bairro(),
                        request.municipio(),
                        request.uf(),
                        request.cep(),
                        request.principal()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaEnderecoService.criar(normalized));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Operation(
            summary = "Atualizar endereço institucional",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaEnderecoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaEnderecoResponse> atualizar(
            @PathVariable Long empresaRefId,
            @PathVariable Long id,
            @RequestBody @Validated EmpresaEnderecoUpdateRequest request
    ) {
        return ResponseEntity.ok(empresaEnderecoService.atualizar(id, request));
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Operation(
            summary = "Buscar endereço institucional por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaEnderecoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaEnderecoResponse> buscarPorId(
            @PathVariable Long empresaRefId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(empresaEnderecoService.buscarPorId(id));
    }

    // ============================================================
    // LIST
    // ============================================================

    @Operation(
            summary = "Listar endereços institucionais",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EmpresaEnderecoResumoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<EmpresaEnderecoResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaEnderecoService.listar(empresaRefId, pageable));
    }

    // ============================================================
    // LIST BY TYPE
    // ============================================================

    @Operation(
            summary = "Listar endereços por tipo",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<EmpresaEnderecoResumoResponse>> listarPorTipo(
            @PathVariable Long empresaRefId,
            @PathVariable TipoEnderecoEmpresa tipo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaEnderecoService.listarPorTipo(empresaRefId, tipo, pageable));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Operation(
            summary = "Remover endereço institucional",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Endereço removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable Long id
    ) {
        empresaEnderecoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}

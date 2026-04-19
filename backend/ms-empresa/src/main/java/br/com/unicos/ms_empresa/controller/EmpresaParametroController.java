package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_parametro.EmpresaParametroUpdateRequest;
import br.com.unicos.ms_empresa.service.EmpresaParametroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
 * <p>Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.</p>
 */
@RestController
@RequestMapping("/v1/empresas/{empresaRefId}/parametros")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Parâmetros da Empresa",
        description = "Endpoints para gerenciamento dos parâmetros flexíveis da empresa."
)
public class EmpresaParametroController {

    private final EmpresaParametroService empresaParametroService;

    @Operation(
            summary = "Criar parâmetro da empresa",
            description = "Cria um novo parâmetro flexível para a empresa informada.",
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
            @PathVariable @Positive Long empresaRefId,
            @RequestBody @Valid EmpresaParametroCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaParametroService.criar(request));
    }

    @Operation(
            summary = "Atualizar parâmetro da empresa",
            description = "Atualiza um parâmetro existente por chave.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Parâmetro atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaParametroResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{chave}")
    public ResponseEntity<EmpresaParametroResponse> atualizar(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable String chave,
            @RequestBody @Valid EmpresaParametroUpdateRequest request
    ) {
        return ResponseEntity.ok(empresaParametroService.atualizar(chave, request));
    }

    @Operation(
            summary = "Buscar parâmetro por chave",
            description = "Busca um parâmetro específico da empresa pela chave.",
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
            @PathVariable @Positive Long empresaRefId,
            @PathVariable String chave
    ) {
        return ResponseEntity.ok(empresaParametroService.buscarPorChave(chave));
    }

    @Operation(
            summary = "Listar parâmetros da empresa",
            description = "Lista todos os parâmetros da empresa de forma paginada.",
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
            @PathVariable @Positive Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaParametroService.listar(pageable));
    }

    @Operation(
            summary = "Remover parâmetro da empresa",
            description = "Remove um parâmetro da empresa pela chave.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Parâmetro removido com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Parâmetro não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{chave}")
    public ResponseEntity<Void> remover(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable String chave
    ) {
        empresaParametroService.remover(chave);
        return ResponseEntity.noContent().build();
    }
}
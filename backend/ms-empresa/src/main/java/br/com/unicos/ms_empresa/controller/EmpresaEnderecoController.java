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
 * dos endereços institucionais da empresa.
 *
 * <p>Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.</p>
 */
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

    @Operation(
            summary = "Criar endereço institucional",
            description = "Cria um novo endereço institucional para a empresa informada.",
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
            @PathVariable @Positive Long empresaRefId,
            @RequestBody @Valid EmpresaEnderecoCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaEnderecoService.criar(request));
    }

    @Operation(
            summary = "Atualizar endereço institucional",
            description = "Atualiza um endereço institucional pelo identificador informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Endereço atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaEnderecoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaEnderecoResponse> atualizar(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable @Positive Long id,
            @RequestBody @Valid EmpresaEnderecoUpdateRequest request
    ) {
        return ResponseEntity.ok(empresaEnderecoService.atualizar(id, request));
    }

    @Operation(
            summary = "Buscar endereço institucional por ID",
            description = "Busca um endereço institucional pelo identificador.",
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
            @PathVariable @Positive Long empresaRefId,
            @PathVariable @Positive Long id
    ) {
        return ResponseEntity.ok(empresaEnderecoService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar endereços institucionais",
            description = "Lista os endereços institucionais da empresa de forma paginada.",
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
            @PathVariable @Positive Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaEnderecoService.listar(pageable));
    }

    @Operation(
            summary = "Listar endereços por tipo",
            description = "Lista os endereços institucionais da empresa filtrando pelo tipo informado.",
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
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<EmpresaEnderecoResumoResponse>> listarPorTipo(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable TipoEnderecoEmpresa tipo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaEnderecoService.listarPorTipo(tipo, pageable));
    }

    @Operation(
            summary = "Remover endereço institucional",
            description = "Remove um endereço institucional pelo identificador informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable @Positive Long empresaRefId,
            @PathVariable @Positive Long id
    ) {
        empresaEnderecoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
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
 * dos contatos institucionais da empresa.
 *
 * <p>Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.</p>
 */
@RestController
@RequestMapping("/v1/empresas/{empresaRefId}/contatos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Contatos da Empresa",
        description = "Endpoints para gerenciamento dos contatos institucionais da empresa."
)
public class EmpresaContatoController {

    private final EmpresaContatoService empresaContatoService;

    @Operation(
            summary = "Criar contato institucional",
            description = "Cria um novo contato institucional para a empresa informada.",
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
    public ResponseEntity<EmpresaContatoResponse> criar(@RequestBody @Valid EmpresaContatoCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaContatoService.criar(request));
    }

    @Operation(
            summary = "Atualizar contato institucional",
            description = "Atualiza um contato institucional pelo identificador informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = EmpresaContatoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaContatoResponse> atualizar(
            @PathVariable @Positive Long id,
            @RequestBody @Valid EmpresaContatoUpdateRequest request
    ) {
        return ResponseEntity.ok(empresaContatoService.atualizar(id, request));
    }

    @Operation(
            summary = "Buscar contato institucional por ID",
            description = "Busca um contato institucional pelo identificador.",
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
    public ResponseEntity<EmpresaContatoResponse> buscarPorId(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(empresaContatoService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar contatos institucionais",
            description = "Lista os contatos institucionais da empresa de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EmpresaContatoResumoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<EmpresaContatoResumoResponse>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(empresaContatoService.listar(pageable));
    }

    @Operation(
            summary = "Listar contatos por tipo",
            description = "Lista os contatos institucionais da empresa filtrando pelo tipo informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = EmpresaContatoResumoResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<EmpresaContatoResumoResponse>> listarPorTipo(
            @PathVariable TipoContatoEmpresa tipo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaContatoService.listarPorTipo(tipo, pageable));
    }

    @Operation(
            summary = "Remover contato institucional",
            description = "Remove um contato institucional pelo identificador informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Contato removido com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable @Positive Long id) {
        empresaContatoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
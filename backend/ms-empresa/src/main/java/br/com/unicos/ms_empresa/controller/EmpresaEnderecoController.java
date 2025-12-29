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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo gerenciamento
 * dos endereços institucionais da empresa.
 *
 * <p>
 * Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.
 * </p>
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

    // ============================================================
    // ➕ CRIAÇÃO
    // ============================================================

    @Operation(
            summary = "Criar endereço institucional",
            description = "Cadastra um novo endereço institucional para a empresa."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Endereço criado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaEnderecoResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_ENDERECO_CRIAR')")
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

        EmpresaEnderecoResponse response = empresaEnderecoService.criar(normalized);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // ✏️ ATUALIZAÇÃO
    // ============================================================

    @Operation(
            summary = "Atualizar endereço institucional",
            description = "Atualiza um endereço institucional existente."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Endereço atualizado com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_ENDERECO_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaEnderecoResponse> atualizar(
            @PathVariable Long empresaRefId,
            @PathVariable Long id,
            @RequestBody @Validated EmpresaEnderecoUpdateRequest request
    ) {
        return ResponseEntity.ok(empresaEnderecoService.atualizar(id, request));
    }

    // ============================================================
    // 🔍 CONSULTA POR ID
    // ============================================================

    @Operation(
            summary = "Buscar endereço institucional por ID",
            description = "Retorna os dados de um endereço institucional específico."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaEnderecoResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_ENDERECO_LISTAR')")
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaEnderecoResponse> buscarPorId(
            @PathVariable Long empresaRefId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(empresaEnderecoService.buscarPorId(id));
    }

    // ============================================================
    // 📄 LISTAGEM
    // ============================================================

    @Operation(
            summary = "Listar endereços institucionais",
            description = "Lista os endereços institucionais da empresa de forma paginada."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = EmpresaEnderecoResumoResponse.class)
                    )
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_ENDERECO_LISTAR')")
    @GetMapping
    public ResponseEntity<Page<EmpresaEnderecoResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaEnderecoService.listar(empresaRefId, pageable));
    }

    // ============================================================
    // 📄 LISTAGEM POR TIPO
    // ============================================================

    @Operation(
            summary = "Listar endereços por tipo",
            description = "Lista os endereços institucionais da empresa filtrando pelo tipo."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_ENDERECO_LISTAR')")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<EmpresaEnderecoResumoResponse>> listarPorTipo(
            @PathVariable Long empresaRefId,
            @PathVariable TipoEnderecoEmpresa tipo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                empresaEnderecoService.listarPorTipo(
                        empresaRefId,
                        tipo,
                        pageable
                )
        );
    }

    // ============================================================
    // 🗑️ EXCLUSÃO
    // ============================================================

    @Operation(
            summary = "Remover endereço institucional",
            description = "Remove um endereço institucional da empresa."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Endereço removido com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_ENDERECO_EXCLUIR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable Long id
    ) {
        empresaEnderecoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}

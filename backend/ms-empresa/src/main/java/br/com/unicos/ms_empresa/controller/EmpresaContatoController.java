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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pelo gerenciamento
 * dos contatos institucionais da empresa.
 *
 * <p>
 * Todos os endpoints são restritos ao tenant
 * e respeitam as permissões do usuário autenticado.
 * </p>
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

    // ============================================================
    // ➕ CRIAÇÃO
    // ============================================================

    @Operation(
            summary = "Criar contato institucional",
            description = "Cadastra um novo contato institucional para a empresa."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Contato criado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaContatoResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONTATO_CRIAR')")
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

        EmpresaContatoResponse response = empresaContatoService.criar(normalized);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // ✏️ ATUALIZAÇÃO
    // ============================================================

    @Operation(
            summary = "Atualizar contato institucional",
            description = "Atualiza um contato institucional existente."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Contato atualizado com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONTATO_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaContatoResponse> atualizar(
            @PathVariable Long empresaRefId,
            @PathVariable Long id,
            @RequestBody @Validated EmpresaContatoUpdateRequest request
    ) {
        return ResponseEntity.ok(empresaContatoService.atualizar(id, request));
    }

    // ============================================================
    // 🔍 CONSULTA POR ID
    // ============================================================

    @Operation(
            summary = "Buscar contato institucional por ID",
            description = "Retorna os dados de um contato institucional específico."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EmpresaContatoResponse.class)
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONTATO_LISTAR')")
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaContatoResponse> buscarPorId(
            @PathVariable Long empresaRefId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(empresaContatoService.buscarPorId(id));
    }

    // ============================================================
    // 📄 LISTAGEM
    // ============================================================

    @Operation(
            summary = "Listar contatos institucionais",
            description = "Lista os contatos institucionais da empresa de forma paginada."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = EmpresaContatoResumoResponse.class)
                    )
            )
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONTATO_LISTAR')")
    @GetMapping
    public ResponseEntity<Page<EmpresaContatoResumoResponse>> listar(
            @PathVariable Long empresaRefId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(empresaContatoService.listar(empresaRefId, pageable));
    }

    // ============================================================
    // 📄 LISTAGEM POR TIPO
    // ============================================================

    @Operation(
            summary = "Listar contatos por tipo",
            description = "Lista os contatos institucionais da empresa filtrando pelo tipo."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Consulta realizada com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONTATO_LISTAR')")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<EmpresaContatoResumoResponse>> listarPorTipo(
            @PathVariable Long empresaRefId,
            @PathVariable TipoContatoEmpresa tipo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                empresaContatoService.listarPorTipo(
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
            summary = "Remover contato institucional",
            description = "Remove um contato institucional da empresa."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Contato removido com sucesso"
    )
    @PreAuthorize("hasPermission(null, 'EMPRESA_CONTATO_EXCLUIR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long empresaRefId,
            @PathVariable Long id
    ) {
        empresaContatoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}

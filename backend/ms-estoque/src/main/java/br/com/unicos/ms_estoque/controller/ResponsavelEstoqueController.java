package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueCreateRequestDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueResponseDto;
import br.com.unicos.ms_estoque.dto.responsavel.ResponsavelEstoqueUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusResponsavelEstoque;
import br.com.unicos.ms_estoque.service.ResponsavelEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de responsáveis por estoque.
 */
@RestController
@RequestMapping("/v1/responsaveis-estoque")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Responsáveis de Estoque",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de responsáveis por estoque."
)
public class ResponsavelEstoqueController {

    private final ResponsavelEstoqueService responsavelService;

    /**
     * Cadastra um novo vínculo de responsável por estoque.
     *
     * @param request dados para criação do vínculo
     * @return vínculo criado
     */
    @Operation(
            summary = "Cadastrar responsável por estoque",
            description = "Cria um novo vínculo de responsável para um estoque.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Responsável criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ResponsavelEstoqueResponseDto> salvar(
            @Valid @RequestBody ResponsavelEstoqueCreateRequestDto request
    ) {
        ResponsavelEstoqueResponseDto response = responsavelService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um vínculo de responsável por estoque.
     *
     * @param id identificador do vínculo
     * @param request dados para atualização
     * @return vínculo atualizado
     */
    @Operation(
            summary = "Atualizar responsável por estoque",
            description = "Atualiza os dados de um responsável por estoque existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Responsável atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResponsavelEstoqueResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResponsavelEstoqueUpdateRequestDto request
    ) {
        ResponsavelEstoqueResponseDto response = responsavelService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um vínculo de responsável por identificador.
     *
     * @param id identificador do vínculo
     * @return vínculo encontrado
     */
    @Operation(
            summary = "Buscar responsável por ID",
            description = "Retorna os dados de um responsável por estoque específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponsavelEstoqueResponseDto> buscarPorId(@PathVariable Long id) {
        ResponsavelEstoqueResponseDto response = responsavelService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista responsáveis vinculados a um estoque.
     *
     * @param estoqueId identificador do estoque
     * @param pageable configuração de paginação
     * @return página de responsáveis
     */
    @Operation(
            summary = "Listar responsáveis por estoque",
            description = "Lista os responsáveis vinculados a um estoque de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/estoque/{estoqueId}")
    public ResponseEntity<Page<ResponsavelEstoqueResponseDto>> listarPorEstoque(
            @PathVariable Long estoqueId,
            @ParameterObject Pageable pageable
    ) {
        Page<ResponsavelEstoqueResponseDto> response = responsavelService.listarPorEstoque(estoqueId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista responsáveis vinculados a um estoque filtrando por status.
     *
     * @param estoqueId identificador do estoque
     * @param status status do vínculo
     * @param pageable configuração de paginação
     * @return página de responsáveis filtrados
     */
    @Operation(
            summary = "Listar responsáveis por estoque e status",
            description = "Lista os responsáveis vinculados a um estoque filtrando por status, de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ResponsavelEstoqueResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/estoque/{estoqueId}/status/{status}")
    public ResponseEntity<Page<ResponsavelEstoqueResponseDto>> listarPorEstoqueEStatus(
            @PathVariable Long estoqueId,
            @Parameter(description = "Status do responsável do estoque")
            @PathVariable StatusResponsavelEstoque status,
            @ParameterObject Pageable pageable
    ) {
        Page<ResponsavelEstoqueResponseDto> response =
                responsavelService.listarPorEstoqueEStatus(estoqueId, status, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um vínculo de responsável por estoque.
     *
     * @param id identificador do vínculo
     * @return resposta sem conteúdo
     */
    @Operation(
            summary = "Remover responsável por estoque",
            description = "Remove um vínculo de responsável por estoque pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Responsável removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        responsavelService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
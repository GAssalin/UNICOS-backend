package br.com.unicos.ms_estoque.controller;

import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialCreateRequestDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialResponseDto;
import br.com.unicos.ms_estoque.dto.vinculo.VinculoEstoqueFilialUpdateRequestDto;
import br.com.unicos.ms_estoque.enums.StatusVinculoEstoqueFilial;
import br.com.unicos.ms_estoque.service.VinculoEstoqueFilialService;
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
 * Controller responsável pelos endpoints de vínculos entre estoque e filial.
 */
@RestController
@RequestMapping("/v1/vinculos-estoque-filial")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Vínculos Estoque x Filial",
        description = "Endpoints para criação, atualização, consulta, listagem e remoção de vínculos entre estoques e filiais."
)
public class VinculoEstoqueFilialController {

    private final VinculoEstoqueFilialService vinculoService;

    /**
     * Cadastra um novo vínculo entre estoque e filial.
     *
     * @param request dados para criação do vínculo
     * @return vínculo criado
     */
    @Operation(
            summary = "Cadastrar vínculo Estoque x Filial",
            description = "Cria um novo vínculo entre um estoque e uma filial.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Vínculo criado com sucesso",
                            content = @Content(schema = @Schema(implementation = VinculoEstoqueFilialResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<VinculoEstoqueFilialResponseDto> salvar(
            @Valid @RequestBody VinculoEstoqueFilialCreateRequestDto request
    ) {
        VinculoEstoqueFilialResponseDto response = vinculoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um vínculo entre estoque e filial.
     *
     * @param id identificador do vínculo
     * @param request dados para atualização
     * @return vínculo atualizado
     */
    @Operation(
            summary = "Atualizar vínculo Estoque x Filial",
            description = "Atualiza os dados de um vínculo existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vínculo atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = VinculoEstoqueFilialResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<VinculoEstoqueFilialResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VinculoEstoqueFilialUpdateRequestDto request
    ) {
        VinculoEstoqueFilialResponseDto response = vinculoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca um vínculo entre estoque e filial por identificador.
     *
     * @param id identificador do vínculo
     * @return vínculo encontrado
     */
    @Operation(
            summary = "Buscar vínculo por ID",
            description = "Retorna os dados de um vínculo Estoque x Filial específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = VinculoEstoqueFilialResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<VinculoEstoqueFilialResponseDto> buscarPorId(@PathVariable Long id) {
        VinculoEstoqueFilialResponseDto response = vinculoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista vínculos por filial.
     *
     * @param filialId identificador da filial
     * @param pageable configuração de paginação
     * @return página de vínculos
     */
    @Operation(
            summary = "Listar vínculos por filial",
            description = "Lista os vínculos entre estoque e filial associados a uma filial de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = VinculoEstoqueFilialResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/filial/{filialId}")
    public ResponseEntity<Page<VinculoEstoqueFilialResponseDto>> listarPorFilial(
            @PathVariable Long filialId,
            @ParameterObject Pageable pageable
    ) {
        Page<VinculoEstoqueFilialResponseDto> response = vinculoService.listarPorFilial(filialId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista vínculos por filial filtrando por status.
     *
     * @param filialId identificador da filial
     * @param status status do vínculo
     * @param pageable configuração de paginação
     * @return página de vínculos filtrados
     */
    @Operation(
            summary = "Listar vínculos por filial e status",
            description = "Lista os vínculos entre estoque e filial associados a uma filial, filtrando por status, de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = VinculoEstoqueFilialResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/filial/{filialId}/status/{status}")
    public ResponseEntity<Page<VinculoEstoqueFilialResponseDto>> listarPorFilialEStatus(
            @PathVariable Long filialId,
            @Parameter(description = "Status do vínculo entre estoque e filial")
            @PathVariable StatusVinculoEstoqueFilial status,
            @ParameterObject Pageable pageable
    ) {
        Page<VinculoEstoqueFilialResponseDto> response =
                vinculoService.listarPorFilialEStatus(filialId, status, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um vínculo entre estoque e filial.
     *
     * @param id identificador do vínculo
     * @return resposta sem conteúdo
     */
    @Operation(
            summary = "Remover vínculo Estoque x Filial",
            description = "Remove um vínculo entre estoque e filial pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vínculo removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vinculoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
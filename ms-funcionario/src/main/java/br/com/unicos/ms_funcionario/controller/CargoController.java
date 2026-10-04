package br.com.unicos.ms_funcionario.controller;

import br.com.unicos.ms_funcionario.dto.cargo.CargoRequest;
import br.com.unicos.ms_funcionario.dto.cargo.CargoResponse;
import br.com.unicos.ms_funcionario.service.CargoService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/v1/funcionarios/cargos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Cargos",
        description = "Endpoints para gerenciamento dos cargos e do papel de cada cargo na hierarquia."
)
public class CargoController {

    private final CargoService cargoService;

    @Operation(
            summary = "Criar cargo",
            description = "O papel define as regras do cargo: vendedores acessam apenas a própria carteira de clientes; "
                    + "supervisores, gerentes e diretores acessam todos os clientes da empresa.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cargo criado com sucesso",
                            content = @Content(schema = @Schema(implementation = CargoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou cargo duplicado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<CargoResponse> criar(
            @RequestBody @Validated CargoRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cargoService.salvar(request));
    }

    @Operation(
            summary = "Atualizar cargo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cargo atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = CargoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou cargo duplicado"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Cargo não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Alteração do papel do próprio cargo"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CargoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated CargoRequest request
    ) {
        return ResponseEntity.ok(cargoService.atualizar(id, request));
    }

    @Operation(
            summary = "Buscar cargo por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = CargoResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Cargo não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CargoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(cargoService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar cargos",
            description = "Lista os cargos da empresa de forma paginada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping
    public ResponseEntity<Page<CargoResponse>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(cargoService.listar(pageable));
    }

    @Operation(
            summary = "Remover cargo",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cargo removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Cargo não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Cargo possui funcionários vinculados"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        cargoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

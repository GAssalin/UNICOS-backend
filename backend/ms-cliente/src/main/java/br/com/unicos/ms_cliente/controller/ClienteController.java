package br.com.unicos.ms_cliente.controller;

import br.com.unicos.ms_cliente.dto.ClienteRequestDTO;
import br.com.unicos.ms_cliente.dto.ClienteResponseDTO;
import br.com.unicos.ms_cliente.enums.StatusCliente;
import br.com.unicos.ms_cliente.service.ClienteService;
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
@RequestMapping("/v1/clientes")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Clientes",
        description = "Endpoints para gerenciamento de clientes."
)
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(
            summary = "Criar cliente",
            description = "Cria um novo cliente vinculado a uma pessoa existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cliente criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(
            @RequestBody @Validated ClienteRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.salvar(request));
    }

    @Operation(
            summary = "Atualizar cliente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cliente atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated ClienteRequestDTO request
    ) {
        return ResponseEntity.ok(clienteService.atualizar(id, request));
    }

    @Operation(
            summary = "Buscar cliente por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar clientes",
            description = "Lista clientes do tenant atual de forma paginada.",
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
    public ResponseEntity<Page<ClienteResponseDTO>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(clienteService.listar(pageable));
    }

    @Operation(
            summary = "Listar clientes por status",
            description = "Lista clientes filtrando pelo status comercial.",
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
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ClienteResponseDTO>> listarPorStatus(
            @PathVariable StatusCliente status,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(clienteService.listarPorStatus(status, pageable));
    }

    @Operation(
            summary = "Remover cliente",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
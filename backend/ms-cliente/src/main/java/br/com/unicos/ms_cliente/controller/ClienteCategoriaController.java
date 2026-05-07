package br.com.unicos.ms_cliente.controller;

import br.com.unicos.ms_cliente.dto.ClienteCategoriaRequestDTO;
import br.com.unicos.ms_cliente.dto.ClienteCategoriaResponseDTO;
import br.com.unicos.ms_cliente.service.ClienteCategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/clientes/categorias")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Categorias de Cliente",
        description = "Endpoints para gerenciamento de categorias comerciais de clientes."
)
public class ClienteCategoriaController {

    private final ClienteCategoriaService clienteCategoriaService;

    @Operation(
            summary = "Criar categoria de cliente",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Categoria criada com sucesso",
                            content = @Content(schema = @Schema(implementation = ClienteCategoriaResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ClienteCategoriaResponseDTO> criar(
            @RequestBody @Validated ClienteCategoriaRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteCategoriaService.salvar(request));
    }
}
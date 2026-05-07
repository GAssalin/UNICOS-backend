package br.com.unicos.ms_cliente.controller;

import br.com.unicos.ms_cliente.dto.ClienteObservacaoRequestDTO;
import br.com.unicos.ms_cliente.dto.ClienteObservacaoResponseDTO;
import br.com.unicos.ms_cliente.service.ClienteObservacaoService;
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
@RequestMapping("/v1/clientes/observacoes")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Observações de Cliente",
        description = "Endpoints para gerenciamento de observações internas de clientes."
)
public class ClienteObservacaoController {

    private final ClienteObservacaoService clienteObservacaoService;

    @Operation(
            summary = "Criar observação de cliente",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Observação criada com sucesso",
                            content = @Content(schema = @Schema(implementation = ClienteObservacaoResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<ClienteObservacaoResponseDTO> criar(
            @RequestBody @Validated ClienteObservacaoRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteObservacaoService.salvar(request));
    }
}
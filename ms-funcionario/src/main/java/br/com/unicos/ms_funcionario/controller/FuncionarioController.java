package br.com.unicos.ms_funcionario.controller;

import br.com.unicos.ms_funcionario.dto.funcionario.FuncionarioRequest;
import br.com.unicos.ms_funcionario.dto.funcionario.FuncionarioResponse;
import br.com.unicos.ms_funcionario.enums.PapelFuncionario;
import br.com.unicos.ms_funcionario.enums.StatusFuncionario;
import br.com.unicos.ms_funcionario.service.FuncionarioService;
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
@RequestMapping("/v1/funcionarios")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Funcionários",
        description = "Endpoints para gerenciamento de funcionários, cargos e hierarquia."
)
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @Operation(
            summary = "Criar funcionário",
            description = "Cadastra um funcionário vinculado a uma pessoa e, opcionalmente, a um usuário do sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Funcionário criado com sucesso",
                            content = @Content(schema = @Schema(implementation = FuncionarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos, duplicados ou hierarquia inválida"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para criar"),
                    @ApiResponse(responseCode = "404", description = "Pessoa, usuário, cargo ou superior não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PostMapping
    public ResponseEntity<FuncionarioResponse> criar(
            @RequestBody @Validated FuncionarioRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(funcionarioService.salvar(request));
    }

    @Operation(
            summary = "Atualizar funcionário",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Funcionário atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = FuncionarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos, duplicados ou hierarquia inválida"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para editar"),
                    @ApiResponse(responseCode = "404", description = "Funcionário, pessoa, usuário, cargo ou superior não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Alteração do cargo, status ou usuário do próprio cadastro"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Validated FuncionarioRequest request
    ) {
        return ResponseEntity.ok(funcionarioService.atualizar(id, request));
    }

    @Operation(
            summary = "Consultar o próprio cadastro",
            description = "Cadastro de funcionário do usuário autenticado, com o papel e o escopo de acesso à carteira de clientes. "
                    + "Disponível para qualquer usuário autenticado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = FuncionarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "O usuário não possui cadastro de funcionário")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<FuncionarioResponse> buscarProprioCadastro() {
        return ResponseEntity.ok(funcionarioService.buscarDoUsuarioAtual());
    }

    @Operation(
            summary = "Buscar funcionário por ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = FuncionarioResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para consultar"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar funcionários",
            description = "Lista os funcionários da empresa de forma paginada, com filtros opcionais por status e papel.",
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
    public ResponseEntity<Page<FuncionarioResponse>> listar(
            @RequestParam(required = false) StatusFuncionario status,
            @RequestParam(required = false) PapelFuncionario papel,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(funcionarioService.listar(status, papel, pageable));
    }

    @Operation(
            summary = "Listar subordinados",
            description = "Lista os subordinados diretos do funcionário.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada com sucesso",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para listar"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @GetMapping("/{id}/subordinados")
    public ResponseEntity<Page<FuncionarioResponse>> listarSubordinados(
            @PathVariable Long id,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(funcionarioService.listarSubordinados(id, pageable));
    }

    @Operation(
            summary = "Remover funcionário",
            description = "Para encerrar o vínculo preservando o histórico, prefira atualizar o status para DESLIGADO.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Funcionário removido"),
                    @ApiResponse(responseCode = "403", description = "Sem permissão para remover"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Funcionário possui subordinados ou é o próprio usuário"),
                    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

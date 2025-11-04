package br.com.unicos.ms_pessoas.controller;

import br.com.unicos.ms_pessoas.dto.CargoRequest;
import br.com.unicos.ms_pessoas.dto.CargoResponse;
import br.com.unicos.ms_pessoas.service.CargoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelos cargos (funções) do sistema.
 *
 * <p>Utilizado para gerenciamento dos cargos exercidos pelos colaboradores.</p>
 */
@RestController
@RequestMapping("/v1/cargos")
@RequiredArgsConstructor
public class CargoController {

    private final CargoService cargoService;

    /**
     * Cria um novo cargo.
     *
     * @param request DTO com os dados do cargo
     * @return {@link CargoResponse} criado
     */
    @PostMapping
    public ResponseEntity<CargoResponse> salvar(@Valid @RequestBody CargoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoService.salvar(request));
    }

    /**
     * Atualiza um cargo existente.
     *
     * @param id      identificador do cargo
     * @param request DTO com os novos dados
     * @return {@link CargoResponse} atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<CargoResponse> atualizar(@PathVariable Long id,
                                                   @Valid @RequestBody CargoRequest request) {
        return ResponseEntity.ok(cargoService.atualizar(id, request));
    }

    /**
     * Lista todos os cargos cadastrados.
     *
     * @return lista de {@link CargoResponse}
     */
    @GetMapping
    public ResponseEntity<List<CargoResponse>> listarTodos() {
        return ResponseEntity.ok(cargoService.listarTodos());
    }

    /**
     * Busca um cargo pelo ID.
     *
     * @param id identificador do cargo
     * @return {@link CargoResponse} se encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<CargoResponse> buscarPorId(@PathVariable Long id) {
        return cargoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exclui um cargo.
     *
     * @param id identificador do cargo
     * @return status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        cargoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

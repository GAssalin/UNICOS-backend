package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaListDTO;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaRequest;
import br.com.unicos.ms_produtos.dto.unidade_medida.UnidadeMedidaResponse;
import br.com.unicos.ms_produtos.service.UnidadeMedidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de unidades de medida.
 * <p>
 * Permite cadastrar, atualizar, remover, consultar e realizar buscas
 * por nome, sigla e listagens simples/detalhadas.
 */
@RestController
@RequestMapping("/v1/unidades-medida")
@RequiredArgsConstructor
public class UnidadeMedidaController {

    private final UnidadeMedidaService unidadeMedidaService;

    // ============================================================
    // 🔹 Criar
    // ============================================================

    /**
     * Cadastra uma nova unidade de medida.
     *
     * @param request dados da unidade.
     * @return unidade criada.
     */
    @PostMapping
    public ResponseEntity<UnidadeMedidaResponse> salvar(
            @Valid @RequestBody UnidadeMedidaRequest request) {

        UnidadeMedidaResponse response = unidadeMedidaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // 🔹 Atualizar
    // ============================================================

    /**
     * Atualiza uma unidade de medida existente.
     *
     * @param id      ID da unidade.
     * @param request dados atualizados.
     * @return unidade atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UnidadeMedidaRequest request) {

        UnidadeMedidaResponse response = unidadeMedidaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Deletar
    // ============================================================

    /**
     * Remove uma unidade de medida pelo ID.
     *
     * @param id ID da unidade.
     * @return 204 em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        unidadeMedidaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // 🔹 Buscar por ID
    // ============================================================

    /**
     * Busca uma unidade de medida pelo ID.
     *
     * @param id ID da unidade.
     * @return unidade encontrada ou 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorId(@PathVariable Long id) {

        Optional<UnidadeMedidaResponse> resultado =
                unidadeMedidaService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // 🔹 Listagem detalhada
    // ============================================================

    /**
     * Lista todas as unidades de medida com informações completas.
     *
     * @return lista detalhada.
     */
    @GetMapping
    public ResponseEntity<List<UnidadeMedidaResponse>> listarTodas() {
        return ResponseEntity.ok(unidadeMedidaService.listarTodas());
    }

    // ============================================================
    // 🔹 Listagem simples
    // ============================================================

    /**
     * Lista unidades de medida em formato simplificado.
     *
     * @return lista simples ordenada por nome.
     */
    @GetMapping("/simples")
    public ResponseEntity<List<UnidadeMedidaListDTO>> listarSimples() {
        return ResponseEntity.ok(unidadeMedidaService.listarSimples());
    }

    // ============================================================
    // 🔹 Buscar por nome exato
    // ============================================================

    /**
     * Busca uma unidade de medida pelo nome exato.
     *
     * @param nome nome da unidade.
     * @return unidade encontrada ou 404.
     */
    @GetMapping("/nome")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorNome(
            @RequestParam String nome) {

        Optional<UnidadeMedidaResponse> resultado =
                unidadeMedidaService.buscarPorNome(nome);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // 🔹 Buscar por sigla exata
    // ============================================================

    /**
     * Busca uma unidade de medida pela sigla.
     *
     * @param sigla sigla da unidade (ex.: kg, un, cx).
     * @return unidade encontrada ou 404.
     */
    @GetMapping("/sigla")
    public ResponseEntity<UnidadeMedidaResponse> buscarPorSigla(
            @RequestParam String sigla) {

        Optional<UnidadeMedidaResponse> resultado =
                unidadeMedidaService.buscarPorSigla(sigla);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // 🔹 Buscar por nome contendo
    // ============================================================

    /**
     * Busca unidades cujo nome contenha o texto informado.
     *
     * @param nome texto parcial do nome.
     * @return lista de unidades encontradas.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<UnidadeMedidaListDTO>> buscarPorNomeContendo(
            @RequestParam String nome) {

        return ResponseEntity.ok(
                unidadeMedidaService.buscarPorNomeContendo(nome)
        );
    }

    // ============================================================
    // 🔹 Verificar sigla existente
    // ============================================================

    /**
     * Verifica se já existe uma unidade cadastrada com a sigla informada.
     *
     * @param sigla sigla a verificar.
     * @return true se existir, false caso contrário.
     */
    @GetMapping("/sigla/existe")
    public ResponseEntity<Boolean> verificarSiglaExistente(
            @RequestParam String sigla) {

        return ResponseEntity.ok(
                unidadeMedidaService.verificarSiglaExistente(sigla)
        );
    }
}

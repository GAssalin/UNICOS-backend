package br.com.unicos.ms_produtos.controller;

import br.com.unicos.ms_produtos.dto.marca.MarcaListDTO;
import br.com.unicos.ms_produtos.dto.marca.MarcaRequest;
import br.com.unicos.ms_produtos.dto.marca.MarcaResponse;
import br.com.unicos.ms_produtos.service.MarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de marcas de produtos.
 * <p>
 * Permite cadastrar, editar, remover, listar e buscar marcas
 * com endpoints organizados e padronizados.
 */
@RestController
@RequestMapping("/v1/marcas")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    // ============================================================
    // 🔹 Criar marca
    // ============================================================

    /**
     * Cria uma nova marca.
     *
     * @param request dados da marca.
     * @return marca criada.
     */
    @PostMapping
    public ResponseEntity<MarcaResponse> salvar(
            @Valid @RequestBody MarcaRequest request) {

        MarcaResponse response = marcaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // 🔹 Atualizar marca
    // ============================================================

    /**
     * Atualiza uma marca existente.
     *
     * @param id      ID da marca.
     * @param request dados atualizados.
     * @return marca atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequest request) {

        MarcaResponse response = marcaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 🔹 Buscar por ID
    // ============================================================

    /**
     * Busca uma marca pelo ID.
     *
     * @param id ID da marca.
     * @return marca encontrada ou 404 se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponse> buscarPorId(@PathVariable Long id) {

        Optional<MarcaResponse> resultado = marcaService.buscarPorId(id);

        return resultado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================================
    // 🔹 Listar todas as marcas (detalhado)
    // ============================================================

    /**
     * Lista todas as marcas cadastradas.
     *
     * @return lista completa de marcas.
     */
    @GetMapping
    public ResponseEntity<List<MarcaResponse>> listarTodas() {
        return ResponseEntity.ok(marcaService.listarTodas());
    }

    // ============================================================
    // 🔹 Listagem simplificada
    // ============================================================

    /**
     * Lista marcas em formato simplificado.
     *
     * @return lista simples de marcas.
     */
    @GetMapping("/simples")
    public ResponseEntity<List<MarcaListDTO>> listarSimples() {
        return ResponseEntity.ok(marcaService.listarSimples());
    }

    // ============================================================
    // 🔹 Buscar marcas por nome (contém)
    // ============================================================

    /**
     * Busca marcas pelo nome (contém, ignore case).
     *
     * @param nome nome ou parte do nome.
     * @return lista de marcas encontradas.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<MarcaResponse>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(marcaService.buscarPorNome(nome));
    }

    // ============================================================
    // 🔹 Deletar marca
    // ============================================================

    /**
     * Remove uma marca pelo ID.
     *
     * @param id ID da marca.
     * @return 204 se removida.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        marcaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // 🔹 Verificar se existe marca por nome
    // ============================================================

    /**
     * Verifica se existe uma marca com o nome informado.
     *
     * @param nome nome da marca.
     * @return true ou false.
     */
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existePorNome(@RequestParam String nome) {
        return ResponseEntity.ok(marcaService.existePorNome(nome));
    }
}

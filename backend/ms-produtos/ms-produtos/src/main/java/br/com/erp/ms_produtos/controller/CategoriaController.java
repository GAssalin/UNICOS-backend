package br.com.erp.ms_produtos.controller;

import br.com.erp.ms_produtos.dto.CategoriaListDTO;
import br.com.erp.ms_produtos.dto.CategoriaRequestDTO;
import br.com.erp.ms_produtos.dto.CategoriaResponseDTO;
import br.com.erp.ms_produtos.service.impl.CategoriaServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável por gerenciar as operações relacionadas às Categorias de produtos.
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaServiceImpl service;

    public CategoriaController(CategoriaServiceImpl service) {
        this.service = service;
    }

    /**
     * Lista todas as categorias com informações completas.
     *
     * @return Lista de CategoriaResponseDTO.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        List<CategoriaResponseDTO> lista = service.listarTodos()
                .stream()
                .map(c -> CategoriaResponseDTO.builder()
                        .id(c.getId())
                        .nome(c.getNome())
                        .descricao(c.getDescricao())
                        .build())
                .toList();

        return ResponseEntity.ok(lista);
    }

    /**
     * Lista todas as categorias em formato reduzido (id e nome).
     *
     * @return Lista de CategoriaListDTO.
     */
    @GetMapping("/resumido")
    public ResponseEntity<List<CategoriaListDTO>> listarResumido() {
        return ResponseEntity.ok(service.listarResumido());
    }

    /**
     * Busca uma categoria específica pelo ID.
     *
     * @param id ID da categoria.
     * @return CategoriaResponseDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarDTOporId(id));
    }

    /**
     * Cria uma nova categoria.
     *
     * @param dto Dados da nova categoria.
     * @return CategoriaResponseDTO.
     */
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO criada = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    /**
     * Atualiza uma categoria existente.
     *
     * @param id  ID da categoria.
     * @param dto Dados atualizados da categoria.
     * @return CategoriaResponseDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO atualizada = service.atualizarDTO(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    /**
     * Remove uma categoria pelo ID.
     *
     * @param id ID da categoria.
     * @return Mensagem de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.ok("Categoria removida com sucesso!");
    }
}
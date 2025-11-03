package br.com.unicos.ms_empresa.controller;

import br.com.unicos.ms_empresa.dto.FilialListDTO;
import br.com.unicos.ms_empresa.dto.FilialRequest;
import br.com.unicos.ms_empresa.dto.FilialResponse;
import br.com.unicos.ms_empresa.service.FilialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelo gerenciamento das filiais das empresas.
 *
 * Fornece endpoints REST para criação, atualização, listagem, busca e exclusão de filiais.
 */
@RestController
@RequestMapping("/v1/filiais")
@RequiredArgsConstructor
public class FilialController {

    private final FilialService filialService;

    /**
     * Cria uma nova filial.
     *
     * @param request dados da filial a ser criada
     * @return filial criada
     */
    @PostMapping
    public ResponseEntity<FilialResponse> criar(@Valid @RequestBody FilialRequest request) {
        FilialResponse response = filialService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma filial existente.
     *
     * @param id identificador da filial
     * @param request dados atualizados
     * @return filial atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<FilialResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FilialRequest request) {
        FilialResponse response = filialService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma filial pelo seu ID.
     *
     * @param id identificador da filial
     * @return filial encontrada, se existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<FilialResponse> buscarPorId(@PathVariable Long id) {
        FilialResponse response = filialService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as filiais vinculadas a uma empresa específica.
     *
     * @param empresaId identificador da empresa
     * @return lista de filiais
     */
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<FilialListDTO>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<FilialListDTO> filiais = filialService.findByEmpresa(empresaId);
        if (filiais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filiais);
    }

    /**
     * Busca uma filial pelo CNPJ informado.
     *
     * @param cnpj número do CNPJ
     * @return filial encontrada, se existir
     */
    @GetMapping("/buscar/cnpj")
    public ResponseEntity<FilialResponse> buscarPorCnpj(@RequestParam String cnpj) {
        return filialService.findByCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista filiais de uma cidade específica.
     *
     * @param cidade nome da cidade
     * @return lista de filiais
     */
    @GetMapping("/buscar/cidade")
    public ResponseEntity<List<FilialListDTO>> buscarPorCidade(@RequestParam String cidade) {
        List<FilialListDTO> filiais = filialService.findByCidade(cidade);
        if (filiais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filiais);
    }

    /**
     * Lista filiais de uma determinada UF.
     *
     * @param uf sigla da unidade federativa
     * @return lista de filiais
     */
    @GetMapping("/buscar/uf")
    public ResponseEntity<List<FilialListDTO>> buscarPorUf(@RequestParam String uf) {
        List<FilialListDTO> filiais = filialService.findByUf(uf);
        if (filiais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filiais);
    }

    /**
     * Lista todas as filiais ordenadas alfabeticamente pela razão social.
     *
     * @return lista de filiais ordenadas
     */
    @GetMapping("/ordenadas")
    public ResponseEntity<List<FilialListDTO>> listarOrdenadas() {
        List<FilialListDTO> filiais = filialService.findAllOrderedByRazaoSocial();
        if (filiais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filiais);
    }

    /**
     * Exclui uma filial pelo seu ID.
     *
     * @param id identificador da filial
     * @return resposta sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        filialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
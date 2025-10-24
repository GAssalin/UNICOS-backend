package br.com.erp.ms_produtos.service;

import br.com.erp.ms_produtos.model.Marca;

import java.util.List;
import java.util.Optional;

public interface MarcaService extends CrudService<Marca, Long> {
    Optional<Marca> buscarPorNome(String nome);

    List<Marca> listarTodas();
}
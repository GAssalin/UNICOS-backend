package br.com.unicos.ms_pessoas.repository;

import br.com.unicos.ms_pessoas.model.DocumentoPessoa;
import br.com.unicos.ms_pessoas.enums.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo gerenciamento de {@link DocumentoPessoa}.
 */
@Repository
public interface DocumentoPessoaRepository extends JpaRepository<DocumentoPessoa, Long> {

    /**
     * Busca todos os documentos de uma pessoa.
     */
    List<DocumentoPessoa> findByPessoaId(Long pessoaId);

    /**
     * Busca um documento específico de uma pessoa pelo tipo.
     */
    DocumentoPessoa findFirstByPessoaIdAndTipoDocumento(Long pessoaId, TipoDocumento tipoDocumento);
}

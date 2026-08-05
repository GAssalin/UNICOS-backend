package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoRequest;
import br.com.unicos.ms_pessoas.dto.relacao.PessoaRelacaoResponse;
import br.com.unicos.ms_pessoas.model.Pessoa;
import br.com.unicos.ms_pessoas.model.PessoaRelacao;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link PessoaRelacao}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class PessoaRelacaoMapper {

    /**
     * Converte uma entidade {@link PessoaRelacao} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos da relação entre pessoas
     */
    public PessoaRelacaoResponse toResponse(PessoaRelacao entity) {
        if (entity == null)
            return null;

        return new PessoaRelacaoResponse(
                entity.getId(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getRelacionado() != null ? entity.getRelacionado().getId() : null,
                entity.getTipoRelacao() != null ? entity.getTipoRelacao().getId() : null
        );
    }

    /**
     * Converte uma entidade {@link PessoaRelacao} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public PessoaRelacaoListDTO toListDTO(PessoaRelacao entity) {
        if (entity == null)
            return null;

        return new PessoaRelacaoListDTO(
                entity.getId(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getRelacionado() != null ? entity.getRelacionado().getId() : null,
                entity.getTipoRelacao() != null ? entity.getTipoRelacao().getId() : null
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link PessoaRelacao}.
     * <p>
     * Como o request contém apenas IDs, é necessário receber as entidades
     * já carregadas para compor os relacionamentos.
     *
     * @param request DTO contendo os dados de entrada
     * @param pessoa entidade Pessoa principal já carregada
     * @param relacionado entidade Pessoa relacionada já carregada
     * @param tipoRelacao entidade TipoRelacaoPessoa já carregada
     * @return nova entidade preenchida com os dados informados
     */
    public PessoaRelacao toEntity(
            PessoaRelacaoRequest request,
            Pessoa pessoa,
            Pessoa relacionado,
            TipoRelacaoPessoa tipoRelacao
    ) {
        if (request == null)
            return null;

        return PessoaRelacao.builder()
                .pessoa(pessoa)
                .relacionado(relacionado)
                .tipoRelacao(tipoRelacao)
                .build();
    }

    /**
     * Atualiza uma entidade {@link PessoaRelacao} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity entidade a ser atualizada
     * @param request DTO com os novos dados
     * @param pessoa entidade Pessoa principal já carregada
     * @param relacionado entidade Pessoa relacionada já carregada
     * @param tipoRelacao entidade TipoRelacaoPessoa já carregada
     */
    public void updateEntity(
            PessoaRelacao entity,
            PessoaRelacaoRequest request,
            Pessoa pessoa,
            Pessoa relacionado,
            TipoRelacaoPessoa tipoRelacao
    ) {
        if (entity == null || request == null)
            return;

        entity.setPessoa(pessoa);
        entity.setRelacionado(relacionado);
        entity.setTipoRelacao(tipoRelacao);
    }

    /**
     * Converte uma lista de entidades {@link PessoaRelacao} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<PessoaRelacaoResponse> toResponseList(List<PessoaRelacao> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link PessoaRelacao} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<PessoaRelacaoListDTO> toListDTOList(List<PessoaRelacao> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}
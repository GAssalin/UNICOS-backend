package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaListDTO;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaRequest;
import br.com.unicos.ms_pessoas.dto.relacao.TipoRelacaoPessoaResponse;
import br.com.unicos.ms_pessoas.model.TipoRelacaoPessoa;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link TipoRelacaoPessoa}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class TipoRelacaoPessoaMapper {

    /**
     * Converte uma entidade {@link TipoRelacaoPessoa} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos do tipo de relação
     */
    public TipoRelacaoPessoaResponse toResponse(TipoRelacaoPessoa entity) {
        if (entity == null)
            return null;

        return new TipoRelacaoPessoaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }

    /**
     * Converte uma entidade {@link TipoRelacaoPessoa} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public TipoRelacaoPessoaListDTO toListDTO(TipoRelacaoPessoa entity) {
        if (entity == null)
            return null;

        return new TipoRelacaoPessoaListDTO(
                entity.getId(),
                entity.getNome()
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link TipoRelacaoPessoa}.
     *
     * @param request DTO contendo os dados de entrada
     * @return nova entidade preenchida com os dados informados
     */
    public TipoRelacaoPessoa toEntity(TipoRelacaoPessoaRequest request) {
        if (request == null)
            return null;

        return TipoRelacaoPessoa.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();
    }

    /**
     * Atualiza uma entidade {@link TipoRelacaoPessoa} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity entidade a ser atualizada
     * @param request DTO com os novos dados
     */
    public void updateEntity(TipoRelacaoPessoa entity, TipoRelacaoPessoaRequest request) {
        if (entity == null || request == null)
            return;

        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
    }

    /**
     * Converte uma lista de entidades {@link TipoRelacaoPessoa} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<TipoRelacaoPessoaResponse> toResponseList(List<TipoRelacaoPessoa> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link TipoRelacaoPessoa} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<TipoRelacaoPessoaListDTO> toListDTOList(List<TipoRelacaoPessoa> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}
package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaResponse;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento dos atributos comuns entre
 * a entidade abstrata {@link Pessoa} e seus respectivos DTOs.
 */
@Component
public class PessoaMapper {

    /**
     * Converte uma entidade {@link Pessoa} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados básicos da pessoa
     */
    public PessoaResponse toResponse(Pessoa entity) {
        if (entity == null)
            return null;

        return new PessoaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getTipoPessoa()
        );
    }

    /**
     * Converte uma entidade {@link Pessoa} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public PessoaListDTO toListDTO(Pessoa entity) {
        if (entity == null)
            return null;

        return new PessoaListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getTipoPessoa()
        );
    }

    /**
     * Preenche uma nova instância de uma subclasse de {@link Pessoa}
     * com os dados básicos informados no DTO de requisição.
     * <p>
     * Este método deve ser utilizado apenas com subclasses concretas,
     * como {@code PessoaFisica} e {@code PessoaJuridica}, pois
     * {@link Pessoa} é uma classe abstrata.
     *
     * @param request DTO contendo os dados de entrada
     * @param entity instância concreta da subclasse de Pessoa
     * @param <T> tipo da entidade concreta
     * @return entidade preenchida com os dados básicos
     */
    public <T extends Pessoa> T toEntity(PessoaRequest request, T entity) {
        if (request == null || entity == null)
            return null;

        entity.setNome(request.nome());
        entity.setTipoPessoa(request.tipoPessoa());
        return entity;
    }

    /**
     * Atualiza os atributos básicos de uma entidade {@link Pessoa}
     * existente com os dados informados no DTO de requisição.
     *
     * @param entity entidade a ser atualizada
     * @param request DTO com os novos dados
     */
    public void updateEntity(Pessoa entity, PessoaRequest request) {
        if (entity == null || request == null)
            return;

        entity.setNome(request.nome());
        entity.setTipoPessoa(request.tipoPessoa());
    }

    /**
     * Converte uma lista de entidades {@link Pessoa} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<PessoaResponse> toResponseList(List<? extends Pessoa> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link Pessoa} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<PessoaListDTO> toListDTOList(List<? extends Pessoa> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}
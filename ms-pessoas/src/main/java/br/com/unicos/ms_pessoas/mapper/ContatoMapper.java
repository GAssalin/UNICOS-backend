package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.contato.ContatoListDTO;
import br.com.unicos.ms_pessoas.dto.contato.ContatoRequest;
import br.com.unicos.ms_pessoas.dto.contato.ContatoResponse;
import br.com.unicos.ms_pessoas.model.Contato;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link Contato}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class ContatoMapper {

    /**
     * Converte uma entidade {@link Contato} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos do contato
     */
    public ContatoResponse toResponse(Contato entity) {
        if (entity == null)
            return null;

        return new ContatoResponse(
                entity.getId(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getTipo(),
                entity.getValor(),
                entity.isPrincipal()
        );
    }

    /**
     * Converte uma entidade {@link Contato} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public ContatoListDTO toListDTO(Contato entity) {
        if (entity == null)
            return null;

        return new ContatoListDTO(
                entity.getId(),
                entity.getTipo(),
                entity.getValor(),
                entity.isPrincipal()
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link Contato}.
     * <p>
     * Como o request contém apenas o ID da pessoa, é necessário receber
     * a entidade {@link Pessoa} já carregada para compor o relacionamento.
     *
     * @param request DTO contendo os dados de entrada
     * @param pessoa  entidade Pessoa já carregada
     * @return nova entidade preenchida com os dados informados
     */
    public Contato toEntity(ContatoRequest request, Pessoa pessoa) {
        if (request == null)
            return null;

        return Contato.builder()
                .pessoa(pessoa)
                .tipo(request.tipo())
                .valor(request.valor())
                .principal(Boolean.TRUE.equals(request.principal()))
                .build();
    }

    /**
     * Atualiza uma entidade {@link Contato} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity  entidade a ser atualizada
     * @param request DTO com os novos dados
     * @param pessoa  entidade Pessoa já carregada
     */
    public void updateEntity(Contato entity, ContatoRequest request, Pessoa pessoa) {
        if (entity == null || request == null)
            return;

        entity.setPessoa(pessoa);
        entity.setTipo(request.tipo());
        entity.setValor(request.valor());
        entity.setPrincipal(Boolean.TRUE.equals(request.principal()));
    }

    /**
     * Converte uma lista de entidades {@link Contato} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<ContatoResponse> toResponseList(List<Contato> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link Contato} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<ContatoListDTO> toListDTOList(List<Contato> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}
package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaFisicaResponse;
import br.com.unicos.ms_pessoas.model.PessoaFisica;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link PessoaFisica}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class PessoaFisicaMapper {

    /**
     * Converte uma entidade {@link PessoaFisica} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos da pessoa física
     */
    public PessoaFisicaResponse toResponse(PessoaFisica entity) {
        if (entity == null)
            return null;

        return new PessoaFisicaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getDataNascimento(),
                entity.getNomeSocial()
        );
    }

    /**
     * Converte uma entidade {@link PessoaFisica} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public PessoaFisicaListDTO toListDTO(PessoaFisica entity) {
        if (entity == null)
            return null;

        return new PessoaFisicaListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCpf()
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link PessoaFisica}.
     *
     * @param request DTO contendo os dados de entrada
     * @return nova entidade preenchida com os dados informados
     */
    public PessoaFisica toEntity(PessoaFisicaRequest request) {
        if (request == null)
            return null;

        return PessoaFisica.builder()
                .nome(request.nome())
                .cpf(request.cpf())
                .dataNascimento(request.dataNascimento())
                .nomeSocial(request.nomeSocial())
                .build();
    }

    /**
     * Atualiza uma entidade {@link PessoaFisica} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity entidade a ser atualizada
     * @param request DTO com os novos dados
     */
    public void updateEntity(PessoaFisica entity, PessoaFisicaRequest request) {
        if (entity == null || request == null)
            return;

        entity.setNome(request.nome());
        entity.setCpf(request.cpf());
        entity.setDataNascimento(request.dataNascimento());
        entity.setNomeSocial(request.nomeSocial());
    }

    /**
     * Converte uma lista de entidades {@link PessoaFisica} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<PessoaFisicaResponse> toResponseList(List<PessoaFisica> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link PessoaFisica} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<PessoaFisicaListDTO> toListDTOList(List<PessoaFisica> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}
package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaListDTO;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaRequest;
import br.com.unicos.ms_pessoas.dto.pessoa.PessoaJuridicaResponse;
import br.com.unicos.ms_pessoas.model.PessoaJuridica;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link PessoaJuridica}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class PessoaJuridicaMapper {

    /**
     * Converte uma entidade {@link PessoaJuridica} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos da pessoa jurídica
     */
    public PessoaJuridicaResponse toResponse(PessoaJuridica entity) {
        if (entity == null)
            return null;

        return new PessoaJuridicaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getRazaoSocial(),
                entity.getNomeFantasia()
        );
    }

    /**
     * Converte uma entidade {@link PessoaJuridica} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public PessoaJuridicaListDTO toListDTO(PessoaJuridica entity) {
        if (entity == null)
            return null;

        return new PessoaJuridicaListDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj()
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link PessoaJuridica}.
     *
     * @param request DTO contendo os dados de entrada
     * @return nova entidade preenchida com os dados informados
     */
    public PessoaJuridica toEntity(PessoaJuridicaRequest request) {
        if (request == null)
            return null;

        return PessoaJuridica.builder()
                .nome(request.nome())
                .cnpj(request.cnpj())
                .razaoSocial(request.razaoSocial())
                .nomeFantasia(request.nomeFantasia())
                .build();
    }

    /**
     * Atualiza uma entidade {@link PessoaJuridica} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity entidade a ser atualizada
     * @param request DTO com os novos dados
     */
    public void updateEntity(PessoaJuridica entity, PessoaJuridicaRequest request) {
        if (entity == null || request == null)
            return;

        entity.setNome(request.nome());
        entity.setCnpj(request.cnpj());
        entity.setRazaoSocial(request.razaoSocial());
        entity.setNomeFantasia(request.nomeFantasia());
    }

    /**
     * Converte uma lista de entidades {@link PessoaJuridica} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<PessoaJuridicaResponse> toResponseList(List<PessoaJuridica> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link PessoaJuridica} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<PessoaJuridicaListDTO> toListDTOList(List<PessoaJuridica> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}
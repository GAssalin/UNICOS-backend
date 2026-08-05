package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.endereco.EnderecoListDTO;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoRequest;
import br.com.unicos.ms_pessoas.dto.endereco.EnderecoResponse;
import br.com.unicos.ms_pessoas.model.Endereco;
import br.com.unicos.ms_pessoas.model.Municipio;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link Endereco}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class EnderecoMapper {

    /**
     * Converte uma entidade {@link Endereco} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos do endereço
     */
    public EnderecoResponse toResponse(Endereco entity) {
        if (entity == null)
            return null;

        return new EnderecoResponse(
                entity.getId(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getTipo(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getMunicipio() != null ? entity.getMunicipio().getId() : null,
                entity.getCep(),
                entity.isPrincipal()
        );
    }

    /**
     * Converte uma entidade {@link Endereco} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public EnderecoListDTO toListDTO(Endereco entity) {
        if (entity == null)
            return null;

        return new EnderecoListDTO(
                entity.getId(),
                entity.getTipo(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getBairro(),
                entity.getCep(),
                entity.isPrincipal()
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link Endereco}.
     * <p>
     * Como o request contém apenas os IDs da pessoa e do município, é necessário
     * receber as entidades já carregadas para compor os relacionamentos.
     *
     * @param request   DTO contendo os dados de entrada
     * @param pessoa    entidade Pessoa já carregada
     * @param municipio entidade Municipio já carregada
     * @return nova entidade preenchida com os dados informados
     */
    public Endereco toEntity(EnderecoRequest request, Pessoa pessoa, Municipio municipio) {
        if (request == null)
            return null;

        return Endereco.builder()
                .pessoa(pessoa)
                .tipo(request.tipo())
                .logradouro(request.logradouro())
                .numero(request.numero())
                .complemento(request.complemento())
                .bairro(request.bairro())
                .municipio(municipio)
                .cep(request.cep())
                .principal(Boolean.TRUE.equals(request.principal()))
                .build();
    }

    /**
     * Atualiza uma entidade {@link Endereco} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity    entidade a ser atualizada
     * @param request   DTO com os novos dados
     * @param pessoa    entidade Pessoa já carregada
     * @param municipio entidade Municipio já carregada
     */
    public void updateEntity(Endereco entity, EnderecoRequest request, Pessoa pessoa, Municipio municipio) {
        if (entity == null || request == null)
            return;

        entity.setPessoa(pessoa);
        entity.setTipo(request.tipo());
        entity.setLogradouro(request.logradouro());
        entity.setNumero(request.numero());
        entity.setComplemento(request.complemento());
        entity.setBairro(request.bairro());
        entity.setMunicipio(municipio);
        entity.setCep(request.cep());
        entity.setPrincipal(Boolean.TRUE.equals(request.principal()));
    }

    /**
     * Converte uma lista de entidades {@link Endereco} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<EnderecoResponse> toResponseList(List<Endereco> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link Endereco} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<EnderecoListDTO> toListDTOList(List<Endereco> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}
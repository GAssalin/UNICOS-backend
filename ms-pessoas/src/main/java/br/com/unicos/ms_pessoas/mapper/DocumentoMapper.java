package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.documento.DocumentoListDTO;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoRequest;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoResponse;
import br.com.unicos.ms_pessoas.model.Documento;
import br.com.unicos.ms_pessoas.model.Pessoa;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pelo mapeamento entre a entidade {@link Documento}
 * e seus respectivos DTOs de entrada e saída.
 */
@Component
public class DocumentoMapper {

    /**
     * Converte uma entidade {@link Documento} em um DTO de resposta detalhada.
     *
     * @param entity entidade a ser convertida
     * @return DTO com os dados completos do documento
     */
    public DocumentoResponse toResponse(Documento entity) {
        if (entity == null)
            return null;

        return new DocumentoResponse(
                entity.getId(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getTipo(),
                entity.getNumero(),
                entity.getOrgaoEmissor(),
                entity.getDataEmissao()
        );
    }

    /**
     * Converte uma entidade {@link Documento} em um DTO de listagem.
     *
     * @param entity entidade a ser convertida
     * @return DTO resumido para listagem
     */
    public DocumentoListDTO toListDTO(Documento entity) {
        if (entity == null)
            return null;

        return new DocumentoListDTO(
                entity.getId(),
                entity.getTipo(),
                entity.getNumero()
        );
    }

    /**
     * Converte um DTO de requisição em uma nova entidade {@link Documento}.
     * <p>
     * Como o request contém apenas o ID da pessoa, é necessário receber
     * a entidade {@link Pessoa} já carregada para compor o relacionamento.
     *
     * @param request DTO contendo os dados de entrada
     * @param pessoa  entidade Pessoa já carregada
     * @return nova entidade preenchida com os dados informados
     */
    public Documento toEntity(DocumentoRequest request, Pessoa pessoa) {
        if (request == null)
            return null;

        return Documento.builder()
                .pessoa(pessoa)
                .tipo(request.tipo())
                .numero(request.numero())
                .orgaoEmissor(request.orgaoEmissor())
                .dataEmissao(request.dataEmissao())
                .build();
    }

    /**
     * Atualiza uma entidade {@link Documento} existente com os dados
     * informados no DTO de requisição.
     *
     * @param entity  entidade a ser atualizada
     * @param request DTO com os novos dados
     * @param pessoa  entidade Pessoa já carregada
     */
    public void updateEntity(Documento entity, DocumentoRequest request, Pessoa pessoa) {
        if (entity == null || request == null)
            return;

        entity.setPessoa(pessoa);
        entity.setTipo(request.tipo());
        entity.setNumero(request.numero());
        entity.setOrgaoEmissor(request.orgaoEmissor());
        entity.setDataEmissao(request.dataEmissao());
    }

    /**
     * Converte uma lista de entidades {@link Documento} em uma lista
     * de DTOs de resposta detalhada.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de resposta
     */
    public List<DocumentoResponse> toResponseList(List<Documento> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link Documento} em uma lista
     * de DTOs de listagem resumida.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<DocumentoListDTO> toListDTOList(List<Documento> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toListDTO)
                .toList();
    }
}
package br.com.unicos.ms_pessoas.mapper;

import br.com.unicos.ms_pessoas.dto.documento.DocumentoListDTO;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoRequest;
import br.com.unicos.ms_pessoas.dto.documento.DocumentoResponse;
import br.com.unicos.ms_pessoas.model.Documento;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentoMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade para DTO detalhado.
     */
    public DocumentoResponse toResponse(Documento entity) {
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
     * Converte a entidade para DTO de listagem.
     */
    public DocumentoListDTO toListDTO(Documento entity) {
        return new DocumentoListDTO(
                entity.getId(),
                entity.getTipo(),
                entity.getNumero()
        );
    }

    /**
     * Converte o DTO request para entidade.
     */
    public Documento toEntity(DocumentoRequest request) {
        return mapper.map(request, Documento.class);
    }
}

package br.com.unicos.ms_filial.mapper;

import br.com.unicos.ms_filial.dto.endereco.EnderecoFilialCreateRequest;
import br.com.unicos.ms_filial.dto.endereco.EnderecoFilialResponse;
import br.com.unicos.ms_filial.dto.endereco.EnderecoFilialUpdateRequest;
import br.com.unicos.ms_filial.model.EnderecoFilial;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade EnderecoFilial e seus DTOs.
 */
@Component
@RequiredArgsConstructor
public class EnderecoFilialMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade EnderecoFilial para DTO de resposta.
     */
    public EnderecoFilialResponse toResponse(EnderecoFilial entity) {
        return new EnderecoFilialResponse(
                entity.getId(),
                entity.getFilialId(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getUf(),
                entity.getCep()
        );
    }

    /**
     * Converte DTO de criação para entidade EnderecoFilial.
     *
     * <p>
     * Campos como {@code id} e auditoria devem ser definidos no service.
     * </p>
     */
    public EnderecoFilial toEntity(EnderecoFilialCreateRequest request) {
        return mapper.map(request, EnderecoFilial.class);
    }

    /**
     * Atualiza uma entidade EnderecoFilial existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e auditoria não devem ser alterados.
     * Se {@code filialId} for imutável na sua regra, não atualize aqui.
     * </p>
     */
    public void updateEntity(EnderecoFilialUpdateRequest request, EnderecoFilial entity) {
        entity.setFilialId(request.filialId());
        entity.setLogradouro(request.logradouro());
        entity.setNumero(request.numero());
        entity.setComplemento(request.complemento());
        entity.setBairro(request.bairro());
        entity.setCidade(request.cidade());
        entity.setUf(request.uf());
        entity.setCep(request.cep());
    }
}

package br.com.unicos.ms_filial.mapper;

import br.com.unicos.ms_filial.dto.contato.ContatoFilialCreateRequest;
import br.com.unicos.ms_filial.dto.contato.ContatoFilialResponse;
import br.com.unicos.ms_filial.dto.contato.ContatoFilialUpdateRequest;
import br.com.unicos.ms_filial.model.ContatoFilial;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade ContatoFilial e seus DTOs.
 */
@Component
@RequiredArgsConstructor
public class ContatoFilialMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade ContatoFilial para DTO de resposta.
     */
    public ContatoFilialResponse toResponse(ContatoFilial entity) {
        return new ContatoFilialResponse(
                entity.getId(),
                entity.getFilialId(),
                entity.getTelefonePrincipal(),
                entity.getTelefoneSecundario(),
                entity.getEmailPrincipal(),
                entity.getEmailSecundario(),
                entity.getNomeResponsavel()
        );
    }

    /**
     * Converte DTO de criação para entidade ContatoFilial.
     *
     * <p>
     * Campos como {@code id} e auditoria devem ser definidos no service.
     * </p>
     */
    public ContatoFilial toEntity(ContatoFilialCreateRequest request) {
        return mapper.map(request, ContatoFilial.class);
    }

    /**
     * Atualiza uma entidade ContatoFilial existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e auditoria não devem ser alterados.
     * Se {@code filialId} for imutável na sua regra, não atualize aqui.
     * </p>
     */
    public void updateEntity(ContatoFilialUpdateRequest request, ContatoFilial entity) {
        entity.setFilialId(request.filialId());
        entity.setTelefonePrincipal(request.telefonePrincipal());
        entity.setTelefoneSecundario(request.telefoneSecundario());
        entity.setEmailPrincipal(request.emailPrincipal());
        entity.setEmailSecundario(request.emailSecundario());
        entity.setNomeResponsavel(request.nomeResponsavel());
    }
}

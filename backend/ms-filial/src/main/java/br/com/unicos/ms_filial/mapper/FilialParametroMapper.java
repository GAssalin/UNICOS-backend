package br.com.unicos.ms_filial.mapper;

import br.com.unicos.ms_filial.dto.parametro.FilialParametroCreateRequest;
import br.com.unicos.ms_filial.dto.parametro.FilialParametroResponse;
import br.com.unicos.ms_filial.dto.parametro.FilialParametroUpdateRequest;
import br.com.unicos.ms_filial.model.FilialParametro;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade FilialParametro e seus DTOs.
 */
@Component
@RequiredArgsConstructor
public class FilialParametroMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade FilialParametro para DTO de resposta.
     */
    public FilialParametroResponse toResponse(FilialParametro entity) {
        return new FilialParametroResponse(
                entity.getId(),
                entity.getFilialId(),
                entity.getChave(),
                entity.getValor(),
                entity.getDescricao(),
                entity.getAtivo()
        );
    }

    /**
     * Converte DTO de criação para entidade FilialParametro.
     *
     * <p>
     * Campos como {@code id} e auditoria devem ser definidos no service.
     * </p>
     */
    public FilialParametro toEntity(FilialParametroCreateRequest request) {
        return mapper.map(request, FilialParametro.class);
    }

    /**
     * Atualiza uma entidade FilialParametro existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e auditoria não devem ser alterados.
     * Se {@code chave} for imutável na sua regra, não atualize aqui.
     * </p>
     */
    public void updateEntity(FilialParametroUpdateRequest request, FilialParametro entity) {
        entity.setFilialId(request.filialId());
        entity.setChave(request.chave());
        entity.setValor(request.valor());
        entity.setDescricao(request.descricao());
        entity.setAtivo(request.ativo());
    }
}

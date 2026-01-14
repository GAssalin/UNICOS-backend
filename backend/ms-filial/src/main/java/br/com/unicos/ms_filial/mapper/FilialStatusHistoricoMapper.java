package br.com.unicos.ms_filial.mapper;

import br.com.unicos.ms_filial.dto.status.FilialStatusHistoricoCreateRequest;
import br.com.unicos.ms_filial.dto.status.FilialStatusHistoricoResponse;
import br.com.unicos.ms_filial.dto.status.FilialStatusHistoricoUpdateRequest;
import br.com.unicos.ms_filial.model.FilialStatusHistorico;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade FilialStatusHistorico e seus DTOs.
 */
@Component
@RequiredArgsConstructor
public class FilialStatusHistoricoMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade FilialStatusHistorico para DTO de resposta.
     */
    public FilialStatusHistoricoResponse toResponse(FilialStatusHistorico entity) {
        return new FilialStatusHistoricoResponse(
                entity.getId(),
                entity.getFilialId(),
                entity.getStatusAnterior(),
                entity.getStatusNovo(),
                entity.getDataAlteracao(),
                entity.getMotivo(),
                entity.getUsuarioId()
        );
    }

    /**
     * Converte DTO de criação para entidade FilialStatusHistorico.
     *
     * <p>
     * Campos como {@code id} e auditoria devem ser definidos no service.
     * Normalmente {@code dataAlteracao} deve ser preenchida no service.
     * </p>
     */
    public FilialStatusHistorico toEntity(FilialStatusHistoricoCreateRequest request) {
        return mapper.map(request, FilialStatusHistorico.class);
    }

    /**
     * Atualiza uma entidade FilialStatusHistorico existente com dados do DTO de atualização.
     *
     * <p>
     * Em geral, histórico não deve ser atualizado (apenas inserido).
     * Este método existe para manter simetria com o padrão.
     * </p>
     */
    public void updateEntity(FilialStatusHistoricoUpdateRequest request, FilialStatusHistorico entity) {
        entity.setFilialId(request.filialId());
        entity.setStatusAnterior(request.statusAnterior());
        entity.setStatusNovo(request.statusNovo());
        entity.setDataAlteracao(request.dataAlteracao());
        entity.setMotivo(request.motivo());
        entity.setUsuarioId(request.usuarioId());
    }
}

package br.com.unicos.ms_filial.mapper;

import br.com.unicos.ms_filial.dto.horario.HorarioFuncionamentoFilialCreateRequest;
import br.com.unicos.ms_filial.dto.horario.HorarioFuncionamentoFilialResponse;
import br.com.unicos.ms_filial.dto.horario.HorarioFuncionamentoFilialUpdateRequest;
import br.com.unicos.ms_filial.model.HorarioFuncionamentoFilial;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade HorarioFuncionamentoFilial e seus DTOs.
 */
@Component
@RequiredArgsConstructor
public class HorarioFuncionamentoFilialMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade HorarioFuncionamentoFilial para DTO de resposta.
     */
    public HorarioFuncionamentoFilialResponse toResponse(HorarioFuncionamentoFilial entity) {
        return new HorarioFuncionamentoFilialResponse(
                entity.getId(),
                entity.getFilialId(),
                entity.getDiaSemana(),
                entity.getHoraAbertura(),
                entity.getHoraFechamento(),
                entity.getAberto()
        );
    }

    /**
     * Converte DTO de criação para entidade HorarioFuncionamentoFilial.
     *
     * <p>
     * Campos como {@code id} e auditoria devem ser definidos no service.
     * </p>
     */
    public HorarioFuncionamentoFilial toEntity(HorarioFuncionamentoFilialCreateRequest request) {
        return mapper.map(request, HorarioFuncionamentoFilial.class);
    }

    /**
     * Atualiza uma entidade HorarioFuncionamentoFilial existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e auditoria não devem ser alterados.
     * </p>
     */
    public void updateEntity(HorarioFuncionamentoFilialUpdateRequest request, HorarioFuncionamentoFilial entity) {
        entity.setFilialId(request.filialId());
        entity.setDiaSemana(request.diaSemana());
        entity.setHoraAbertura(request.horaAbertura());
        entity.setHoraFechamento(request.horaFechamento());
        entity.setAberto(request.aberto());
    }
}

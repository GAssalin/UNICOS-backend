package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioUpdateRequest;
import br.com.unicos.ms_empresa.model.EmpresaUsuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper responsável pela conversão entre a entidade {@link EmpresaUsuario}
 * e seus respectivos DTOs.
 *
 * <p>
 * Todas as conversões são realizadas de forma explícita,
 * sem uso de frameworks automáticos de mapeamento,
 * garantindo clareza, previsibilidade e facilidade de manutenção.
 * </p>
 */
@Component
public class EmpresaUsuarioMapper {

    /**
     * Converte o DTO de criação para uma nova entidade {@link EmpresaUsuario}.
     *
     * @param request DTO de criação
     * @return nova entidade EmpresaUsuario
     */
    public EmpresaUsuario toEntity(EmpresaUsuarioCreateRequest request) {
        if (request == null) {
            return null;
        }

        EmpresaUsuario entity = new EmpresaUsuario();
        entity.setUsuarioId(request.usuarioId());
        entity.setPerfil(request.perfil());

        return entity;
    }

    /**
     * Atualiza os campos mutáveis de uma entidade {@link EmpresaUsuario}
     * com base no DTO de atualização.
     *
     * <p>
     * Campos imutáveis ou controlados pelo domínio, como {@code id}
     * e {@code usuarioId}, não são alterados neste método.
     * </p>
     *
     * @param request DTO de atualização
     * @param entity entidade a ser atualizada
     */
    public void updateEntityFromDTO(EmpresaUsuarioUpdateRequest request, EmpresaUsuario entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setPerfil(request.perfil());
    }

    /**
     * Converte a entidade {@link EmpresaUsuario} para o DTO de resposta detalhada.
     *
     * @param entity entidade de vínculo usuário-empresa
     * @return DTO detalhado do vínculo
     */
    public EmpresaUsuarioResponse toResponseDTO(EmpresaUsuario entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaUsuarioResponse(
                entity.getId(),
                entity.getUsuarioId(),
                entity.getPerfil(),
                mapLocalDateTime(entity.getCriadoEm()),
                mapLocalDateTime(entity.getAtualizadoEm())
        );
    }

    /**
     * Converte a entidade {@link EmpresaUsuario} para o DTO resumido.
     *
     * @param entity entidade de vínculo usuário-empresa
     * @return DTO resumido do vínculo
     */
    public EmpresaUsuarioResumoResponse toResumoDTO(EmpresaUsuario entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaUsuarioResumoResponse(
                entity.getUsuarioId(),
                entity.getPerfil()
        );
    }

    /**
     * Conversão explícita de {@link LocalDateTime}.
     *
     * @param source data/hora de origem
     * @return data/hora convertida
     */
    private LocalDateTime mapLocalDateTime(LocalDateTime source) {
        return source == null
                ? null
                : LocalDateTime.of(
                source.getYear(),
                source.getMonthValue(),
                source.getDayOfMonth(),
                source.getHour(),
                source.getMinute(),
                source.getSecond(),
                source.getNano()
        );
    }

    /**
     * Método de compatibilidade com código legado.
     *
     * @param request DTO de atualização
     * @param entity entidade a ser atualizada
     */
    public void updateEntity(EmpresaUsuarioUpdateRequest request, EmpresaUsuario entity) {
        updateEntityFromDTO(request, entity);
    }

    /**
     * Método de compatibilidade com código legado.
     *
     * @param entity entidade de vínculo usuário-empresa
     * @return DTO detalhado do vínculo
     */
    public EmpresaUsuarioResponse toResponse(EmpresaUsuario entity) {
        return toResponseDTO(entity);
    }

    /**
     * Método de compatibilidade com código legado.
     *
     * @param entity entidade de vínculo usuário-empresa
     * @return DTO resumido do vínculo
     */
    public EmpresaUsuarioResumoResponse toResumoResponse(EmpresaUsuario entity) {
        return toResumoDTO(entity);
    }
}
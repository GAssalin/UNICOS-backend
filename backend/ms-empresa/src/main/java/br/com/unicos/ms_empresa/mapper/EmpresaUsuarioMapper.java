package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
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
     * <p>
     * Campos controlados pelo domínio, como {@code id}, auditoria e demais
     * metadados, não são definidos neste mapper e devem ser atribuídos
     * na camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return nova entidade EmpresaUsuario
     */
    public EmpresaUsuario toEntity(EmpresaUsuarioCreateRequest request) {
        if (request == null) {
            return null;
        }

        EmpresaUsuario entity = new EmpresaUsuario();
        entity.setEmpresaId(request.empresaId());
        entity.setEmpresa(mapEmpresaReferencia(request.empresaRefId()));
        entity.setUsuarioId(request.usuarioId());
        entity.setPerfil(request.perfil());

        return entity;
    }

    /**
     * Atualiza os campos mutáveis de uma entidade {@link EmpresaUsuario}
     * com base no DTO de atualização.
     *
     * <p>
     * Campos imutáveis ou controlados pelo domínio, como {@code id},
     * {@code empresaId}, {@code empresa} e {@code usuarioId},
     * não são alterados neste método.
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
                entity.getEmpresaId(),
                extractEmpresaRefId(entity),
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
     * Cria uma referência simplificada de {@link Empresa}
     * contendo apenas o identificador.
     *
     * @param empresaRefId identificador da empresa de referência
     * @return entidade Empresa com ID preenchido
     */
    private Empresa mapEmpresaReferencia(Long empresaRefId) {
        if (empresaRefId == null) {
            return null;
        }

        Empresa empresa = new Empresa();
        empresa.setId(empresaRefId);
        return empresa;
    }

    /**
     * Extrai de forma segura o identificador da empresa de referência.
     *
     * @param entity entidade de vínculo usuário-empresa
     * @return ID da empresa associada
     */
    private Long extractEmpresaRefId(EmpresaUsuario entity) {
        if (entity.getEmpresa() == null) {
            return null;
        }

        return entity.getEmpresa().getId();
    }

    /**
     * Conversão explícita de {@link LocalDateTime}.
     *
     * <p>
     * Mantido como método dedicado para deixar a transformação de data/hora
     * visível no mapper e facilitar futura evolução.
     * </p>
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
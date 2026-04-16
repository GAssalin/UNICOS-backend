package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_contato.EmpresaContatoUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaContato;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper responsável pela conversão entre a entidade {@link EmpresaContato}
 * e seus respectivos DTOs.
 *
 * <p>
 * Todas as conversões são realizadas de forma explícita,
 * sem uso de frameworks automáticos de mapeamento,
 * garantindo clareza, previsibilidade e facilidade de manutenção.
 * </p>
 */
@Component
public class EmpresaContatoMapper {

    /**
     * Converte o DTO de criação para uma nova entidade {@link EmpresaContato}.
     *
     * <p>
     * Campos controlados pelo domínio, como {@code id}, auditoria e demais
     * metadados, não são definidos neste mapper e devem ser atribuídos
     * na camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return nova entidade EmpresaContato
     */
    public EmpresaContato toEntity(EmpresaContatoCreateRequest request) {
        if (request == null) {
            return null;
        }

        EmpresaContato entity = new EmpresaContato();
        entity.setEmpresaId(request.empresaId());
        entity.setEmpresa(mapEmpresaReferencia(request.empresaRefId()));
        entity.setTipoContato(request.tipoContato());
        entity.setValor(request.valor());
        entity.setPrincipal(request.principal());

        return entity;
    }

    /**
     * Atualiza os campos mutáveis de uma entidade {@link EmpresaContato}
     * com base no DTO de atualização.
     *
     * <p>
     * Campos imutáveis ou controlados pelo domínio, como {@code id},
     * {@code empresaId}, {@code empresa} e {@code tipoContato},
     * não são alterados neste método.
     * </p>
     *
     * @param request DTO de atualização
     * @param entity entidade a ser atualizada
     */
    public void updateEntityFromDTO(EmpresaContatoUpdateRequest request, EmpresaContato entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setValor(request.valor());
        entity.setPrincipal(request.principal());
    }

    /**
     * Converte a entidade {@link EmpresaContato} para o DTO de resposta detalhada.
     *
     * @param entity entidade de contato
     * @return DTO detalhado do contato
     */
    public EmpresaContatoResponse toResponseDTO(EmpresaContato entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaContatoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                extractEmpresaRefId(entity),
                entity.getTipoContato(),
                entity.getValor(),
                entity.isPrincipal(),
                mapLocalDateTime(entity.getCriadoEm()),
                mapLocalDateTime(entity.getAtualizadoEm())
        );
    }

    /**
     * Converte a entidade {@link EmpresaContato} para o DTO resumido.
     *
     * @param entity entidade de contato
     * @return DTO resumido do contato
     */
    public EmpresaContatoResumoResponse toResumoDTO(EmpresaContato entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaContatoResumoResponse(
                entity.getId(),
                entity.getTipoContato(),
                entity.getValor(),
                entity.isPrincipal()
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
     * @param entity entidade de contato
     * @return ID da empresa associada
     */
    private Long extractEmpresaRefId(EmpresaContato entity) {
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
    public void updateEntity(EmpresaContatoUpdateRequest request, EmpresaContato entity) {
        updateEntityFromDTO(request, entity);
    }

    /**
     * Método de compatibilidade com código legado.
     *
     * @param entity entidade de contato
     * @return DTO detalhado do contato
     */
    public EmpresaContatoResponse toResponse(EmpresaContato entity) {
        return toResponseDTO(entity);
    }

    /**
     * Método de compatibilidade com código legado.
     *
     * @param entity entidade de contato
     * @return DTO resumido do contato
     */
    public EmpresaContatoResumoResponse toResumoResponse(EmpresaContato entity) {
        return toResumoDTO(entity);
    }
}
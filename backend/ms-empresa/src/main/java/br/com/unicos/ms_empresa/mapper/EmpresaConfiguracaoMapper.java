package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_configuracao.EmpresaConfiguracaoUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaConfiguracao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper responsável pela conversão entre a entidade {@link EmpresaConfiguracao}
 * e seus respectivos DTOs.
 *
 * <p>
 * Todas as conversões são realizadas de forma explícita,
 * sem uso de frameworks automáticos de mapeamento,
 * garantindo clareza, previsibilidade e facilidade de manutenção.
 * </p>
 */
@Component
public class EmpresaConfiguracaoMapper {

    /**
     * Converte o DTO de criação para uma nova entidade {@link EmpresaConfiguracao}.
     *
     * <p>
     * Campos controlados pelo domínio, como {@code id}, auditoria e demais
     * metadados, não são definidos neste mapper e devem ser atribuídos
     * na camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return nova entidade EmpresaConfiguracao
     */
    public EmpresaConfiguracao toEntity(EmpresaConfiguracaoCreateRequest request) {
        if (request == null) {
            return null;
        }

        EmpresaConfiguracao entity = new EmpresaConfiguracao();
        entity.setEmpresaId(request.empresaId());
        entity.setEmpresa(mapEmpresaReferencia(request.empresaRefId()));
        entity.setChave(request.chave());
        entity.setValor(request.valor());

        return entity;
    }

    /**
     * Atualiza os campos mutáveis de uma entidade {@link EmpresaConfiguracao}
     * com base no DTO de atualização.
     *
     * <p>
     * Campos imutáveis ou controlados pelo domínio, como {@code id},
     * {@code empresaId} e {@code empresa}, não são alterados neste método.
     * </p>
     *
     * @param request DTO de atualização
     * @param entity entidade a ser atualizada
     */
    public void updateEntityFromDTO(EmpresaConfiguracaoUpdateRequest request, EmpresaConfiguracao entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setChave(request.chave());
        entity.setValor(request.valor());
    }

    /**
     * Converte a entidade {@link EmpresaConfiguracao} para o DTO de resposta detalhada.
     *
     * @param entity entidade de configuração
     * @return DTO detalhado da configuração
     */
    public EmpresaConfiguracaoResponse toResponseDTO(EmpresaConfiguracao entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaConfiguracaoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                extractEmpresaRefId(entity),
                entity.getChave(),
                entity.getValor(),
                mapLocalDateTime(entity.getCriadoEm()),
                mapLocalDateTime(entity.getAtualizadoEm())
        );
    }

    /**
     * Converte a entidade {@link EmpresaConfiguracao} para o DTO resumido.
     *
     * @param entity entidade de configuração
     * @return DTO resumido da configuração
     */
    public EmpresaConfiguracaoResumoResponse toResumoDTO(EmpresaConfiguracao entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaConfiguracaoResumoResponse(
                entity.getId(),
                entity.getChave(),
                entity.getValor()
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
     * @param entity entidade de configuração
     * @return ID da empresa associada
     */
    private Long extractEmpresaRefId(EmpresaConfiguracao entity) {
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
    public void updateEntity(EmpresaConfiguracaoUpdateRequest request, EmpresaConfiguracao entity) {
        updateEntityFromDTO(request, entity);
    }

    /**
     * Método de compatibilidade com código legado.
     *
     * @param entity entidade de configuração
     * @return DTO detalhado da configuração
     */
    public EmpresaConfiguracaoResponse toResponse(EmpresaConfiguracao entity) {
        return toResponseDTO(entity);
    }

    /**
     * Método de compatibilidade com código legado.
     *
     * @param entity entidade de configuração
     * @return DTO resumido da configuração
     */
    public EmpresaConfiguracaoResumoResponse toResumoResponse(EmpresaConfiguracao entity) {
        return toResumoDTO(entity);
    }
}
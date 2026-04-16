package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoResumoResponse;
import br.com.unicos.ms_empresa.dto.empresa_endereco.EmpresaEnderecoUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
import br.com.unicos.ms_empresa.model.EmpresaEndereco;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper responsável pela conversão entre a entidade {@link EmpresaEndereco}
 * e seus respectivos DTOs.
 *
 * <p>
 * Todas as conversões são realizadas de forma explícita,
 * sem uso de frameworks automáticos de mapeamento,
 * garantindo clareza, previsibilidade e facilidade de manutenção.
 * </p>
 */
@Component
public class EmpresaEnderecoMapper {

    /**
     * Converte o DTO de criação para uma nova entidade {@link EmpresaEndereco}.
     *
     * <p>
     * Campos controlados pelo domínio, como {@code id}, auditoria e demais
     * metadados, não são definidos neste mapper e devem ser atribuídos
     * na camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return nova entidade EmpresaEndereco
     */
    public EmpresaEndereco toEntity(EmpresaEnderecoCreateRequest request) {
        if (request == null) {
            return null;
        }

        EmpresaEndereco entity = new EmpresaEndereco();
        entity.setEmpresaId(request.empresaId());
        entity.setEmpresa(mapEmpresaReferencia(request.empresaRefId()));
        entity.setTipoEndereco(request.tipoEndereco());
        entity.setLogradouro(request.logradouro());
        entity.setNumero(request.numero());
        entity.setComplemento(request.complemento());
        entity.setBairro(request.bairro());
        entity.setMunicipio(request.municipio());
        entity.setUf(request.uf());
        entity.setCep(request.cep());
        entity.setPrincipal(request.principal());

        return entity;
    }

    /**
     * Atualiza os campos mutáveis de uma entidade {@link EmpresaEndereco}
     * com base no DTO de atualização.
     *
     * <p>
     * Campos imutáveis ou controlados pelo domínio, como {@code id},
     * {@code empresaId} e a referência da empresa, não são alterados neste método.
     * </p>
     *
     * @param request DTO de atualização
     * @param entity entidade a ser atualizada
     */
    public void updateEntityFromDTO(EmpresaEnderecoUpdateRequest request, EmpresaEndereco entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setLogradouro(request.logradouro());
        entity.setNumero(request.numero());
        entity.setComplemento(request.complemento());
        entity.setBairro(request.bairro());
        entity.setMunicipio(request.municipio());
        entity.setUf(request.uf());
        entity.setCep(request.cep());
        entity.setPrincipal(request.principal());
    }

    /**
     * Converte a entidade {@link EmpresaEndereco} para o DTO de resposta detalhada.
     *
     * @param entity entidade de endereço
     * @return DTO detalhado do endereço
     */
    public EmpresaEnderecoResponse toResponseDTO(EmpresaEndereco entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaEnderecoResponse(
                entity.getId(),
                entity.getEmpresaId(),
                extractEmpresaRefId(entity),
                entity.getTipoEndereco(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getMunicipio(),
                entity.getUf(),
                entity.getCep(),
                entity.isPrincipal(),
                mapLocalDateTime(entity.getCriadoEm()),
                mapLocalDateTime(entity.getAtualizadoEm())
        );
    }

    /**
     * Converte a entidade {@link EmpresaEndereco} para o DTO resumido.
     *
     * @param entity entidade de endereço
     * @return DTO resumido do endereço
     */
    public EmpresaEnderecoResumoResponse toResumoDTO(EmpresaEndereco entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaEnderecoResumoResponse(
                entity.getId(),
                entity.getTipoEndereco(),
                entity.getMunicipio(),
                entity.getUf(),
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
     * @param entity entidade de endereço
     * @return ID da empresa associada
     */
    private Long extractEmpresaRefId(EmpresaEndereco entity) {
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
}
package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa.EmpresaCreateRequestDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaResponseDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaResumoDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaUpdateRequestDTO;
import br.com.unicos.ms_empresa.model.Empresa;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Mapper responsável pela conversão entre a entidade {@link Empresa}
 * e seus respectivos DTOs.
 *
 * <p>
 * Todas as conversões são realizadas de forma explícita,
 * sem uso de frameworks automáticos de mapeamento,
 * para garantir clareza, previsibilidade e facilidade de manutenção.
 * </p>
 */
@Component
public class EmpresaMapper {

    /**
     * Converte a entidade {@link Empresa} para o DTO de resposta detalhada.
     *
     * @param entity entidade de empresa
     * @return DTO detalhado da empresa
     */
    public EmpresaResponseDTO toResponseDTO(Empresa entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaResponseDTO(
                entity.getId(),
                entity.getMatrizId(),
                entity.getRazaoSocial(),
                entity.getNomeFantasia(),
                entity.getCnpj(),
                entity.getTipoEmpresa(),
                entity.getStatusEmpresa(),
                entity.getRegimeTributario(),
                mapLocalDate(entity.getDataAbertura()),
                entity.getPessoaJuridicaId()
        );
    }

    /**
     * Converte a entidade {@link Empresa} para o DTO resumido.
     *
     * @param entity entidade de empresa
     * @return DTO resumido da empresa
     */
    public EmpresaResumoDTO toResumoDTO(Empresa entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaResumoDTO(
                entity.getId(),
                entity.getMatrizId(),
                entity.getRazaoSocial(),
                entity.getCnpj(),
                entity.getStatusEmpresa()
        );
    }

    /**
     * Converte o DTO de criação para uma nova entidade {@link Empresa}.
     *
     * <p>
     * Campos controlados pelo domínio, como {@code id}, {@code empresaId},
     * auditoria e demais metadados, não são definidos neste mapper
     * e devem ser atribuídos na camada de service.
     * </p>
     *
     * @param request DTO de criação
     * @return nova entidade Empresa
     */
    public Empresa toEntity(EmpresaCreateRequestDTO request) {
        if (request == null) {
            return null;
        }

        Empresa entity = new Empresa();
        entity.setRazaoSocial(request.razaoSocial());
        entity.setNomeFantasia(request.nomeFantasia());
        entity.setCnpj(request.cnpj());
        entity.setTipoEmpresa(request.tipoEmpresa());
        entity.setStatusEmpresa(request.statusEmpresa());
        entity.setRegimeTributario(request.regimeTributario());
        entity.setDataAbertura(mapLocalDate(request.dataAbertura()));
        entity.setPessoaJuridicaId(request.pessoaJuridicaId());

        return entity;
    }

    /**
     * Atualiza os campos mutáveis de uma entidade {@link Empresa}
     * com base no DTO de atualização.
     *
     * <p>
     * Campos imutáveis ou controlados pelo domínio, como {@code id},
     * {@code empresaId} e {@code cnpj}, não são alterados neste método.
     * </p>
     *
     * @param request DTO de atualização
     * @param entity entidade a ser atualizada
     */
    public void updateEntityFromDTO(EmpresaUpdateRequestDTO request, Empresa entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setRazaoSocial(request.razaoSocial());
        entity.setNomeFantasia(request.nomeFantasia());
        entity.setTipoEmpresa(request.tipoEmpresa());
        entity.setStatusEmpresa(request.statusEmpresa());
        entity.setRegimeTributario(request.regimeTributario());
        entity.setDataAbertura(mapLocalDate(request.dataAbertura()));
        entity.setPessoaJuridicaId(request.pessoaJuridicaId());
    }

    /**
     * Conversão explícita de {@link LocalDate}.
     *
     * <p>
     * Mantido como método dedicado para deixar a transformação de data
     * visível no mapper e facilitar futura evolução, como formatação,
     * timezone ou adaptação para outros tipos.
     * </p>
     *
     * @param source data de origem
     * @return data convertida
     */
    private LocalDate mapLocalDate(LocalDate source) {
        return source == null ? null : LocalDate.of(
                source.getYear(),
                source.getMonthValue(),
                source.getDayOfMonth()
        );
    }
}
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

    public EmpresaResponseDTO toResponseDTO(Empresa entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaResponseDTO(
                entity.getId(),
                null, // REMOVIDO conceito de empresaId
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

    public EmpresaResumoDTO toResumoDTO(Empresa entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaResumoDTO(
                entity.getId(),
                null, // REMOVIDO conceito de empresaId
                entity.getRazaoSocial(),
                entity.getCnpj(),
                entity.getStatusEmpresa()
        );
    }

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

    private LocalDate mapLocalDate(LocalDate source) {
        return source == null ? null : LocalDate.of(
                source.getYear(),
                source.getMonthValue(),
                source.getDayOfMonth()
        );
    }
}
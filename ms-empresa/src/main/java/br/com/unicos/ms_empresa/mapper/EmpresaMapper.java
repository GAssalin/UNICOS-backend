package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa.EmpresaCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaResponse;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaListDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaUpdateRequest;
import br.com.unicos.ms_empresa.model.Empresa;
import org.springframework.stereotype.Component;


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

    public EmpresaResponse toResponse(Empresa entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaResponse(
                entity.getId(),
                null, // REMOVIDO conceito de empresaId
                entity.getRazaoSocial(),
                entity.getNomeFantasia(),
                entity.getCnpj(),
                entity.getTipoEmpresa(),
                entity.getStatusEmpresa(),
                entity.getRegimeTributario(),
                entity.getDataAbertura(),
                entity.getPessoaJuridicaId()
        );
    }

    public EmpresaListDTO toListDTO(Empresa entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaListDTO(
                entity.getId(),
                null, // REMOVIDO conceito de empresaId
                entity.getRazaoSocial(),
                entity.getCnpj(),
                entity.getStatusEmpresa()
        );
    }

    public Empresa toEntity(EmpresaCreateRequest request) {
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
        entity.setDataAbertura(request.dataAbertura());
        entity.setPessoaJuridicaId(request.pessoaJuridicaId());

        return entity;
    }

    public void updateEntity(Empresa entity, EmpresaUpdateRequest request) {
        if (request == null || entity == null) {
            return;
        }

        entity.setRazaoSocial(request.razaoSocial());
        entity.setNomeFantasia(request.nomeFantasia());
        entity.setTipoEmpresa(request.tipoEmpresa());
        entity.setStatusEmpresa(request.statusEmpresa());
        entity.setRegimeTributario(request.regimeTributario());
        entity.setDataAbertura(request.dataAbertura());
        entity.setPessoaJuridicaId(request.pessoaJuridicaId());
    }

}
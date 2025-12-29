package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa.EmpresaCreateRequestDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaResponseDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaResumoDTO;
import br.com.unicos.ms_empresa.dto.empresa.EmpresaUpdateRequestDTO;
import br.com.unicos.ms_empresa.model.Empresa;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade Empresa e seus DTOs.
 *
 * <p>
 * Segue o padrão de mapeamento manual adotado no UniCoS,
 * utilizando ModelMapper apenas para conversões diretas
 * e protegendo campos sensíveis no domínio.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class EmpresaMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade Empresa para DTO de resposta detalhada.
     */
    public EmpresaResponseDTO toResponse(Empresa entity) {
        return new EmpresaResponseDTO(
                entity.getId(),
                entity.getEmpresaId(),
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

    /**
     * Converte a entidade Empresa para DTO resumido.
     */
    public EmpresaResumoDTO toResumo(Empresa entity) {
        return new EmpresaResumoDTO(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getRazaoSocial(),
                entity.getCnpj(),
                entity.getStatusEmpresa()
        );
    }

    /**
     * Converte DTO de criação para entidade Empresa.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId}, auditoria e
     * status de ativação devem ser definidos no service.
     * </p>
     */
    public Empresa toEntity(EmpresaCreateRequestDTO request) {
        return mapper.map(request, Empresa.class);
    }

    /**
     * Atualiza uma entidade Empresa existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id}, {@code empresaId} e {@code cnpj}
     * não devem ser alterados.
     * </p>
     */
    public void updateEntity(EmpresaUpdateRequestDTO request, Empresa entity) {
        entity.setRazaoSocial(request.razaoSocial());
        entity.setNomeFantasia(request.nomeFantasia());
        entity.setTipoEmpresa(request.tipoEmpresa());
        entity.setStatusEmpresa(request.statusEmpresa());
        entity.setRegimeTributario(request.regimeTributario());
        entity.setDataAbertura(request.dataAbertura());
        entity.setPessoaJuridicaId(request.pessoaJuridicaId());
    }
}

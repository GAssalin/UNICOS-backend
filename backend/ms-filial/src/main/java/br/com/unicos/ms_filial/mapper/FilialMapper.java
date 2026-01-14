package br.com.unicos.ms_filial.mapper;

import br.com.unicos.ms_filial.dto.filial.FilialCreateRequest;
import br.com.unicos.ms_filial.dto.filial.FilialResponse;
import br.com.unicos.ms_filial.dto.filial.FilialUpdateRequest;
import br.com.unicos.ms_filial.model.Filial;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade Filial e seus DTOs.
 *
 * <p>
 * Segue o padrão de mapeamento manual adotado no UniCoS,
 * utilizando ModelMapper apenas para conversões diretas
 * e protegendo campos sensíveis no domínio.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class FilialMapper {

    private final ModelMapper mapper;

    /**
     * Converte a entidade Filial para DTO de resposta.
     */
    public FilialResponse toResponse(Filial entity) {
        return new FilialResponse(
                entity.getId(),
                entity.getCodigo(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getStatusFilial(),
                entity.getEmpresaId(),
                entity.getEnderecoFilialId(),
                entity.getContatoFilialId()
        );
    }

    /**
     * Converte DTO de criação para entidade Filial.
     *
     * <p>
     * Campos como {@code id}, {@code empresaId} (tenant), auditoria e
     * controles internos devem ser definidos no service.
     * </p>
     */
    public Filial toEntity(FilialCreateRequest request) {
        return mapper.map(request, Filial.class);
    }

    /**
     * Atualiza uma entidade Filial existente com dados do DTO de atualização.
     *
     * <p>
     * Campos imutáveis como {@code id} e campos de auditoria não devem ser alterados.
     * Ajuste regras adicionais (ex.: {@code cnpj} imutável) conforme sua política.
     * </p>
     */
    public void updateEntity(FilialUpdateRequest request, Filial entity) {
        entity.setCodigo(request.codigo());
        entity.setNome(request.nome());
        entity.setCnpj(request.cnpj());
        entity.setStatusFilial(request.statusFilial());
        entity.setEmpresaId(request.empresaId());
        entity.setEnderecoFilialId(request.enderecoFilialId());
        entity.setContatoFilialId(request.contatoFilialId());
    }
}

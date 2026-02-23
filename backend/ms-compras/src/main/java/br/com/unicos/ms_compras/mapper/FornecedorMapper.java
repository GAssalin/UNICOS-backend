package br.com.unicos.ms_compras.mapper;

import br.com.unicos.ms_compras.dto.FornecedorDto;
import br.com.unicos.ms_compras.model.Fornecedor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper da entidade Fornecedor.
 */
@Component
@RequiredArgsConstructor
public class FornecedorMapper {

    private final ModelMapper mapper;

    public FornecedorDto toResponse(Fornecedor entity) {
        return new FornecedorDto(
                entity.getId(),
                entity.getCodigo(),
                entity.getRazaoSocial(),
                entity.getNomeFantasia(),
                entity.getCnpj(),
                entity.getInscricaoEstadual(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getObservacao()
        );
    }

    public Fornecedor toEntity(FornecedorDto dto) {
        // Ok para create. Para update, prefira updateEntity.
        return mapper.map(dto, Fornecedor.class);
    }

    public void updateEntity(FornecedorDto dto, Fornecedor entity) {
        entity.setCodigo(dto.codigo());
        entity.setRazaoSocial(dto.razaoSocial());
        entity.setNomeFantasia(dto.nomeFantasia());
        entity.setCnpj(dto.cnpj());
        entity.setInscricaoEstadual(dto.inscricaoEstadual());
        entity.setEmail(dto.email());
        entity.setTelefone(dto.telefone());
        entity.setObservacao(dto.observacao());
        // Não mexe em contatos/enderecos aqui sem regra explícita.
    }
}
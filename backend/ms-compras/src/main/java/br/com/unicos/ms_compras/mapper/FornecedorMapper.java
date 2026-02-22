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
                entity.getRazaoSocial(),
                entity.getNomeFantasia(),
                entity.getCnpj(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getAtivo()
        );
    }

    public Fornecedor toEntity(FornecedorDto dto) {
        return mapper.map(dto, Fornecedor.class);
    }

    public void updateEntity(FornecedorDto dto, Fornecedor entity) {
        entity.setRazaoSocial(dto.razaoSocial());
        entity.setNomeFantasia(dto.nomeFantasia());
        entity.setCnpj(dto.cnpj());
        entity.setEmail(dto.email());
        entity.setTelefone(dto.telefone());
        entity.setAtivo(dto.ativo());
    }
}
package br.com.unicos.ms_compras.dto;

/**
 * DTO de endereço de fornecedor.
 */
public record EnderecoFornecedorDto(
        Long id,
        Long fornecedorId,
        String tipo,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String observacao
) { }

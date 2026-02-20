package br.com.unicos.ms_compras.dto;

/**
 * DTO de contato de fornecedor.
 */
public record ContatoFornecedorDto(
        Long id,
        Long fornecedorId,
        String nome,
        String cargo,
        String telefone,
        String celular,
        String email,
        Boolean principal,
        String observacao
) {
}

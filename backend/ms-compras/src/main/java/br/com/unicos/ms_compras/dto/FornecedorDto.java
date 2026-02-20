package br.com.unicos.ms_compras.dto;

/**
 * DTO de fornecedor para transporte de dados cadastrais.
 */
public record FornecedorDto(
        Long id,
        String codigo,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String inscricaoEstadual,
        String email,
        String telefone,
        String observacao
) {
}

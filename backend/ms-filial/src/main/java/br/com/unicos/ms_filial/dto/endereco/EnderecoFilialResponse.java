package br.com.unicos.ms_filial.dto.endereco;

/**
 * DTO de resposta de EnderecoFilial.
 */
public record EnderecoFilialResponse(
        Long id,
        Long filialId,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String cep
) { }

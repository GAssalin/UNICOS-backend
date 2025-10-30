package br.com.erp.ms_empresa.dto;

import br.com.erp.ms_empresa.model.TipoEnderecoEmpresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para criação e atualização dos endereços vinculados a uma empresa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoEmpresaRequest {

    @NotNull(message = "O ID da empresa é obrigatório.")
    private Long empresaId;

    @NotBlank(message = "O logradouro é obrigatório.")
    @Size(max = 150, message = "O logradouro deve ter no máximo 150 caracteres.")
    private String logradouro;

    @NotBlank(message = "O número é obrigatório.")
    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres.")
    private String numero;

    @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres.")
    private String complemento;

    @NotBlank(message = "O bairro é obrigatório.")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres.")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
    private String cidade;

    @NotBlank(message = "A UF é obrigatória.")
    @Size(max = 2, message = "A UF deve ter no máximo 2 caracteres.")
    private String uf;

    @NotBlank(message = "O CEP é obrigatório.")
    @Size(max = 10, message = "O CEP deve ter no máximo 10 caracteres.")
    private String cep;

    @NotNull(message = "O tipo de endereço é obrigatório.")
    private TipoEnderecoEmpresa tipo;
}
package br.com.erp.ms_empresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para criação e atualização dos dados de uma empresa matriz.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {

    @NotBlank(message = "A razão social é obrigatória.")
    @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres.")
    private String razaoSocial;

    @NotBlank(message = "O nome fantasia é obrigatório.")
    @Size(max = 150, message = "O nome fantasia deve ter no máximo 150 caracteres.")
    private String nomeFantasia;

    @NotBlank(message = "O CNPJ é obrigatório.")
    @Size(max = 18, message = "O CNPJ deve ter no máximo 18 caracteres.")
    private String cnpj;

    @Size(max = 20, message = "A inscrição estadual deve ter no máximo 20 caracteres.")
    private String inscricaoEstadual;

    @Size(max = 20, message = "A inscrição municipal deve ter no máximo 20 caracteres.")
    private String inscricaoMunicipal;
}
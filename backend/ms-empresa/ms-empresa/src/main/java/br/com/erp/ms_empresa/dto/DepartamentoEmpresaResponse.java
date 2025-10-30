package br.com.erp.ms_empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para retorno detalhado das informações de um departamento empresarial.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoEmpresaResponse {

    private Long id;
    private Long empresaId;
    private String empresaRazaoSocial;
    private String nome;
    private String descricao;
    private Boolean ativo;
}
package br.com.erp.ms_empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para retorno detalhado das informações de um contato empresarial.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContatoEmpresaResponse {

    private Long id;
    private Long empresaId;
    private String empresaRazaoSocial;
    private String nomeContato;
    private String cargo;
    private String telefone;
    private String celular;
    private String email;
}
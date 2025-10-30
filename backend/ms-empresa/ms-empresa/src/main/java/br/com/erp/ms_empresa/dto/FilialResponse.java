package br.com.erp.ms_empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para retorno detalhado das informações de uma filial.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilialResponse {

    private Long id;
    private Long empresaId;
    private String empresaMatrizNome;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String responsavel;
    private String telefone;
    private String email;
    private String endereco;
    private String cidade;
    private String uf;
    private String cep;
}
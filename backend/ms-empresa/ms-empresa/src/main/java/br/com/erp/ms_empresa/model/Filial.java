package br.com.erp.ms_empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "empresa_matriz_nome", length = 150)
    private String empresaMatrizNome;

    @NotBlank(message = "A razão social da filial é obrigatória.")
    @Column(nullable = false, length = 150)
    private String razaoSocial;

    @NotBlank(message = "O nome fantasia da filial é obrigatório.")
    @Column(nullable = false, length = 150)
    private String nomeFantasia;

    @NotBlank(message = "O CNPJ da filial é obrigatório.")
    @Column(nullable = false, length = 18, unique = true)
    private String cnpj;

    @Column(length = 20)
    private String inscricaoEstadual;

    @Column(length = 20)
    private String inscricaoMunicipal;

    @Column(length = 150)
    private String responsavel;

    @Column(length = 50)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String endereco;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String uf;

    @Column(length = 10)
    private String cep;
}
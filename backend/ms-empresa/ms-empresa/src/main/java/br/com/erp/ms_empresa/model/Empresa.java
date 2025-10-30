package br.com.erp.ms_empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String razaoSocial;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nomeFantasia;

    @NotBlank
    @Column(nullable = false, length = 18, unique = true)
    private String cnpj;

    @Column(length = 20)
    private String inscricaoEstadual;

    @Column(length = 20)
    private String inscricaoMunicipal;

    @NotNull
    @Column(nullable = false)
    private Boolean matriz = true;

    @ManyToOne
    @JoinColumn(name = "empresa_matriz_id")
    private Empresa empresaMatriz;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnderecoEmpresa> enderecos;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContatoEmpresa> contatos;
}

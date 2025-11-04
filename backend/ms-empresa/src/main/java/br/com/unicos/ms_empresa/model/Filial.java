package br.com.unicos.ms_empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Representa uma filial, centro de distribuição ou unidade produtiva
 * vinculada a uma empresa matriz.
 */
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

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nome;

    @NotBlank
    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @OneToOne(mappedBy = "filial", cascade = CascadeType.ALL)
    private EnderecoEmpresa endereco;

    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}

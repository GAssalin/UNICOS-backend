package br.com.unicos.ms_empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contato_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContatoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nomeContato;

    @Column(length = 50)
    private String cargo;

    @Column(length = 20)
    private String telefone;

    @Column(length = 20)
    private String celular;

    @Column(length = 100)
    private String email;
}

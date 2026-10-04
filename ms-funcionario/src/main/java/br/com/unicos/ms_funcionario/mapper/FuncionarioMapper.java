package br.com.unicos.ms_funcionario.mapper;

import br.com.unicos.ms_funcionario.dto.funcionario.FuncionarioRequest;
import br.com.unicos.ms_funcionario.dto.funcionario.FuncionarioResponse;
import br.com.unicos.ms_funcionario.model.Funcionario;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidade {@link Funcionario} e seus DTOs.
 *
 * <p>Cargo e superior são resolvidos e validados pelo service.</p>
 */
@Component
public class FuncionarioMapper {

    public FuncionarioResponse toResponse(Funcionario entity) {
        if (entity == null)
            return null;

        return new FuncionarioResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getPessoaId(),
                entity.getUsuarioId(),
                entity.getMatricula(),
                entity.getCargo().getId(),
                entity.getCargo().getNome(),
                entity.getCargo().getPapel(),
                entity.getEscopoCarteira(),
                entity.getSuperiorId(),
                entity.getFilialId(),
                entity.getDataAdmissao(),
                entity.getDataDesligamento(),
                entity.getStatus(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public Funcionario toEntity(FuncionarioRequest request) {
        if (request == null)
            return null;

        return Funcionario.builder()
                .pessoaId(request.pessoaId())
                .usuarioId(request.usuarioId())
                .matricula(normalizarMatricula(request.matricula()))
                .filialId(request.filialId())
                .dataAdmissao(request.dataAdmissao())
                .dataDesligamento(request.dataDesligamento())
                .status(request.status())
                .build();
    }

    public void updateEntity(Funcionario entity, FuncionarioRequest request) {
        if (request == null || entity == null)
            return;

        entity.setPessoaId(request.pessoaId());
        entity.setUsuarioId(request.usuarioId());
        entity.setMatricula(normalizarMatricula(request.matricula()));
        entity.setFilialId(request.filialId());
        entity.setDataAdmissao(request.dataAdmissao());
        entity.setDataDesligamento(request.dataDesligamento());
        entity.setStatus(request.status());
    }

    /**
     * Matrícula em branco equivale a não informada: evita conflito na restrição de unicidade.
     */
    public static String normalizarMatricula(String matricula) {
        return matricula == null || matricula.isBlank() ? null : matricula.trim();
    }
}

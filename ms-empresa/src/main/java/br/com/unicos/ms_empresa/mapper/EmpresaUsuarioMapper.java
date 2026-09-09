package br.com.unicos.ms_empresa.mapper;

import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioCreateRequest;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioResponse;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioListDTO;
import br.com.unicos.ms_empresa.dto.empresa_usuario.EmpresaUsuarioUpdateRequest;
import br.com.unicos.ms_empresa.model.EmpresaUsuario;
import org.springframework.stereotype.Component;


/**
 * Mapper responsável pela conversão entre a entidade {@link EmpresaUsuario}
 * e seus respectivos DTOs.
 *
 * <p>
 * Todas as conversões são realizadas de forma explícita,
 * sem uso de frameworks automáticos de mapeamento,
 * garantindo clareza, previsibilidade e facilidade de manutenção.
 * </p>
 */
@Component
public class EmpresaUsuarioMapper {

    /**
     * Converte o DTO de criação para uma nova entidade {@link EmpresaUsuario}.
     *
     * @param request DTO de criação
     * @return nova entidade EmpresaUsuario
     */
    public EmpresaUsuario toEntity(EmpresaUsuarioCreateRequest request) {
        if (request == null) {
            return null;
        }

        EmpresaUsuario entity = new EmpresaUsuario();
        entity.setUsuarioId(request.usuarioId());
        entity.setPerfil(request.perfil());

        return entity;
    }

    /**
     * Atualiza os campos mutáveis de uma entidade {@link EmpresaUsuario}
     * com base no DTO de atualização.
     *
     * <p>
     * Campos imutáveis ou controlados pelo domínio, como {@code id}
     * e {@code usuarioId}, não são alterados neste método.
     * </p>
     *
     * @param request DTO de atualização
     * @param entity entidade a ser atualizada
     */
    public void updateEntity(EmpresaUsuario entity, EmpresaUsuarioUpdateRequest request) {
        if (request == null || entity == null) {
            return;
        }

        entity.setPerfil(request.perfil());
    }

    /**
     * Converte a entidade {@link EmpresaUsuario} para o DTO de resposta detalhada.
     *
     * @param entity entidade de vínculo usuário-empresa
     * @return DTO detalhado do vínculo
     */
    public EmpresaUsuarioResponse toResponse(EmpresaUsuario entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaUsuarioResponse(
                entity.getId(),
                entity.getUsuarioId(),
                entity.getPerfil(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    /**
     * Converte a entidade {@link EmpresaUsuario} para o DTO resumido.
     *
     * @param entity entidade de vínculo usuário-empresa
     * @return DTO resumido do vínculo
     */
    public EmpresaUsuarioListDTO toListDTO(EmpresaUsuario entity) {
        if (entity == null) {
            return null;
        }

        return new EmpresaUsuarioListDTO(
                entity.getUsuarioId(),
                entity.getPerfil()
        );
    }




}
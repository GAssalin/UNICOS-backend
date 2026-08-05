package br.com.unicos.ms_usuario.mapper;

import br.com.unicos.ms_usuario.dto.verificacao.ConfirmarEmailVerificacaoResponse;
import br.com.unicos.ms_usuario.dto.verificacao.UsuarioEmailVerificacaoListDTO;
import br.com.unicos.ms_usuario.dto.verificacao.UsuarioEmailVerificacaoResponse;
import br.com.unicos.ms_usuario.model.Usuario;
import br.com.unicos.ms_usuario.model.UsuarioEmailVerificacao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class UsuarioEmailVerificacaoMapper {

    /**
     * Converte a entidade {@link UsuarioEmailVerificacao} para o DTO detalhado de resposta.
     *
     * @param entity entidade de verificação de e-mail
     * @return DTO detalhado
     */
    public UsuarioEmailVerificacaoResponse toResponse(UsuarioEmailVerificacao entity) {
        if (entity == null)
            return null;

        return new UsuarioEmailVerificacaoResponse(
                entity.getId(),
                extractUsuarioId(entity),
                entity.getExpiracao(),
                entity.isUtilizado(),
                entity.getCriadoEm()
        );
    }

    /**
     * Converte a entidade {@link UsuarioEmailVerificacao} para o DTO de listagem.
     *
     * @param entity entidade de verificação de e-mail
     * @return DTO de listagem
     */
    public UsuarioEmailVerificacaoListDTO toListDTO(UsuarioEmailVerificacao entity) {
        if (entity == null)
            return null;

        return new UsuarioEmailVerificacaoListDTO(
                entity.getId(),
                extractUsuarioId(entity),
                entity.isUtilizado(),
                entity.getExpiracao()
        );
    }

    /**
     * Converte uma lista de entidades {@link UsuarioEmailVerificacao}
     * para uma lista de DTOs detalhados.
     *
     * @param entities lista de entidades
     * @return lista de DTOs detalhados
     */
    public List<UsuarioEmailVerificacaoResponse> toResponseList(List<UsuarioEmailVerificacao> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte uma lista de entidades {@link UsuarioEmailVerificacao}
     * para uma lista de DTOs de listagem.
     *
     * @param entities lista de entidades
     * @return lista de DTOs de listagem
     */
    public List<UsuarioEmailVerificacaoListDTO> toListDTOList(List<UsuarioEmailVerificacao> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();

        return entities.stream()
                .map(this::toListDTO)
                .toList();
    }

    /**
     * Cria uma nova entidade de verificação de e-mail.
     *
     * <p>
     * O token já deve ser recebido em formato hash pela camada de serviço.
     * </p>
     *
     * @param usuario usuário vinculado ao token
     * @param tokenHash hash do token de verificação
     * @param expiracao data/hora de expiração do token
     * @return nova entidade de verificação
     */
    public UsuarioEmailVerificacao toEntity(Usuario usuario, String tokenHash, LocalDateTime expiracao) {
        if (usuario == null || tokenHash == null || expiracao == null)
            return null;

        return UsuarioEmailVerificacao.builder()
                .usuario(usuario)
                .tokenHash(tokenHash)
                .expiracao(expiracao)
                .utilizado(false)
                .build();
    }

    /**
     * Monta o DTO de resposta da confirmação de e-mail.
     *
     * @param usuario usuário confirmado
     * @param mensagem mensagem de retorno da operação
     * @return DTO com resultado da confirmação
     */
    public ConfirmarEmailVerificacaoResponse toConfirmacaoResponse(Usuario usuario, String mensagem) {
        if (usuario == null)
            return null;

        return new ConfirmarEmailVerificacaoResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.isEmailVerificado(),
                mensagem
        );
    }

    /**
     * Extrai o ID do usuário associado à entidade.
     *
     * @param entity entidade de verificação
     * @return ID do usuário ou null
     */
    private Long extractUsuarioId(UsuarioEmailVerificacao entity) {
        return entity.getUsuario() != null ? entity.getUsuario().getId() : null;
    }
}
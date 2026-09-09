# ms-produto — alinhamento com ms-permissao

## Alterações

- Versão 1.0.0-SNAPSHOT. Java 21, Spring Boot 3.4.3 e Spring Cloud 2024.0.0 mantidos.
- Lombok usa ${lombok.version} no annotation processor, conforme a referência.
- Removidos Config Client/importação obrigatória do Config Server e a dependência ms-pessoas, sem uso no código enviado. Adicionados Actuator e AOP. A lista de dependências diretas corresponde à referência.
- application.properties adaptado do ms-permissao para ms-produto/PRODUTO_*. Incluído application-prod.properties, com as mesmas configurações da referência.
- Health liberado no SecurityConfig. Demais configurações de segurança preservadas.
- V1 movida para db/migration/common; V2–V10 para db/migration/local, sem alterar bytes, nomes ou versões dos scripts. Como na referência, os dois diretórios continuam habilitados também no perfil prod, incluindo os seeds.
- Seis DTOs *ResumoResponse renomeados para *ListDTO. Mappers usam toListDTO e updateEntity(entity, request), mantendo o parâmetro empresaId quando necessário.
- Requests de criação e atualização mantidos separados, com campos e validações originais. Regras de catálogo, preços, categorias, marcas, atributos, imagens, códigos de barras e unidades preservadas.
- PermissaoClient agora é final, permitindo injeção por construtor. Fallback da consulta de permissões corrigido para boolean; indisponibilidade continua lançando 503.
- Rotas /v1/produtos e seus recursos mantidos. Filtro exige correspondência exata do recurso ou seguida por /, evitando colisões de prefixos. Nomes de permissões coincidem com V10 do ms-permissao.
- Incluídos testes JUnit com 50 casos parametrizados de HTTP/recurso, bloqueio por permissão, limpeza dos contextos, injeção do cliente e assinatura/erro do fallback.

## Configuração e execução

Use JDK 21. Configure as variáveis no ambiente ou na IDE; o Spring não carrega .env automaticamente:

- PRODUTO_DB_HOST, PRODUTO_DB_PORT, PRODUTO_DB_NAME, PRODUTO_DB_USER, PRODUTO_DB_PASSWORD.
- UNICOS_JWT_SECRET, UNICOS_JWT_ISSUER, UNICOS_TEMPO_EXP_TOKEN, UNICOS_INTERNAL_TOKEN.
- RABBITMQ_HOST, RABBITMQ_PORT, RABBITMQ_USER, RABBITMQ_PASSWORD.
- Opcionais: PRODUTO_SERVER_PORT (padrão 0), EUREKA_URL e OUTBOX_INTERVALO_MS (padrão 2000).
- Produção: SPRING_PROFILES_ACTIVE=prod.

Disponibilize core-tenant, core-usuario e core-auth 1.0.0-SNAPSHOT e suas dependências transitivas no Maven local ou configure o servidor Maven github para acesso ao GitHub Packages.

Windows: `mvnw.cmd clean test` e `mvnw.cmd spring-boot:run`.
Linux/macOS: `sh mvnw clean test` e `sh mvnw spring-boot:run`.

Dockerfile preservado: requer unicos-core e ms-produto lado a lado no contexto de build e declara EXPOSE 8087. Para usar essa porta, configure PRODUTO_SERVER_PORT=8087. O padrão 0 escolhe uma porta aleatória, como na referência.

## Validação e limites

Verificações estáticas concluídas: XML e dependências do POM, imports internos, preservação das rotas, campos/validações dos DTOs, transformações nos mappers, integridade byte a byte das dez migrations e correspondência das 40 permissões com os seeds da referência. Há 97 arquivos Java, incluindo os testes.

`bash mvnw -B test` foi tentado, mas a resolução do parent Spring Boot falhou por indisponibilidade de DNS/rede para os repositórios. O ambiente disponibiliza runtime Java 17, sem JDK 21. A compilação completa, os testes JUnit e a integração com banco e serviços não foram executados com sucesso. As verificações estáticas não substituem essas etapas.

Os nomes de schemas no OpenAPI mudam com os DTOs de listagem; clientes gerados que dependam desses nomes precisam ser regenerados. Os payloads JSON foram preservados.

A confiança nos cabeçalhos de usuário/tenant e as propriedades JWT/token seguem o projeto recebido; não foi implementada nova validação de token no filtro. Este alinhamento não constitui revisão completa das regras de negócio ou do isolamento entre tenants.

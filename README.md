# GLP Client Portal

Backend do portal da **GLP Consultoria Energética**, desenvolvido como API REST em Java e Spring Boot. A aplicação centraliza clientes, contratos, consumo mensal, economia gerada, usuários e indicadores de dashboard, com autenticação JWT e autorização por perfil.

## Funcionalidades da V1

- gestão de clientes;
- contratos vinculados a clientes;
- registro e consulta de consumo mensal;
- cálculo e histórico de economia;
- usuários com perfis `ADMIN` e `CLIENTE`;
- autenticação stateless com Spring Security + JWT;
- isolamento de dados do cliente autenticado;
- dashboard com visão global para ADMIN e visão restrita para CLIENTE;
- relatório PDF;
- documentação Swagger/OpenAPI;
- tratamento global e padronizado de erros;
- Docker/Docker Compose;
- testes automatizados com JUnit 5 e Mockito;
- CI com GitHub Actions.

Gateway de pagamento **não faz parte do escopo da V1**.

## Stack

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- JJWT
- PostgreSQL
- Bean Validation
- springdoc OpenAPI
- iText
- Lombok
- Maven
- JUnit 5 / Mockito
- Docker
- GitHub Actions

## Arquitetura

O projeto utiliza um **monólito modular organizado por domínio**:

```text
com.glp.client_portal
├── cliente
├── contrato
├── consumo
├── economia
├── dashboard
├── usuario
│   └── auth
├── converter
└── exception
```

As principais decisões incluem UUID para identificadores expostos pela API, `BigDecimal` para valores monetários/consumo e `YearMonth` para referências mensais.

## Segurança

O login retorna um JWT que deve ser enviado como `Bearer Token`.

- `ADMIN`: operações administrativas e visão global.
- `CLIENTE`: leitura limitada ao cliente vinculado ao usuário.
- operações de criação/alteração/exclusão sob `/clientes/**` são administrativas;
- o backend valida tanto o `clienteId` quanto o vínculo real do `contratoId`, evitando acesso cruzado entre clientes.

Swagger permanece público para documentação; as demais rotas exigem autenticação, exceto o login.

## Principais endpoints

| Método | Rota | Finalidade |
| --- | --- | --- |
| POST | `/login` | Autenticação |
| POST | `/usuarios` | Cadastro de usuário (ADMIN) |
| PATCH | `/usuarios/alterar_senha` | Alteração da própria senha |
| POST | `/clientes` | Cadastro de cliente (ADMIN) |
| GET | `/clientes` | Lista conforme perfil |
| GET | `/clientes/{id}` | Consulta de cliente |
| PUT | `/clientes/{id}` | Atualização (ADMIN) |
| DELETE | `/clientes/{id}` | Exclusão (ADMIN) |
| POST | `/clientes/{clienteId}/contratos` | Criação de contrato (ADMIN) |
| GET | `/clientes/{clienteId}/contratos` | Contratos do cliente |
| GET | `/clientes/{clienteId}/contratos/{contratoId}` | Detalhe do contrato |
| POST | `/clientes/{clienteId}/contratos/{contratoId}/consumos` | Registro de consumo (ADMIN) |
| GET | `/clientes/{clienteId}/contratos/{contratoId}/consumos` | Histórico de consumo |
| POST | `/clientes/{clienteId}/contratos/{contratoId}/economias` | Registro/cálculo de economia (ADMIN) |
| GET | `/clientes/{clienteId}/contratos/{contratoId}/economias` | Histórico de economia |
| GET | `/clientes/{clienteId}/total-economizado` | Total economizado |
| GET | `/dashboard` | Indicadores conforme perfil |

A documentação interativa fica disponível em `/swagger-ui/index.html`.

## Executando localmente

### Pré-requisitos

- Java 17+
- Maven
- PostgreSQL

Crie o banco:

```sql
CREATE DATABASE glp_client_portal;
```

Copie o arquivo de configuração de exemplo e defina as variáveis de ambiente:

```text
DB_USERNAME=postgres
DB_PASSWORD=sua_senha
JWT_SECRET=uma_chave_secreta_forte
```

Depois execute:

```bash
mvn spring-boot:run
```

Por padrão, a API estará em `http://localhost:8080`.

### Docker Compose

Com `DB_PASSWORD` e `JWT_SECRET` definidos no ambiente:

```bash
docker compose up --build
```

O container da aplicação expõe a API em `http://localhost:8081`.

## Testes

Execute:

```bash
mvn test
```

A suíte cobre os serviços principais, autenticação, regras de usuário, dashboard e regras de ownership. Pull Requests para `develop` ou `main` executam testes e build pelo GitHub Actions.

## Git Flow

Fluxo obrigatório do projeto:

```text
feature/* ou fix/* → develop → main
```

- `develop` recebe o trabalho em andamento por Pull Request.
- `main` representa a versão estável.
- a integração `develop → main` é feita manualmente pelo mantenedor após validação.

## Próxima fase

Após o fechamento do backend V1, a próxima etapa é revisar e integrar o frontend web ao contrato atual da API.

## Autor

Desenvolvido por **Robson Morcineck** para GLP Consultoria Energética.

# GLP Client Portal
 
Sistema backend desenvolvido em Java com Spring Boot para a **GLP Consultoria Energética**, permitindo que clientes acompanhem informações sobre seus contratos de energia, consumo mensal e economia gerada.
 
Projeto desenvolvido para a empresa **GLP Consultoria**.
 
---
 
## 📋 Sobre o projeto
 
A GLP Consultoria Energética atua na gestão energética corporativa, ajudando empresas a reduzir custos e otimizar seu consumo de energia. O **GLP Client Portal** nasceu da necessidade de oferecer aos clientes da GLP um canal digital para acompanhar, de forma transparente, os contratos firmados, o consumo registrado mês a mês e a economia real gerada ao longo do tempo.
 
O sistema foi construído como uma **API REST**, desacoplada de qualquer interface visual, preparada para futuramente alimentar um frontend web ou mobile.
 
---
 
## 🚀 Funcionalidades
 
O projeto está organizado em 4 módulos de domínio:
 
### Cliente
- Cadastrar, listar, buscar, atualizar e remover clientes
- Suporte a pessoa física (CPF) e jurídica (CNPJ)
### Contrato
- Criar contratos vinculados a um cliente
- Listar contratos de um cliente
- Consultar detalhes de um contrato específico
### Consumo Mensal
- Registrar o consumo de energia (kWh) e custo de um determinado mês
- Consultar o histórico de consumo de um contrato
### Economia
- Calcular a economia gerada em um mês, comparando o custo antes e depois do contrato
- Consultar o total economizado por um cliente, somando todos os contratos
---
 
## 🛠️ Tecnologias
 
- **Java 17**
- **Spring Boot 4.1.0**
- **Spring Data JPA** - persistência e acesso a dados
- **Spring Validation (Bean Validation)** - validação de dados de entrada
- **PostgreSQL** - banco de dados relacional
- **Lombok** - redução de código repetitivo (getters, setters, etc.)
- **Maven** - gerenciamento de dependências e build
- **GitHub Actions** - pipeline de CI (build e testes automatizados em Pull Requests)
### Ferramentas recomendadas para desenvolvimento
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Postman](https://www.postman.com/) - para testar os endpoints da API
- [pgAdmin](https://www.pgadmin.org/) ou [DBeaver](https://dbeaver.io/) - para administração do banco de dados
---
 
## 🏗️ Arquitetura
 
O projeto segue uma arquitetura de **monólito modular**, organizada por domínio de negócio (e não por camada técnica). Cada módulo contém sua própria Entity, Repository, Service e Controller:
 
```
com.glp.client_portal
├── cliente
├── contrato
├── consumo
├── economia
├── converter      (conversores customizados para persistência)
└── exception      (tratamento global de erros)
```
 
Essa abordagem favorece a manutenibilidade do projeto à medida que ele cresce, mantendo tudo relacionado a um mesmo domínio (ex: Cliente) agrupado em um único lugar sem a complexidade operacional de uma arquitetura de microsserviços, que não se justifica para o escopo atual do projeto.
 
### Decisões técnicas relevantes
 
- **UUID como identificador**: como a API é exposta para clientes externos, IDs sequenciais foram descartados em favor de UUID, evitando enumeration attacks (varredura sequencial de recursos).
- **BigDecimal para valores monetários e de consumo**: evita os erros de arredondamento característicos de `double`/`float`.
- **YearMonth para mês de referência**: usado em `ConsumoMensal` e `Economia` para representar mês/ano de forma semanticamente correta, com um `AttributeConverter` customizado garantindo persistência legível no banco (texto, não binário).
- **Tratamento de erros centralizado**: um `@RestControllerAdvice` único trata exceções de domínio (recurso não encontrado), erros de validação de campos e erros inesperados, retornando sempre uma resposta JSON padronizada.
---
 
## ⚙️ Como rodar o projeto localmente
 
### Pré-requisitos
- Java 17 ou superior
- Maven
- PostgreSQL em execução
### 1. Clone o repositório
 
```bash
git clone https://github.com/Morcineck/glp-client-portal.git
cd glp-client-portal
```
 
### 2. Crie o banco de dados
 
```sql
CREATE DATABASE glp_client_portal;
```
 
### 3. Configure o arquivo de propriedades
 
Copie o arquivo de exemplo:
 
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```
 
### 4. Configure as variáveis de ambiente
 
O projeto lê a senha do banco de dados a partir de uma variável de ambiente, nunca diretamente do código-fonte. Configure:
 
```
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_aqui
```
 
> No IntelliJ: **Run → Edit Configurations → Environment variables**
 
### 5. Execute o projeto
 
```bash
mvn spring-boot:run
```
 
A aplicação estará disponível em `http://localhost:8080`.
 
---
 
## 📡 Endpoints principais
 
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/clientes` | Cadastra um novo cliente |
| `GET` | `/clientes` | Lista todos os clientes |
| `GET` | `/clientes/{id}` | Busca um cliente por ID |
| `PUT` | `/clientes/{id}` | Atualiza um cliente |
| `DELETE` | `/clientes/{id}` | Remove um cliente |
| `POST` | `/clientes/{clienteId}/contratos` | Cria um contrato para um cliente |
| `GET` | `/clientes/{clienteId}/contratos` | Lista contratos de um cliente |
| `GET` | `/clientes/{clienteId}/contratos/{contratoId}` | Detalha um contrato |
| `POST` | `/clientes/{clienteId}/contratos/{contratoId}/consumos` | Registra consumo mensal |
| `GET` | `/clientes/{clienteId}/contratos/{contratoId}/consumos` | Lista histórico de consumo |
| `POST` | `/clientes/{clienteId}/contratos/{contratoId}/economias` | Calcula economia de um mês |
| `GET` | `/clientes/{clienteId}/contratos/{contratoId}/economias` | Lista economias de um contrato |
| `GET` | `/clientes/{clienteId}/total-economizado` | Retorna o total economizado pelo cliente |
 
### Exemplo de requisição — Cadastrar cliente
 
```http
POST /clientes
Content-Type: application/json
 
{
    "nome": "Empresa Exemplo LTDA",
    "email": "contato@empresaexemplo.com",
    "telefone": "11999999999",
    "documento": "12345678000199",
    "tipoDocumento": "CNPJ"
}
```
 
### Exemplo de resposta de erro (validação)
 
```json
{
    "timestamp": "2026-06-27T20:11:21.05",
    "status": 400,
    "erro": "Dados de requisição inválidos",
    "mensagem": "Houve erros de validação nos campos preenchidos",
    "path": "/clientes",
    "erros": [
        {
            "campo": "email",
            "mensagem": "O email é obrigatório"
        }
    ]
}
```
 
---
 
## 🌳 Fluxo de branches
 
O projeto segue um fluxo simplificado de Git Flow:
 
- **`main`** - código estável, validado, pronto para apresentação
- **`develop`** - desenvolvimento ativo
- **`feature/*`** ou **`refatoracao/*`** - branches temporárias para novas funcionalidades ou refatorações, mescladas em `develop` via Pull Request
Todo Pull Request direcionado à `main` dispara automaticamente um workflow de build e testes via GitHub Actions.
 
---
 
## 🗺️ Roadmap futuro
 
- [ ] Autenticação e autorização (Spring Security + JWT)
- [ ] Frontend web para consumo da API
- [ ] Documentação interativa da API (Swagger/OpenAPI)
- [ ] Testes automatizados (JUnit/Mockito)
- [ ] Integração com gateway de pagamento
---
 
## 👤 Autor
 
Desenvolvido por **Robson Morcineck** para GLP Consultoria Energética.

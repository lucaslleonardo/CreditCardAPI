# Credit Card API

API REST desenvolvida em Java com Spring Boot para simular o gerenciamento de um sistema de cartões de crédito.

O projeto contempla o gerenciamento de clientes, contas, cartões, compras, faturas e pagamentos, incluindo autenticação, autorização por perfil e implementação de regras de negócio relacionadas ao limite de crédito e pagamento de faturas.

O projeto foi desenvolvido com foco em prática de desenvolvimento back end, arquitetura de APIs REST, persistência de dados, segurança, tratamento de exceções, testes e containerização.

## Tecnologias utilizadas

* Java 21
* Spring Boot 4.1.0
* Spring Data JPA
* Spring Security
* JWT
* MapStruct
* MySQL 8
* Maven
* Docker
* Docker Compose
* Swagger / OpenAPI
* Lombok
* Bean Validation
* JUnit
* Mockito
* SLF4J

## Funcionalidades

### Usuários

* Cadastro de usuários
* Autenticação utilizando email e senha
* Geração de token JWT
* Controle de acesso baseado em roles
* Perfis `ADMIN` e `CLIENTE`

### Clientes

* Cadastro de clientes
* Consulta de clientes
* Atualização de dados
* Exclusão de clientes
* Validação dos dados recebidos

### Contas

* Cadastro de contas vinculadas a clientes
* Consulta de contas
* Atualização de contas
* Exclusão de contas
* Controle de status da conta

### Cartões

* Cadastro de cartões vinculados a contas
* Consulta de cartões
* Exclusão de cartões
* Controle de status do cartão
* Controle de limite total e limite disponível

### Compras

* Cadastro de compras
* Associação da compra ao cartão e à fatura
* Validação do status do cartão
* Validação do status da fatura
* Verificação de limite disponível
* Aprovação ou recusa da compra
* Atualização automática do limite disponível
* Atualização do valor da fatura
* Cancelamento de compras

### Faturas

* Criação e consulta de faturas
* Associação das faturas aos cartões
* Controle de vencimento
* Controle de status da fatura
* Atualização do valor da fatura conforme novas compras
* Controle de juros por atraso

### Pagamentos

* Pagamento de faturas
* Pagamento parcial
* Pagamento integral
* Atualização do limite disponível após o pagamento
* Aplicação de juros em faturas vencidas
* Alteração automática do status da fatura após quitação
* Consulta do histórico de pagamentos

## Regras de negócio

O projeto possui regras de negócio implementadas na camada de serviço para representar o funcionamento básico de uma operação de cartão de crédito.

Entre elas:

* Compras somente podem ser realizadas com cartões ativos
* Compras são recusadas quando o limite disponível é insuficiente
* Compras aprovadas reduzem o limite disponível do cartão
* Compras aprovadas aumentam o valor da fatura
* O cancelamento de uma compra devolve o limite utilizado ao cartão
* Pagamentos aumentam novamente o limite disponível
* Uma fatura pode receber pagamentos parciais
* Uma fatura é marcada como paga quando seu saldo chega a zero
* Faturas vencidas recebem aplicação de juros
* O juros é aplicado apenas uma vez por fatura

## Segurança

A API utiliza Spring Security com autenticação baseada em JWT.

O fluxo de autenticação funciona da seguinte forma:

```text
Cliente
   ↓
POST /auth/login
   ↓
Autenticação
   ↓
JWT
   ↓
Authorization: Bearer <token>
   ↓
Endpoint protegido
```

Os acessos são controlados de acordo com o perfil do usuário.

### `ROLE_CLIENTE`

Possui acesso às operações relacionadas aos seus recursos permitidos.

### `ROLE_ADMIN`

Possui acesso às operações administrativas do sistema.

Endpoints públicos incluem autenticação e cadastro de usuário.

## Arquitetura

O projeto segue uma organização baseada na separação de responsabilidades entre as principais camadas da aplicação:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

### Controller

Responsável pela exposição dos endpoints REST, recebimento das requisições e retorno das respostas HTTP.

### Service

Responsável pelas regras de negócio e pelo fluxo das operações.

### Repository

Responsável pela comunicação com o banco de dados utilizando Spring Data JPA.

### Entity

Representa as entidades persistidas no banco de dados e seus relacionamentos.

### DTO

Utilizado para definir os dados de entrada e saída da API, evitando a exposição direta das entidades.

### Mapper

O MapStruct é utilizado para realizar a conversão entre DTOs e entidades.

## Principais relacionamentos

O domínio da aplicação possui relacionamentos entre clientes, contas, cartões, compras, faturas e pagamentos.

Uma representação simplificada é:

```text
Cliente
   │
   └── Conta
          │
          └── Cartão
                 │
                 ├── Fatura
                 │      │
                 │      ├── Compra
                 │      └── Pagamento
                 │
                 └── Compra
```

## Validação e tratamento de erros

A API utiliza Bean Validation para validação dos dados recebidos nas requisições.

Também são utilizadas exceções específicas para representar diferentes situações de negócio, como:

* Cliente não encontrado
* Conta não encontrada
* Cartão não encontrado
* Cartão não ativo
* Fatura não encontrada
* Fatura não aberta
* Conta já cadastrada
* Cartão já cadastrado
* Pagamento superior ao valor da fatura

As exceções são tratadas de forma centralizada para manter respostas HTTP padronizadas.

## Documentação da API

A documentação dos endpoints é disponibilizada utilizando Swagger e OpenAPI.

Após iniciar a aplicação, acesse:

`http://localhost:8080/swagger-ui/index.html`

A documentação permite visualizar os endpoints disponíveis, parâmetros, requisições e respostas da API.

## Banco de dados

O projeto utiliza MySQL como banco de dados e Spring Data JPA para persistência.

O relacionamento entre a aplicação e o banco é realizado através do Hibernate.

O projeto também possui configuração Docker Compose para executar o MySQL em container.

## Como executar o projeto

### Pré-requisitos

Para executar o projeto localmente, é necessário ter instalado:

* Java 21
* Maven
* Docker
* Docker Compose

### 1. Clone o repositório

```bash
git clone https://github.com/lucaslleonardo/CreditCardAPI.git
```

Entre no diretório:

```bash
cd CreditCardAPI
```

### 2. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
DATABASE_USERNAME=root
DATABASE_PASSWORD=sua_senha

JWT_SECRET=sua_chave_secreta
JWT_EXPIRATION=86400000
```

Não versionar informações sensíveis no repositório.

### 3. Execute os containers

```bash
docker compose up --build
```

O Docker Compose inicia o banco MySQL e a aplicação.

### 4. Acesse a API

`http://localhost:8080`

### 5. Acesse o Swagger

`http://localhost:8080/swagger-ui/index.html`

## Autenticação

Para acessar os endpoints protegidos, primeiro realize o login:

```http
POST /auth/login
Content-Type: application/json
```

Exemplo:

```json
{
  "email": "usuario@email.com",
  "password": "123456"
}
```

A API retorna um JWT.

No Postman ou outra ferramenta de testes, envie o token no header:

```http
Authorization: Bearer SEU_TOKEN
```

## Exemplo de fluxo

Um fluxo básico do sistema pode ser representado da seguinte forma:

```text
Cadastro do usuário
        ↓
Cadastro do cliente
        ↓
Criação da conta
        ↓
Criação do cartão
        ↓
Criação da fatura
        ↓
Realização da compra
        ↓
Validação do cartão
        ↓
Validação do limite
        ↓
Compra aprovada
        ↓
Limite disponível reduzido
        ↓
Valor adicionado à fatura
        ↓
Pagamento da fatura
        ↓
Limite disponível restaurado
```

## Testes

O projeto utiliza JUnit e Mockito para testes automatizados da camada de serviço.

Os testes utilizam mocks para isolar as dependências dos serviços, permitindo validar diferentes cenários de negócio, incluindo:

* Operações realizadas com sucesso
* Entidades não encontradas
* Entidades já cadastradas
* Exceções de negócio
* Validação das chamadas aos repositories
* Alteração de dados durante as operações

## Logs

A aplicação utiliza SLF4J para registro de informações importantes durante a execução.

Os logs auxiliam no acompanhamento de operações como:

* Cadastro de entidades
* Consultas
* Aprovação e recusa de compras
* Pagamentos
* Aplicação de juros
* Erros durante operações

## Objetivos do projeto

Este projeto foi desenvolvido com o objetivo de aprofundar conhecimentos em desenvolvimento back end utilizando Java e Spring Boot, praticando conceitos presentes no desenvolvimento de APIs REST.

Entre os principais objetivos estão:

* Desenvolvimento de APIs REST
* Implementação de regras de negócio
* Persistência utilizando JPA
* Modelagem de relacionamentos entre entidades
* Autenticação e autorização com JWT
* Validação de dados
* Tratamento global de exceções
* Testes unitários
* Logging
* Documentação de APIs
* Containerização com Docker
* Utilização de banco de dados relacional

## Melhorias futuras

Algumas melhorias que podem ser implementadas futuramente:

* Implementação de CI/CD
* Deploy em ambiente cloud
* Testes de integração
* Testes de integração utilizando Testcontainers
* Monitoramento da aplicação
* Implementação de métricas
* Melhorias na cobertura de testes
* Paginação nas consultas
* Melhorias no controle de permissões
* Implementação de refresh token

## Autor

**Lucas Leonardo**

Desenvolvedor Back End | Java | Spring Boot

GitHub: https://github.com/lucaslleonardo

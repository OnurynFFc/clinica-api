# Clínica API - 3ºVersão

API REST para gerenciamento de clínica médica, desenvolvida com **Spring Boot 3** e **Java 21**.

## 📋 Sobre o Projeto

Sistema completo para gestão de clínicas médicas com funcionalidades de cadastro de médicos e pacientes, agendamento de consultas com validação de regras de negócio, e documentação interativa via Swagger.

## 🚀 Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.5 |
| Spring Data JPA | 3.2.5 |
| Sql Server | 12 |
| Lombok | latest |
| Springdoc OpenAPI (Swagger) | 2.5.0 |
| Maven | 3.6 |

## 📦 Funcionalidades

### Médicos
-  Cadastro com CRM e especialidade
-  Listagem paginada com filtro por especialidade
-  Atualização de dados
-  Exclusão lógica (soft delete)

### Pacientes
-  Cadastro com validação de CPF
-  Listagem paginada
-  Atualização de dados
-  Exclusão lógica (soft delete)

### Consultas
-  Agendamento com validação de conflitos de horário
-  Regras de negócio: horário de funcionamento (07h–18h), sem domingos, 30min de antecedência
-  Cancelamento com mínimo 24h de antecedência
-  Listagem por médico e por paciente

##  Arquitetura

```
src/
└── main/
    └── java/com/clinica/api/
        ├── config/          # Configurações (Swagger)
        ├── controller/      # Controllers REST
        ├── dto/             # Data Transfer Objects (Records)
        ├── exception/       # Tratamento global de erros
        ├── model/           # Entidades JPA
        ├── repository/      # Repositórios Spring Data
        └── service/         # Regras de negócio
```

## ▶ Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.x
- SQL Server

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/clinica-api.git
cd clinica-api
```

### 2. Configure o banco de dados
Edite `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clinica_db?createDatabaseIfNotExist=true
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 3. Execute a aplicação
```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## Documentação da API

Com a aplicação rodando, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

## Endpoints Principais

### Médicos — `/api/v1/medicos`
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/` | Cadastrar médico |
| GET | `/` | Listar médicos ativos |
| GET | `/{id}` | Buscar por ID |
| GET | `/especialidade/{esp}` | Filtrar por especialidade |
| PUT | `/{id}` | Atualizar médico |
| DELETE | `/{id}` | Inativar médico |

### Pacientes — `/api/v1/pacientes`
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/` | Cadastrar paciente |
| GET | `/` | Listar pacientes ativos |
| GET | `/{id}` | Buscar por ID |
| PUT | `/{id}` | Atualizar paciente |
| DELETE | `/{id}` | Inativar paciente |

### Consultas — `/api/v1/consultas`
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/` | Agendar consulta |
| GET | `/` | Listar consultas |
| GET | `/{id}` | Buscar por ID |
| GET | `/medico/{medicoId}` | Consultas por médico |
| GET | `/paciente/{pacienteId}` | Consultas por paciente |
| PATCH | `/{id}/cancelar` | Cancelar consulta |

## Regras de Negócio

- Clínica funciona de **segunda a sábado**, das **07h às 18h**
- Agendamentos precisam de **mínimo 30 minutos** de antecedência
- Cancelamentos precisam de **mínimo 24 horas** de antecedência
- Médico e paciente **não podem ter dois agendamentos** no mesmo horário
- Exclusão de médicos e pacientes é **lógica** (campo `ativo = false`)

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

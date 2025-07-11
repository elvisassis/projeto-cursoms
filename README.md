
# 📚 Projeto Curso MS - Microservices com Spring Boot, Eureka, RabbitMQ e API Gateway

## ✔️ Visão Geral

Este projeto é uma arquitetura de microserviços construída com **Spring Boot 3**, **Spring Cloud 2025**, **RabbitMQ**, **Eureka Server** e **API Gateway**. Ele simula um ecossistema financeiro simples, contendo serviços de cadastro de clientes, gestão de cartões e avaliação de crédito.

## ⚙️ Tecnologias Utilizadas

| Tecnologia                             | Finalidade                                      |
|----------------------------------------|------------------------------------------------|
| Spring Boot 3.5.3                      | Base para os microserviços                     |
| Spring Cloud Netflix Eureka            | Service Discovery                              |
| Spring Cloud Gateway                   | API Gateway e roteamento                       |
| Spring Security + OAuth2 JWT (Keycloak)| Autenticação e autorização                     |
| RabbitMQ                               | Mensageria entre microservices                 |
| Spring Boot Data JPA + H2              | Persistência leve nos microsserviços           |
| SpringDoc OpenAPI                      | Geração automática da documentação Swagger     |
| Spring Boot Actuator                   | Monitoramento e health check                   |
| Dotenv Java                            | Leitura de variáveis do arquivo `.env`         |
| Docker Compose                         | Orquestração de containers                     |

## 🛠️ Estrutura dos Microsserviços

| Microsserviço         | Porta | Descrição                                      |
|-----------------------|------|------------------------------------------------|
| **eurikaserver**      | 8761 | Service Discovery com Eureka                   |
| **msclientes**        | Aleatória | Gerenciamento de clientes (CRUD)          |
| **mscartoes**         | Aleatória | Emissão e consulta de cartões              |
| **msavaliadorcredito**| Aleatória | Avaliação de crédito, comunicação RabbitMQ |
| **mscloudgateway**    | 8080 | API Gateway com autenticação OAuth2           |

## 🔧 Principais Configurações

As variáveis de ambiente são carregadas do arquivo `.env`. Exemplos de variáveis usadas:

```env
EUREKA_SERVER=ms-eureka
RABBITMQ_SERVER=ms-rabbitmq
RABBITMQ_USER=guest
RABBITMQ_PASS=guest
RABBITMQ_PORT=5672
KEYCLOAK_SERVER=http://ms-keycloak
KEYCLOAK_PORT=8081
```

Cada microsserviço possui um arquivo `application.yaml` onde estão definidas:
- Nome da aplicação
- Porta do servidor
- Configuração do Eureka (client)
- Configuração do RabbitMQ
- Configuração de OAuth2 no Gateway
- Arquivo de log dedicado por microserviço

## 🚀 Como Executar

### 1. Requisitos

- Java 21
- Maven 3.9+
- Docker e Docker Compose

### 2. Build dos Microservices

Na raiz do projeto:

```bash
mvn clean package -DskipTests
```

### 3. Rodar os containers

```bash
docker-compose up -d
```

### 4. Acessos importantes

| Serviço     | URL                                               |
|-------------|----------------------------------------------------|
| Eureka      | http://localhost:8761                              |
| API Gateway | http://localhost:8080                              |
| H2 Console  | http://localhost:<porta>/h2-console (msclientes)   |
| Swagger     | http://localhost:<porta>/swagger-ui.html           |
| Keycloak    | http://localhost:8081                               |

## 📜 Bibliotecas Principais de Cada Serviço

### eurikaserver
- spring-cloud-starter-netflix-eureka-server
- spring-boot-starter-security

### msclientes
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-cloud-starter-netflix-eureka-client
- h2
- springdoc-openapi-starter-webmvc-ui

### mscartoes
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-cloud-starter-netflix-eureka-client
- spring-boot-starter-amqp
- h2
- springdoc-openapi-starter-webmvc-ui

### msavaliadorcredito
- spring-boot-starter-web
- spring-cloud-starter-openfeign
- spring-cloud-starter-netflix-eureka-client
- spring-boot-starter-amqp
- springdoc-openapi-starter-webmvc-ui

### mscloudgateway
- spring-cloud-starter-gateway-server-webflux
- spring-boot-starter-oauth2-resource-server
- spring-cloud-starter-netflix-eureka-client

## 🔐 Autenticação e Segurança

- O **API Gateway** valida os tokens JWT emitidos pelo **Keycloak**.
- O **Eureka Server** está protegido com autenticação básica.

## 🔑 Configuração do Keycloak

### 1. Acessar o Keycloak

Abra o Keycloak no navegador:
```
http://localhost:8081
```
Usuário e senha padrão configurados no docker-compose (exemplo):
- Usuário: admin
- Senha: admin


### 2. Criar o Realm

1. Acesse o menu no canto superior esquerdo e clique em **“Realms”**.
2. Clique em **“Create Realm”**.
3. Nomeie como: `mscourserealm`.
4. Clique em **“Create”**.
5. Após criar, vá até **Realm Settings → General** e configure o campo **Frontend URL**:

| Ambiente           | Frontend URL                         |
|--------------------|--------------------------------------|
| Com Docker         | `http://ms-keycloak:8080`            |
| Sem Docker (local) | Deixe em branco                      |

> ⚠️ Isso garante que os redirects e tokens emitidos sejam válidos para o endereço onde o Gateway está acessando o Keycloak.


### 3. Criar o Client

1. Dentro do realm `mscourserealm`, vá em **“Clients”**.
2. Clique em **“Create Client”**.
3. Configure:
    - Client ID: `mscredito`
    - Client Protocol: `openid-connect`
    - Root URL: deixe em branco
4. Clique em **“Save”**.
5. Em **Settings**, habilite **“Authorization Enabled”** e **“Service Accounts Enabled”**, se necessário.
6. Em **Access Settings**, configure:
    - Valid Redirect URIs: `http://localhost:8080/*`
    - Web Origins: `+`

### 4. Criar um Usuário de Teste

1. Vá até **Users**.
2. Clique em **“Add user”**, preencha os campos obrigatórios.
3. Depois de criar, vá até a aba **“Credentials”**, defina uma senha e marque **“Temporary: OFF”**.

### 5. Configurar as Roles (opcional)

Se necessário, crie roles personalizadas em **Roles** e associe aos usuários.

### 6. Verificar URL do OpenID Provider

Após criar o realm, o **Issuer URI** será:
```
http://localhost:8081/realms/mscourserealm
```

Configure este valor no **application.yaml** do mscloudgateway:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8081/realms/mscourserealm
```

## 📊 Monitoramento e Health Check

Todos os serviços expõem os endpoints do **Spring Boot Actuator** para monitoramento e status em `/actuator`.

## 📃 Documentação API (Swagger)

Cada microserviço expõe sua própria documentação Swagger em:

```
http://localhost:<porta-do-microserviço>/swagger-ui.html
```

## 👨‍💻 Autor

Elvis Assis

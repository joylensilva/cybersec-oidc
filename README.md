# CyberSec OIDC - Projeto Educacional de Autenticação OAuth2 e OIDC

## 📚 Sobre o Projeto

Este projeto foi desenvolvido com objetivo educacional para auxiliar no entendimento de **autenticação OAuth2 e OpenID Connect (OIDC)** segura. Ele faz parte de uma aula sobre tipos de autenticação e a evolução da identidade digital, demonstrando na prática os conceitos fundamentais de autenticação moderna.

O projeto implementa duas abordagens de autenticação:
- **OIDC Login** para aplicações web (fluxo de autorização com navegador)
- **JWT Resource Server** para APIs REST (autenticação stateless baseada em tokens)

## 🎯 Objetivos Educacionais

Este projeto demonstra:

1. **Evolução da Identidade Digital**: Como passamos de autenticação básica (usuário/senha) para protocolos modernos como OAuth2 e OIDC
2. **OAuth2 Authorization Code Flow**: O fluxo mais seguro para aplicações web
3. **OpenID Connect**: Camada de identidade sobre OAuth2
4. **JWT (JSON Web Tokens)**: Autenticação stateless para APIs
5. **Separação de Concerns**: Servidor de autorização separado da aplicação
6. **Keycloak**: Identity Provider (IdP) open source para gerenciamento de identidades

## 📂 Estrutura do Projeto

```
cybersec-oidc/
├── src/
│   └── main/
│       ├── java/br/com/cybersec/
│       │   ├── CybersecApplication.java    # Classe principal Spring Boot
│       │   ├── SecurityConfig.java         # Configuração de segurança (OAuth2 + JWT)
│       │   └── HelloController.java        # Endpoints de demonstração
│       └── resources/
│           └── application.yaml            # Configurações OAuth2/OIDC
├── keycloak-compose.yml                    # Docker Compose para Keycloak
├── pom.xml                                 # Dependências Maven
└── README.md                               # Este arquivo
```

## 🔐 Componentes de Código

### 1. SecurityConfig.java

Configura duas cadeias de segurança separadas:

#### **API Security** (`/api/**`)
```java
@Bean
@Order(1)
SecurityFilterChain apiSecurity(HttpSecurity http)
```
- Valida tokens JWT nas requisições
- Endpoint público: `/api/public` (sem autenticação)
- Endpoints protegidos: `/api/**` (requer JWT válido)
- Stateless: sem sessão HTTP

#### **Web Security** (outras rotas)
```java
@Bean
@Order(1)
SecurityFilterChain webSecurity(HttpSecurity http)
```
- Utiliza OAuth2 Login (OIDC)
- Endpoint público: `/public` (sem autenticação)
- Endpoints protegidos: requerem login via Keycloak
- Stateful: mantém sessão HTTP

### 2. HelloController.java

Endpoints de demonstração:

| Endpoint | Tipo | Autenticação | Descrição |
|----------|------|--------------|-----------|
| `/public` | Web | Nenhuma | Acesso público |
| `/private` | Web | OIDC Login | Retorna nome do usuário autenticado |
| `/token` | Web | OIDC Login | Exibe o access token JWT |
| `/api/public` | API | Nenhuma | API pública |
| `/api/private` | API | JWT Bearer | Valida JWT e retorna nome do usuário |

### 3. application.yaml

Configurações do cliente OAuth2 e Resource Server:

```yaml
spring:
  security:
    oauth2:
      client:                              # Configuração como Cliente OAuth2
        registration:
          keycloak:
            client-id: cybersec-client
            client-secret: [secret]
            scope: openid, profile, email
            authorization-grant-type: authorization_code
        provider:
          keycloak:
            issuer-uri: http://localhost:9091/realms/cybersec
      
      resourceserver:                      # Configuração como Resource Server
        jwt:
          issuer-uri: http://localhost:9091/realms/cybersec
```

## 🚀 Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.6+
- Docker (para Keycloak)

### Passo 1: Iniciar o Keycloak

```bash
docker compose -f keycloak-compose.yml up -d
```

O Keycloak estará disponível em: http://localhost:9091
- Usuário admin: `admin`
- Senha admin: `admin`

### Passo 2: Configurar o Keycloak

Acesse o console admin do Keycloak e configure:

1. **Realm**: `cybersec` (se não existir)
2. **Client**: `cybersec-client`
   - Client Authentication: ON
   - Valid Redirect URIs: `http://localhost:8080/login/oauth2/code/keycloak`
   - Client Secret: `4eGQ2eTlBFQHPCQ0nMdqIGkZo09Q7Y1m`
3. **Usuário de teste**: Crie um usuário para testar o login

### Passo 3: Executar a Aplicação Spring Boot

```bash
./mvnw spring-boot:run
```

Ou no Windows:
```bash
mvnw.cmd spring-boot:run
```

A aplicação estará disponível em: http://localhost:8080

## 🧪 Testando a Aplicação

### Teste 1: Endpoints Públicos

```bash
# Web público
curl http://localhost:8080/public

# API pública
curl http://localhost:8080/api/public
```

### Teste 2: Login Web (OIDC)

1. Acesse http://localhost:8080/private no navegador
2. Você será redirecionado para o Keycloak
3. Faça login com suas credenciais
4. Você será redirecionado de volta e verá sua mensagem personalizada

### Teste 3: Obter Token JWT

1. Acesse http://localhost:8080/token após fazer login
2. Copie o token JWT exibido

### Teste 4: API com JWT

```bash
# Substitua [SEU_TOKEN] pelo token obtido no passo anterior
curl -H "Authorization: Bearer [SEU_TOKEN]" http://localhost:8080/api/private
```

## 📖 Conceitos Abordados

### OAuth2 e OpenID Connect

- **OAuth2**: Framework de autorização que permite que aplicações obtenham acesso limitado a recursos
- **OIDC**: Camada de identidade sobre OAuth2, adiciona informações sobre o usuário autenticado
- **Authorization Code Flow**: Fluxo mais seguro, usando redirecionamentos do navegador

### Tokens

- **Access Token (JWT)**: Token que prova a autorização para acessar recursos
- **ID Token**: Token específico do OIDC que contém informações sobre o usuário
- **Refresh Token**: Permite obter novos access tokens sem nova autenticação

### Security Patterns

- **Separation of Concerns**: Servidor de autenticação separado da aplicação
- **Stateless Authentication**: APIs não mantêm estado de sessão
- **Bearer Token**: Token carregado no header Authorization

## 📚 Tecnologias Utilizadas

- **Spring Boot 4.0.2**: Framework Java
- **Spring Security**: Framework de segurança
- **Spring Security OAuth2 Client**: Cliente OAuth2
- **Spring Security OAuth2 Resource Server**: Validação de JWT
- **Keycloak 26.5.2**: Identity Provider
- **Java 21**: Linguagem de programação
- **Maven**: Gerenciador de dependências

## 🎓 Recursos de Aprendizado

Para entender melhor os conceitos:

1. **OAuth2**: https://oauth.net/2/
2. **OpenID Connect**: https://openid.net/connect/
3. **JWT**: https://jwt.io/
4. **Spring Security OAuth2**: https://spring.io/guides/tutorials/spring-boot-oauth2
5. **Keycloak**: https://www.keycloak.org/documentation

## 📝 Notas

- Este é um projeto **educacional** e **demonstrativo**
- Para produção, configure HTTPS e ajuste as configurações de segurança
- Os segredos (secrets) devem ser armazenados de forma segura (variáveis de ambiente, vault, etc.)
- O Keycloak em modo dev não deve ser usado em produção

## 📄 Licença

Este projeto está sob a licença especificada no arquivo LICENSE.

---

**Desenvolvido para fins educacionais**

# Keycloak XP 🚀

**Keycloak XP** é um experimento projetado para auxiliar desenvolvedores de API REST com **Spring Boot** a compreender autenticação e autorização utilizando **Keycloak**. O projeto oferece configurações básicas e funcionalidades essenciais, incluindo:

- 🐳 Contêiner Keycloak com persistência de dados no PostgreSQL
- 🔐 Criação e gestão de usuários
- 🔑 Autenticação e autorização
- 📌 Integração com o SDK do Keycloak para autenticação de servidor
- 🧪 Testes unitários e documentação

## 📦 Tecnologias Utilizadas

| Tecnologia     | Versão       |
| -------------- | ------------ |
| Java           | 21           |
| Spring Boot    | 3.4.2        |
| PostgreSQL     | Mais recente |
| Keycloak       | 26.1.0       |
| Docker Compose | Sim          |

## 🚀 Como Executar

### 1️⃣ Clonando o repositório

```sh
 git clone https://github.com/ronny-souza/keycloak-xp.git
 cd keycloak-xp
```

### 2️⃣ Configuração do Keycloak com Docker

O projeto contém um arquivo `docker-compose.yml` na raiz para criar contêineres do **PostgreSQL** e **Keycloak**.

1. Acesse `src/main/resources/` e copie `init-keycloak-database.sql` para o mesmo diretório onde colocar o `docker-compose.yml`.
2. Inicie os contêineres:

```sh
docker-compose up -d
```

### 3️⃣ Acessando o Keycloak

- URL: [http://localhost:8181](http://localhost:8181)
- Usuário: `admin`
- Senha: `admin`

### 4️⃣ Criando um Realm e Configurando um Client

1. **Criar um novo Realm**: `keycloakxp`
2. **Configurar um Client**:

| Configuração           | Valor                                                      |
| ---------------------- | ---------------------------------------------------------- |
| Client ID              | keycloakxp                                                 |
| Client Authentication  | Habilitado                                                 |
| Fluxos de Autenticação | Standard Flow, Direct Access Grants, Service Account Roles |

### 5️⃣ Configuração da API

No `application.yml`, adicione as seguintes variáveis de ambiente:

```yaml
keycloak:
  server: ${KEYCLOAK_SERVER}
  realm: ${KEYCLOAK_REALM}
  clients:
    default:
      clientId: ${DEFAULT_CLIENT_ID}
      clientSecret: ${DEFAULT_CLIENT_SECRET}
```

| Variável                | Exemplo                 |
| ----------------------- | ----------------------- |
| `KEYCLOAK_SERVER`       | `http://localhost:8181` |
| `KEYCLOAK_REALM`        | `keycloakxp`            |
| `DEFAULT_CLIENT_ID`     | `keycloakxp`            |
| `DEFAULT_CLIENT_SECRET` | `chave_secreta`         |

### 6️⃣ Executando a API

```sh
mvn spring-boot:run
```

Acesse a documentação do Swagger para testar os endpoints: 👉 [http://localhost:8095/swagger-ui/index.html](http://localhost:8095/swagger-ui/index.html)

## 🐳 Como executar utilizando Docker?

A API está disponível no Docker Hub, e pode ser utilizada por meio de uma configuração adicionada ao arquivo [`docker-compose.yml`](https://github.com/ronny-souza/keycloak-xp/blob/main/docker-compose.yml), presente nesse repositório.

### 1️⃣ Configurando o arquivo docker-compose.yml

No arquivo `docker-compose.yml`, adicione a seguinte configuração:

```yaml
  keycloakxp-api:
    container_name: keycloakxp-api
    image: ronyerimsa/keycloakxp-api:latest
    environment:
      - APP_OAUTH2_JWK_ISSUER_URI=http://keycloak:8080/realms/keycloakxp
      - KEYCLOAK_SERVER=http://keycloak:8080
      - KEYCLOAK_REALM=keycloakxp
      - DEFAULT_CLIENT_ID=keycloakxp
      - DEFAULT_CLIENT_SECRET=h3L7jiRII72FmRFFcwChSS3wXhwsbWEF
      - API_DEVELOPER_CONTACT=ronyerimarinho19@gmail.com
    ports:
      - 8095:8095
    depends_on:
      - keycloak
```

A configuração acima determina utiliza a versão mais recente da aplicação, que pode ser visualizada [aqui](https://hub.docker.com/r/ronyerimsa/keycloakxp-api). As variáveis de ambiente foram configuradas para se comunicar com o serviço do Keycloak presente no `docker-compose.yml`, que está executando na porta 8080. Ao alterar o serviço ou portas, certifique-se de ajustar todo o arquivo de configuração.

### 2️⃣ Variáveis de ambiente

As seguintes variáveis de ambiente podem ser configuradas, e são **obrigatórias**:

| Variável                | Exemplo                 | Descrição                 |                  
| ----------------------- | ----------------------- |
| `APP_OAUTH2_JWK_ISSUER_URI`       | `http://keycloak:8080/realms/keycloakxp` | URI do emissor JWK para autenticação OAuth2. |
| `KEYCLOAK_SERVER`       | `http://keycloak:8080` | URL do servidor Keycloak. |
| `KEYCLOAK_REALM`        | `keycloakxp`            |  Nome do realm no Keycloak. |
| `DEFAULT_CLIENT_ID`     | `keycloakxp`            |  ID do cliente padrão registrado no Keycloak. |
| `DEFAULT_CLIENT_SECRET` | `chave_secreta`         |  Segredo do cliente padrão. |
| `API_DEVELOPER_CONTACT` | `youremail@example.com`         |  E-mail de contato disponível na interface pública do Swagger da API. |

### 3️⃣ Executando os contêineres

```sh
docker-compose up -d
```

### 4️⃣ Acessando a API

Acesse a documentação do Swagger para testar os endpoints: 👉 [http://localhost:8095/swagger-ui/index.html](http://localhost:8095/swagger-ui/index.html)

## 📄 Licença

Este projeto está licenciado sob a **MIT License** - Sinta-se livre para usar, modificar e contribuir!

---

💡 **Dúvidas ou sugestões?** Abra uma **issue** ou contribua com um **pull request**! 😉


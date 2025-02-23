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

Acesse a documentação do Swagger para testar os endpoints: 👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## 📄 Licença

Este projeto está licenciado sob a **MIT License** - Sinta-se livre para usar, modificar e contribuir!

---

💡 **Dúvidas ou sugestões?** Abra uma **issue** ou contribua com um **pull request**! 😉


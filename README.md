
# Projeto Shelter Management API

Este projeto é uma API RESTful desenvolvida em Java usando Spring Boot, que permite realizar operações CRUD (Create, Read, Update, Delete) em entidades do tipo **Shelter** (Abrigo). O objetivo principal é administrar informações sobre abrigos, incluindo nome, endereço e capacidade, e persistir esses dados em um banco de dados SQL Azure.

## Funcionalidade do Projeto

A API oferece os seguintes endpoints:

- **POST /api/shelters**: Cria um novo shelter.
- **GET /api/shelters**: Retorna uma lista de todos os shelters cadastrados.
- **GET /api/shelters/{id}**: Retorna os detalhes de um shelter específico pelo seu ID.
- **PUT /api/shelters/{id}**: Atualiza as informações de um shelter existente.
- **DELETE /api/shelters/{id}**: Exclui um shelter pelo seu ID.

Cada shelter possui os atributos:
- `id`: Identificador único (Long).
- `name`: Nome do abrigo (String).
- `address`: Endereço completo do abrigo (String).
- `capacity`: Capacidade máxima de pessoas no abrigo (Integer).

## Estrutura de Pastas

```plaintext
root/
├── README.md              # Este arquivo de documentação
├── azure-pipelines.yml    # Pipeline de Build/Deploy no Azure DevOps
└── demo/                  # Projeto principal Spring Boot
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml            # Arquivo de configuração do Maven
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── com/example/demo/
        │   │       ├── DemoApplication.java   # Classe principal Spring Boot
        │   │       ├── controller/
        │   │       │   └── ShelterController.java  # Endpoints REST
        │   │       ├── model/
        │   │       │   └── Shelter.java            # Entidade JPA
        │   │       └── repository/
        │   │           └── ShelterRepository.java  # Interface JPA Repository
        │   └── resources/
        │       ├── application.properties       # Configurações de datasource e JPA
        │       └── application-azure.properties  # (Opcional) Configurações específicas para Azure
        └── test/
            └── java/
                └── com/example/demo/
                    └── DemoApplicationTests.java  # Testes unitários
```

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
  - Spring Web (starter)
  - Spring Data JPA (starter)
- Banco de Dados: SQL Server hospedado no Azure SQL
- Maven para gerenciamento de dependências e build
- Azure DevOps Pipelines (azure-pipelines.yml) para CI/CD
- Postman (ou qualquer cliente HTTP) para testes manuais

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- JDK 17 (ou superior)
- Maven 3.x
- Conta no Azure com:  
  - Instância de Azure SQL Database configurada  
  - App Service (caso queira deploy em nuvem)  
- (Opcional) Azure CLI ou Azure DevOps configurado para pipelines

## Passo a Passo para Executar Localmente

1. **Obter o código**

   Clone este repositório ou descompacte o diretório que contém a estrutura acima.

   ```bash
   git clone https://github.com/viniciors/GS-devops.git
   cd GS-devops
   ```

2. **Configurar o banco de dados alvo**

   Se você deseja testar localmente contra o mesmo Azure SQL usado em produção, basta manter o `application.properties` com a string de conexão. Caso queira usar um banco local ou H2, ajuste conforme sua necessidade.

   Exemplo (configuração padrão no projeto):
   ```properties
   spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:sqlserver://servidor-sqldb-gs-demo.database.windows.net:1433;database=sqldb-gs-demo;user=adm@servidor-sqldb-gs-demo;password=@Gs12345;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;}
   spring.datasource.username=${SPRING_DATASOURCE_USERNAME:adm@servidor-sqldb-gs-demo}
   spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:@Gs12345}
   spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
   spring.jpa.show-sql=true
   spring.jpa.hibernate.ddl-auto=update
   ```

   - **Caso utilize Azure SQL**: não é necessário alterar nada. O placeholder já define o valor padrão que conecta ao Azure.
   - **Caso prefira banco local**:
     - Instale e rode um SQL Server local.  
     - Substitua as variáveis diretamente no `application.properties`, por exemplo:
       ```properties
       spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=MeuDbLocal;user=sa;password=SenhaLocal123;encrypt=false;
       spring.datasource.username=sa
       spring.datasource.password=SenhaLocal123
       ```

3. **Rodar a aplicação**

   Execute o Maven para compilar e iniciar o Spring Boot:
   ```bash
   mvn clean package -f demo/pom.xml
   mvn spring-boot:run -f demo/pom.xml
   ```
   ou simplesmente:
   ```bash
   java -jar demo/target/demo-1.0.0.jar
   ```

   O servidor embutido (Tomcat) será iniciado na porta `8080`.

4. **Testar endpoints usando Postman**

   Certifique-se de que a aplicação esteja rodando em `http://localhost:8080`. Nos exemplos abaixo, substitua `http://localhost:8080` pela URL base, se diferente.

   ### Criar um Shelter (POST)
   - **URL**: `POST http://localhost:8080/api/shelters`
   - **Headers**:
     ```http
     Content-Type: application/json
     ```
   - **Body (raw JSON)**:
     ```json
     {
       "name": "Abrigo Local",
       "address": "Rua Local, 123",
       "capacity": 100
     }
     ```

   ### Listar todos os Shelters (GET)
   - **URL**: `GET http://localhost:8080/api/shelters`
   - **Response Esperado**:
     ```json
     [
       {
         "id": 1,
         "name": "Abrigo Local",
         "address": "Rua Local, 123",
         "capacity": 100
       }
     ]
     ```

   ### Obter Shelter por ID (GET)
   - **URL**: `GET http://localhost:8080/api/shelters/1`
   - **Response**:
     ```json
     {
       "id": 1,
       "name": "Abrigo Local",
       "address": "Rua Local, 123",
       "capacity": 100
     }
     ```

   ### Atualizar Shelter (PUT)
   - **URL**: `PUT http://localhost:8080/api/shelters/1`
   - **Headers**:
     ```http
     Content-Type: application/json
     ```
   - **Body (raw JSON)**:
     ```json
     {
       "name": "Abrigo Atualizado",
       "address": "Rua Atualizada, 456",
       "capacity": 150
     }
     ```
   - **Response Esperado**:
     ```json
     {
       "id": 1,
       "name": "Abrigo Atualizado",
       "address": "Rua Atualizada, 456",
       "capacity": 150
     }
     ```

   ### Excluir Shelter (DELETE)
   - **URL**: `DELETE http://localhost:8080/api/shelters/1`
   - **Response Esperado**: `204 No Content` ou `200 OK` (conforme implementação).

## Passo a Passo para Deploy no Azure (através do Azure DevOps Pipeline)

Este passo considera que você já possui:
- Conta Azure, Resource Group, Azure SQL Server/Database e App Service criados.
- Azure DevOps com Service Connection configurada.

1. **Configurar as variáveis no App Service**
   - Acesse **App Service → gs-app → Configuração → Configurações de aplicativo**.
   - Adicione as seguintes entradas:
     - `SPRING_DATASOURCE_URL`:  
       ```
       jdbc:sqlserver://servidor-sqldb-gs-demo.database.windows.net:1433;database=sqldb-gs-demo;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
       ```
     - `SPRING_DATASOURCE_USERNAME`: `adm@servidor-sqldb-gs-demo`
     - `SPRING_DATASOURCE_PASSWORD`: `@Gs12345`
   - Clique em **Salvar** para reiniciar o App Service.

2. **Pipeline YAML (azure-pipelines.yml)**

   Coloque este arquivo na raiz do repositório (ao lado da pasta `demo`):
   ```yaml
   trigger:
     - main

   pool:
     vmImage: 'ubuntu-latest'

   variables:
     azureSubscription: 'AzureConnection'    # Nome da sua connection string no Azure DevOps
     azureResourceGroup: 'rg-demo-app'      # Resource Group onde está o App Service
     azureWebAppName: 'gs-app'             # Nome do App Service

   jobs:
     - job: BuildAndDeploy
       displayName: 'Build + Deploy'
       steps:
         # 1) Build com Maven
         - task: Maven@3
           displayName: 'Compilar projeto Spring Boot'
           inputs:
             mavenPomFile: 'demo/pom.xml'
             goals: 'clean package'
             javaHomeOption: 'JDKVersion'
             jdkVersionOption: '1.17'

         # 2) Deploy direto do JAR gerado
         - task: AzureWebApp@1
           displayName: 'Deploy no Azure App Service'
           inputs:
             azureSubscription: '$(azureSubscription)'
             appName: '$(azureWebAppName)'
             resourceGroupName: '$(azureResourceGroup)'
             package: 'demo/target/*.jar'
             appSettings: |
               -WEBSITE_RUN_FROM_PACKAGE 1
               # Aqui podemos forçar profiles: se quiser, adicione:
               #-SPRING_PROFILES_ACTIVE azure
   ```

   - **Notas**:
     - O `package: 'demo/target/*.jar'` aponta diretamente para o JAR compilado pelo Maven.
     - O `WEBSITE_RUN_FROM_PACKAGE 1` faz com que o App Service execute a partir do pacote enviado sem descompactar.
     - Como já definimos `SPRING_DATASOURCE_*` diretamente no App Service, não precisamos injetar mais variáveis no YAML.

3. **Executar pipeline**
   - Faça commit/push no repositório com o arquivo `azure-pipelines.yml`.  
   - No Azure DevOps, vá até Pipelines e observe que o pipeline disparará automaticamente.  
   - Aguarde as etapas de Build e Deploy concluírem com sucesso.

4. **Verificar no App Service**
   - Abra **App Service → gs-app → Log stream** e confirme que, ao reiniciar, aparece:
     ```
     Conectando a jdbc:sqlserver://servidor-sqldb-gs-demo.database.windows.net:1433;database=sqldb-gs-demo;...
     HikariPool-1 - Start completed.
     HHH000400: Using dialect: org.hibernate.dialect.SQLServerDialect
     ```
   - Realize uma requisição **POST** via Postman para `https://gs-app.azurewebsites.net/api/shelters` (conforme exemplos anteriores) e confirme o INSERT no log.  
   - Faça **GET** e verifique se retorna a lista de shelters.

## JSON de Teste para Postman

A seguir estão os exemplos de JSON que você pode copiar para testar cada operação CRUD no Postman:

### 1) Criar Shelter (POST)
- **URL**: `POST https://<seu-app>.azurewebsites.net/api/shelters`
- **Body (raw JSON)**:
  ```json
  {
    "name": "Abrigo Teste",
    "address": "Rua das Árvores, 123",
    "capacity": 80
  }
  ```

### 2) Listar Shelters (GET)
- **URL**: `GET https://<seu-app>.azurewebsites.net/api/shelters`
- **Não há body**

### 3) Obter Shelter por ID (GET)
- **URL**: `GET https://<seu-app>.azurewebsites.net/api/shelters/1`
- **Não há body**

### 4) Atualizar Shelter (PUT)**
- **URL**: `PUT https://<seu-app>.azurewebsites.net/api/shelters/1`
- **Body (raw JSON)**:
  ```json
  {
    "name": "Abrigo Atualizado",
    "address": "Avenida Principal, 456",
    "capacity": 120
  }
  ```

### 5) Excluir Shelter (DELETE)
- **URL**: `DELETE https://<seu-app>.azurewebsites.net/api/shelters/1`
- **Não há body**

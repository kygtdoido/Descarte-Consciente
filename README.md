# Descarte Consciente

Sistema desktop desenvolvido em **Java** para auxiliar no gerenciamento de descartes, doações e usuários, com foco em incentivar práticas mais conscientes de descarte e reaproveitamento de materiais.

O projeto foi desenvolvido como trabalho acadêmico, utilizando **Java Swing** para a interface gráfica, **MySQL** para o banco de dados e **JDBC** para a conexão entre a aplicação e o banco.

---

## Objetivo do Projeto

O objetivo do sistema é facilitar o controle de informações relacionadas ao descarte consciente, permitindo o cadastro e gerenciamento de usuários, doações e registros relacionados ao descarte de materiais.

A proposta busca unir tecnologia e sustentabilidade, oferecendo uma solução simples para organização e acompanhamento dessas informações.

---

## Tecnologias Utilizadas

* Java
* Java Swing
* JDBC
* MySQL
* Maven
* NetBeans
* Git e GitHub

---

## Funcionalidades

* Tela de login
* Cadastro de usuários
* Autenticação de usuário administrador
* Gerenciamento de informações do sistema
* Cadastro e controle de doações
* Registro de descartes
* Integração com banco de dados MySQL
* Interface gráfica desenvolvida com Java Swing

---

## Estrutura do Projeto

```text
DescarteConsciente/
├── src/
│   └── main/
│       └── java/
│           └── br/
│               └── com/
│                   └── descarteconsciente/
│                       ├── dao/
│                       ├── model/
│                       ├── util/
│                       └── view/
├── databaseV4.sql
├── pom.xml
├── README.md
└── .gitignore
```

---

## Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

* Java JDK 17 ou superior
* MySQL Server
* NetBeans ou outra IDE compatível com Maven
* Git, caso deseje clonar o repositório

---

## Como Executar o Projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/kygtdoido/Descarte-Consciente.git
```

Depois, acesse a pasta do projeto:

```bash
cd Descarte-Consciente/DescarteConsciente_FINAL
```

---

### 2. Abrir o projeto na IDE

Abra o projeto no **NetBeans** ou em outra IDE compatível com projetos Maven.

No NetBeans:

1. Clique em **File**
2. Clique em **Open Project**
3. Selecione a pasta do projeto
4. Aguarde o Maven carregar as dependências

---

### 3. Criar o banco de dados

Abra o MySQL Workbench, phpMyAdmin ou outro gerenciador MySQL e execute o arquivo:

```text
databaseV4.sql
```

Esse arquivo cria a estrutura necessária do banco de dados e insere os dados iniciais para teste.

---

### 4. Configurar a conexão com o banco

No projeto, acesse a classe responsável pela conexão com o banco de dados, normalmente localizada em:

```text
src/main/java/br/com/descarteconsciente/util/DatabaseConnection.java
```

Verifique e ajuste os dados conforme o seu ambiente local:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/NOME_DO_BANCO";
private static final String DB_USER = "root";
private static final String DB_PASS = "Sua_Senha_aqui";
```

Altere:

* `NOME_DO_BANCO` para o nome do banco criado pelo arquivo SQL
* `root` para o seu usuário do MySQL, se for diferente
* `Sua_Senha_aqui` para a senha do seu MySQL local

> Observação: por segurança, não é recomendado colocar senhas reais em repositórios públicos.

---

### 5. Executar o sistema

Após configurar o banco de dados:

1. Abra o projeto no NetBeans
2. Aguarde o carregamento das dependências Maven
3. Execute a classe principal do sistema
4. Faça login utilizando o usuário administrador de teste

---

## Usuário Administrador de Teste

O sistema possui um usuário administrador inicial para testes locais:

```text
E-mail: admin@descarte.com
Senha: admin
```

Esse usuário é apenas para fins de teste e demonstração do projeto.

Em um ambiente real, recomenda-se alterar a senha padrão após o primeiro acesso.

---

## Banco de Dados

O banco de dados utilizado é o **MySQL**.

O script de criação do banco está disponível no arquivo:

```text
databaseV4.sql
```

Esse arquivo deve ser executado antes de iniciar o sistema.

---

## Observações Importantes

* A pasta `target/` não deve ser enviada ao GitHub, pois é gerada automaticamente pelo Maven.
* Arquivos de backup, versões antigas e cópias duplicadas também não devem ser enviados.
* Senhas reais, tokens, chaves de API ou dados sensíveis não devem ser colocados no repositório.
* O login `admin@descarte.com / admin` é apenas um usuário de teste do sistema.

---

## Sugestão de `.gitignore`

Crie um arquivo chamado `.gitignore` na raiz do projeto com o seguinte conteúdo:

```gitignore
# Arquivos gerados pelo Maven
target/

# Arquivos compilados
*.class

# Logs
*.log

# NetBeans
nbproject/private/
build/
dist/

# Configurações locais e sensíveis
.env
config.properties

# Sistema operacional
.DS_Store
Thumbs.db
```

---

## Status do Projeto

Projeto acadêmico em desenvolvimento/concluído para fins de estudo e apresentação.

---

## Autor

**Kevyn Rodrigues Nunes de Souza**

Estudante de Ciência da Computação
Universidade São Judas Tadeu - USJT

---

## Licença

Este projeto foi desenvolvido para fins acadêmicos e de estudo.

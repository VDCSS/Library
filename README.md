# Biblioteca Java Comunicação 2.0

Projeto em Java desenvolvido para demonstrar e facilitar rotinas de comunicação entre sistemas, incluindo troca de mensagens, integração via API e padronização de serviços.

## 🚀 Objetivo

Fornecer uma base sólida e extensível para comunicação entre módulos ou aplicações, com foco em simplicidade, organização e boas práticas.

## 📦 Funcionalidades

* Envio e recebimento de mensagens
* Estrutura modular e de fácil expansão
* Suporte a diferentes protocolos (HTTP, JSON, etc.)
* Tratamento de erros e respostas padronizadas

## 🛠 Tecnologias Utilizadas

* **Java 17+**

* **Spring Boot 3.x**

* **Maven**

* **APIs REST (Spring Web)**

* **Lombok**

* **Jackson (JSON)**

* **Node.js & NPM** (necessários para funcionalidades frontend ou scripts auxiliares)

* **Java 17+**

* **Spring Boot 3.x**

* **Maven**

* **APIs REST (Spring Web)**

* **Lombok** para reduzir boilerplate

* **JSON Processing** (Jackson)

## 📦 Dependências Necessárias

As dependências abaixo devem estar no seu `pom.xml` para garantir o funcionamento completo do projeto:

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Jackson (JSON) -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>

    <!-- Spring Boot Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 📂 Estrutura do Projeto

```
/src
  /main
    /java
      ...classes principais
    /resources
      application.properties
```

## 📘 Como Executar

1. Clone o repositório:

```
git clone <url-do-repositorio>
```

2. Compile e execute:

```
mvn spring-boot:run
```

## 📄 Licença

Projeto livre para uso e modificação.

---

Atualizado para a versão **Comunicação 2.0**.

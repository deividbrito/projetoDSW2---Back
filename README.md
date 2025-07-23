
# 📦 API - Controle Financeiro Pessoal

Este repositório contém a API REST desenvolvida em Java com Servlets, responsável por gerenciar transações financeiras pessoais. É consumida pelo front-end presente [neste repositório](https://github.com/deividbrito/projetoDSW2---Front).

## 🔧 Tecnologias Utilizadas

- Java 17+
- Servlets (Jakarta EE)
- Apache Tomcat 10.1
- MySQL
- Maven
- GSON
- JDBC com Connection Pool via DataSource (JNDI)

## 📁 Estrutura do Projeto

```
br.dsw2
├── command/
├── controller/
├── dao/
├── factory/
├── filter/  ← CORS Filter
├── model/
```

## 🔐 Configuração de Banco de Dados

A conexão é feita via JNDI (`context.xml`):

```xml
<Resource name="jdbc/FinanceiroDS"
          auth="Container"
          type="javax.sql.DataSource"
          maxTotal="20" maxIdle="10"
          username="root"
          password="SENHA"
          driverClassName="com.mysql.cj.jdbc.Driver"
          url="jdbc:mysql://localhost:3306/controle_financeiro"/>
```

Inclua esse trecho no `context.xml` do Tomcat.

## 📑 Documentação da API

A API permite gerenciar transações financeiras (entradas e saídas). Abaixo estão os **endpoints** disponíveis com base no padrão REST.

---

### **1. GET /transacoes** – Listar todas as transações

**Descrição:** Retorna uma lista com todas as transações cadastradas no sistema.  
**Método HTTP:** `GET`  
**Endpoint:** `/transacoes`  
**Parâmetros:** Nenhum.

**Códigos de Resposta:**
- `200 OK` – Requisição bem-sucedida, transações retornadas.
- `500 Internal Server Error` – Erro interno ao buscar os dados.

---

### **2. POST /transacoes** – Criar nova transação

**Descrição:** Cadastra uma nova transação no banco de dados.  
**Método HTTP:** `POST`  
**Endpoint:** `/transacoes`

**Corpo da Requisição (JSON):**
```json
{
  "descricao": "Salário",
  "valor": 2500.00,
  "tipo": "entrada",
  "data": "2025-07-10"
}
```

**Códigos de Resposta:**
- `201 Created` – Transação criada com sucesso.
- `400 Bad Request` – Dados ausentes ou inválidos.
- `500 Internal Server Error` – Erro ao persistir os dados.

---

### **3. PUT /transacoes/{id}** – Atualizar uma transação

**Descrição:** Atualiza os dados de uma transação existente com base no ID.  
**Método HTTP:** `PUT`  
**Endpoint:** `/transacoes/{id}`

**Parâmetros:**  
- `id` – ID da transação a ser atualizada.

**Corpo da Requisição (JSON):**
```json
{
  "descricao": "Mercado",
  "valor": 150.75,
  "tipo": "saida",
  "data": "2025-07-15"
}
```

**Códigos de Resposta:**
- `200 OK` – Transação atualizada com sucesso.
- `400 Bad Request` – Dados inválidos.
- `404 Not Found` – Transação não encontrada.
- `500 Internal Server Error` – Erro ao atualizar os dados.

---

### **4. DELETE /transacoes/{id}** – Excluir uma transação

**Descrição:** Remove uma transação do banco de dados pelo ID.  
**Método HTTP:** `DELETE`  
**Endpoint:** `/transacoes/{id}`

**Parâmetros:**  
- `id` – ID da transação a ser excluída.

**Códigos de Resposta:**
- `204 No Content` – Transação removida com sucesso.
- `404 Not Found` – Nenhuma transação encontrada com o ID.
- `500 Internal Server Error` – Erro ao remover os dados.

---

### **5. GET /transacoes/{id}** – Obter transação específica

**Descrição:** Retorna os dados de uma única transação, a partir do ID informado.  
**Método HTTP:** `GET`  
**Endpoint:** `/transacoes/{id}`

**Parâmetros:**  
- `id` – ID da transação desejada.

**Códigos de Resposta:**
- `200 OK` – Transação encontrada.
- `404 Not Found` – Nenhuma transação com o ID fornecido.
- `500 Internal Server Error` – Erro ao buscar os dados.

---

## 🧪 Testes

Todos os endpoints foram testados via **Postman**. A aplicação pode ser consumida por qualquer front-end habilitado com CORS.

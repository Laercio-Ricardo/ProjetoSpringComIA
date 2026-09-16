# 💰 WalletAI - Assistente Financeiro com Inteligência Artificial

API inteligente de orçamento e controle financeiro desenvolvida com **Java 26**, **Spring Boot 4.1.1** e **Spring AI 2.0.1**, utilizando **Google Gemini** para processamento de linguagem natural e **Tool Calling (Function Calling)** para automação de persistência em banco de dados.

O projeto conta também com uma interface web moderna estilo *Fintech (Dark Mode)* servida diretamente pelo Spring Boot.

---

## 🚀 Tecnologias Utilizadas

* **Java 26** (Toolchain configurada)
* **Spring Boot 4.1.1**
* **Spring AI 2.0.1** (Com integração Google GenAI)
* **Google Gemini (`gemini-3.6-flash`)** via Google AI Studio
* **Spring Data JPA & Hibernate**
* **Banco de Dados H2** (Em memória)
* **Front-end:** HTML5, CSS3 e JavaScript (Interface SPA moderna)

---

## 🧠 Arquitetura e Funcionalidades

1. **Processamento de Linguagem Natural (PLN):** O usuário envia comandos em texto livre (ex: *"Cadastre uma receita de salário de 5000"*).
2. **Tool Calling (Function Calling):** A inteligência artificial analisa a intenção do usuário e decide automaticamente quando e como acionar as funções Java anotadas com `@Tool` (`TransacaoTools`).
3. **Persistência de Dados:** As transações (receitas e despesas) são validadas, mapeadas e salvas no banco de dados H2 via JPA.
4. **Interface Gráfica de Banco Digital:** Um painel moderno em modo escuro com atalhos rápidos para testes intuitivos.

---

## 📂 Estrutura do Projeto

```text
src/
├── main/
│   ├── java/com/exemplo/orcamento/
│   │   ├── controller/      # Endpoints REST (/api/orcamento/comando)
│   │   ├── model/           # Entidades JPA e Enums (Transacao, TipoTransacao)
│   │   ├── repository/      # Repositórios de dados Spring Data JPA
│   │   ├── service/         # Regras de negócio e Orquestração do ChatClient (OrcamentoAiService)
│   │   └── tool/            # Ferramentas de IA para Tool Calling (@Tool)
│   └── resources/
│       ├── static/          # Interface Front-end (index.html estilo Fintech)
│       └── application.properties # Configurações do H2 e API Key do Gemini
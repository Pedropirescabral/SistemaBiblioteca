# 📚 Sistema de Gerenciamento de Biblioteca

## Status do Projeto
🟡 **Em Desenvolvimento**

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Uso |
|---|---|
| Java (JDK 11+) | Linguagem principal da aplicação |
| Java Swing | Interface gráfica desktop |
| MySQL | Banco de dados relacional |
| JDBC | Conexão Java ↔ MySQL |
| NetBeans IDE | Ambiente de desenvolvimento |
| Git / GitHub | Versionamento de código |

---

## 👨‍💻 Time de Desenvolvedores

| Nome | Função |
|---|---|
| Pedro Pires Cabral | Desenvolvedor Full Stack |

---

## 🎯 Objetivo do Software

O **Sistema de Gerenciamento de Biblioteca** tem como objetivo facilitar o controle de acervo, cadastro de leitores e gestão de empréstimos de uma biblioteca, substituindo processos manuais por uma solução digital desktop integrada a banco de dados.

---

## ✅ Funcionalidades do Sistema (Requisitos)

### 📖 Módulo de Livros
- Cadastro de livros (Título, Autor, ISBN, Ano de Publicação, Estoque)
- Listagem de todos os livros cadastrados
- Persistência dos dados no banco MySQL

### 👤 Módulo de Leitores
- Cadastro de leitores (Nome, CPF, Telefone, E-mail)
- Listagem de todos os leitores cadastrados
- Persistência dos dados no banco MySQL

### 🔄 Módulo de Empréstimos
- Registro de empréstimos vinculando livro e leitor
- Definição de data do empréstimo e data de devolução prevista
- Listagem de empréstimos ativos
- **Destaque visual** (amarelo + vermelho) para empréstimos com devolução em atraso
- Registro de devolução (remoção do empréstimo ativo)

### 🗄️ Banco de Dados
- Script SQL para criação automática das tabelas (`banco.sql`)
- Dados iniciais de teste incluídos no script
- Relacionamentos via chaves estrangeiras entre livros, leitores e empréstimos

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- JDK 11 ou superior instalado
- MySQL Server em execução
- NetBeans IDE
- Driver JDBC: `mysql-connector-j` (adicionar em Libraries)

### Passo a Passo

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/SistemaBiblioteca.git
```

2. **Configure o banco de dados**
   - Abra o MySQL Workbench
   - Execute o arquivo `banco.sql`

3. **Configure a conexão**
   - Abra `src/sistemaBiblioteca/ConexaoDB.java`
   - Ajuste o campo `SENHA` com a sua senha do MySQL

4. **Adicione o driver JDBC**
   - Clique com o botão direito em *Libraries* no NetBeans
   - Selecione *Add JAR/Folder* e aponte para o `mysql-connector-j.jar`

5. **Execute o projeto**
   - Pressione `F6` ou clique em *Run*

---

## 🗂️ Estrutura do Projeto

```
SistemaBiblioteca/
├── banco.sql                          # Script de criação do banco de dados
├── src/
│   └── sistemaBiblioteca/
│       ├── Main.java                  # Ponto de entrada
│       ├── TelaPrincipal.java         # JFrame principal + sidebar + CardLayout
│       ├── ConexaoDB.java             # Conexão JDBC com MySQL
│       ├── PainelLivros.java          # Tela de cadastro de livros
│       ├── PainelLeitores.java        # Tela de cadastro de leitores
│       ├── PainelEmprestimos.java     # Tela de empréstimos
│       ├── Livro.java                 # Modelo de dados - Livro
│       ├── Leitor.java                # Modelo de dados - Leitor
│       └── Emprestimo.java            # Modelo de dados - Empréstimo
├── lib/
│   └── mysql-connector-j.jar          # Driver JDBC (adicionar manualmente)
└── nbproject/                         # Configurações do NetBeans
```

---

## 📋 Histórico de Etapas

| Etapa | Descrição | Status |
|---|---|---|
| Etapa 1 | Modelagem de banco de dados (conceitual, lógico, físico) | ✅ Concluída |
| Etapa 2 | Projeto de interfaces (wireframes) | ✅ Concluída |
| Etapa 3 | Implementação das telas em Java Swing | ✅ Concluída |
| Etapa 4 | Integração com banco de dados MySQL via JDBC | ✅ Concluída |
| Etapa 5 | Versionamento com Git e GitHub | 🟡 Em andamento |

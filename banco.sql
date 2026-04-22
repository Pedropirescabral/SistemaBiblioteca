-- ============================================================
--  Sistema de Biblioteca  |  Script de Banco de Dados
--  Executar no MySQL Workbench antes de rodar o projeto
-- ============================================================

CREATE DATABASE IF NOT EXISTS biblioteca;
USE biblioteca;

-- ------------------------------------------------------------
--  Tabela: livros
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS livros (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    titulo   VARCHAR(255) NOT NULL,
    autor    VARCHAR(255) NOT NULL,
    isbn     VARCHAR(50),
    ano      INT,
    estoque  INT NOT NULL DEFAULT 0
);

-- ------------------------------------------------------------
--  Tabela: leitores
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS leitores (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL,
    cpf       VARCHAR(20)  NOT NULL,
    telefone  VARCHAR(20),
    email     VARCHAR(255)
);

-- ------------------------------------------------------------
--  Tabela: emprestimos
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS emprestimos (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    livro_id         INT         NOT NULL,
    leitor_id        INT         NOT NULL,
    data_emprestimo  VARCHAR(10) NOT NULL,
    data_devolucao   VARCHAR(10) NOT NULL,
    FOREIGN KEY (livro_id)  REFERENCES livros(id),
    FOREIGN KEY (leitor_id) REFERENCES leitores(id)
);

-- ------------------------------------------------------------
--  Dados iniciais para teste
-- ------------------------------------------------------------
INSERT INTO livros (titulo, autor, isbn, ano, estoque) VALUES
('Dom Casmurro',          'Machado de Assis', '978-8535902778', 1899, 3),
('O Cortiço',             'Aluísio Azevedo',  '978-8535910636', 1890, 5),
('Iracema',               'José de Alencar',  '978-8508176809', 1865, 2),
('O Alienista',           'Machado de Assis', '978-8525406781', 1882, 4),
('Memórias Póstumas',     'Machado de Assis', '978-8535909654', 1881, 3);

INSERT INTO leitores (nome, cpf, telefone, email) VALUES
('João da Silva',    '111.222.333-44', '(48) 99999-1111', 'joao@email.com'),
('Maria Oliveira',   '555.666.777-88', '(48) 98888-2222', 'maria@email.com'),
('Carlos Souza',     '999.888.777-66', '(48) 97777-3333', 'carlos@email.com');

INSERT INTO emprestimos (livro_id, leitor_id, data_emprestimo, data_devolucao) VALUES
(1, 1, '01/04/2026', '15/04/2026'),
(2, 2, '05/04/2026', '10/04/2026');

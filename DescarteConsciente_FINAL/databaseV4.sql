
-- ============================================================
--  DESCARTE CONSCIENTE - BANCO COMPLETO
--  Sistema de Coleta de Medicamentos Vencidos | ODS 3
-- ============================================================

-- =========================
-- Criação do banco
-- =========================
CREATE DATABASE IF NOT EXISTS descarte_consciente
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE descarte_consciente;

-- ============================================================
-- REMOVE TABELAS ANTIGAS (evita conflito)
-- ============================================================

DROP TABLE IF EXISTS lembretes;
DROP TABLE IF EXISTS pontos_coleta;
DROP TABLE IF EXISTS usuarios;

-- ============================================================
-- TABELA USUÁRIOS
-- ============================================================

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(100) NOT NULL,

    email VARCHAR(150) NOT NULL UNIQUE,

    senha VARCHAR(64) NOT NULL,

    is_admin TINYINT(1) DEFAULT 0,

    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================
-- TABELA PONTOS DE COLETA
-- ============================================================

CREATE TABLE pontos_coleta (

    id INT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(150) NOT NULL UNIQUE,

    tipo ENUM(
        'FARMACIA',
        'DROGARIA',
        'POSTO_SAUDE',
        'PONTO_MUNICIPAL'
    ) NOT NULL,

    rua VARCHAR(200),

    numero VARCHAR(20),

    bairro VARCHAR(100),

    cidade VARCHAR(100),

    cep VARCHAR(10),

    telefone VARCHAR(20),

    horario_seg_sex VARCHAR(60),

    horario_sab VARCHAR(60),

    horario_dom VARCHAR(60),

    aceita_controlados TINYINT(1) DEFAULT 0,

    aceita_liquidos TINYINT(1) DEFAULT 0,

    aceita_comprimidos TINYINT(1) DEFAULT 1,

    aceita_perfurocortantes TINYINT(1) DEFAULT 0,

    latitude DOUBLE DEFAULT 0,

    longitude DOUBLE DEFAULT 0,

    ativo TINYINT(1) DEFAULT 1

) ENGINE=InnoDB;

-- ============================================================
-- TABELA LEMBRETES
-- ============================================================

CREATE TABLE lembretes (

    id INT AUTO_INCREMENT PRIMARY KEY,

    usuario_id INT NOT NULL,

    medicamento VARCHAR(150) NOT NULL,

    data_validade DATE NOT NULL,

    notificado TINYINT(1) DEFAULT 0,

    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE

) ENGINE=InnoDB;

-- ============================================================
-- USUÁRIO ADMIN
-- SENHA = admin
-- ============================================================

INSERT INTO usuarios
(nome, email, senha, is_admin)
VALUES
(
    'Administrador',
    'admin@descarte.com',
    '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
    1
);

-- ============================================================
-- PONTOS DE COLETA
-- ============================================================

INSERT INTO pontos_coleta
(
    nome,
    tipo,
    rua,
    numero,
    bairro,
    cidade,
    cep,
    horario_seg_sex,
    horario_sab,
    horario_dom,
    aceita_controlados,
    aceita_liquidos,
    aceita_comprimidos,
    aceita_perfurocortantes,
    latitude,
    longitude
)

VALUES

(
    'Farmácia Bem+',
    'FARMACIA',
    'Rua das Flores',
    '123',
    'Centro',
    'São Paulo',
    '01001-000',
    '8h às 20h',
    '8h às 14h',
    NULL,
    0,
    0,
    1,
    0,
    -23.5505,
    -46.6333
),

(
    'Drogaria Saúde',
    'DROGARIA',
    'Av. Brasil',
    '456',
    'Centro',
    'São Paulo',
    '01002-000',
    '7h às 22h',
    '8h às 18h',
    '8h às 18h',
    1,
    1,
    1,
    0,
    -23.5520,
    -46.6350
),

(
    'Posto de Descarte Municipal',
    'PONTO_MUNICIPAL',
    'Rua Verde',
    '789',
    'Jardim São Paulo',
    'São Paulo',
    '02003-000',
    '8h às 17h',
    NULL,
    NULL,
    1,
    1,
    1,
    1,
    -23.5480,
    -46.6300
),

(
    'Farmácia Popular',
    'FARMACIA',
    'Rua da Paz',
    '321',
    'Vila Nova',
    'São Paulo',
    '03004-000',
    '8h às 20h',
    '8h às 13h',
    NULL,
    0,
    0,
    1,
    0,
    -23.5540,
    -46.6370
),

(
    'UBS Centro',
    'POSTO_SAUDE',
    'Av. Principal',
    '1000',
    'Centro',
    'São Paulo',
    '01005-000',
    '7h às 19h',
    NULL,
    NULL,
    1,
    1,
    1,
    1,
    -23.5490,
    -46.6320
),

(
    'Drogaria Vida',
    'DROGARIA',
    'Rua das Acácias',
    '50',
    'Jardim América',
    'São Paulo',
    '01310-100',
    '8h às 22h',
    '9h às 15h',
    NULL,
    0,
    1,
    1,
    0,
    -23.5560,
    -46.6400
),

(
    'Posto Municipal Norte',
    'PONTO_MUNICIPAL',
    'Av. Dos Estados',
    '200',
    'Santana',
    'São Paulo',
    '02014-000',
    '8h às 17h',
    NULL,
    NULL,
    1,
    1,
    1,
    1,
    -23.5430,
    -46.6270
);

-- ============================================================
-- VERIFICAÇÃO FINAL
-- ============================================================

SELECT * FROM usuarios;

SELECT * FROM pontos_coleta;

SELECT * FROM lembretes;

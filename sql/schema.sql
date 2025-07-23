CREATE DATABASE IF NOT EXISTS controle_financeiro;
USE controle_financeiro;

CREATE TABLE transacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    tipo ENUM('receita', 'despesa') NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    data DATE NOT NULL
);

CREATE TABLE pessoa (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	logradouro VARCHAR(100),
    numero VARCHAR(10),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cep VARCHAR(10),
    cidade VARCHAR(100),
    estado VARCHAR(50),
	ativo BIT(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa(nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES('Pedro Humberto Mattiollo', 'Rua Guido Kaestner Sênior', '301', 'Apartamento 402', 'Boa Vista', '89012-360', 'Blumenau', 'SC', 1);
INSERT INTO pessoa(nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES('Dayanne Frizon Krindges', 'Rua Guido Kaestner Sênior', '301', 'Apartamento 402', 'Boa Vista', '89012-360', 'Blumenau', 'SC', 0);
INSERT INTO pessoa(nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES('Celso Mattiollo', 'Avenida XVII de Fevereiro', '408', 'Casa', 'Centro', '89745-970', 'Presidente Castelo Branco', 'SC', 1);
INSERT INTO pessoa(nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES('Marizete Savenhago Mattiollo', 'Avenida XVII de Fevereiro', '408', 'Casa', 'Centro', '89745-970', 'Presidente Castelo Branco', 'SC', 0);
INSERT INTO pessoa(nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES('Breno Mattiollo', 'Avenida XVII de Fevereiro', '408', 'Casa', 'Centro', '89745-970', 'Presidente Castelo Branco', 'SC', 1);
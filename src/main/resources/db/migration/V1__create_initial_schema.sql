CREATE TABLE cliente (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(255),
    documento VARCHAR(255) NOT NULL UNIQUE,
    tipo_documento VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP(6)
);

CREATE TABLE usuario (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    cliente_id UUID,
    CONSTRAINT fk_usuario_cliente
        FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE contrato (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    tipo_contrato VARCHAR(255) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    valor_mensal NUMERIC(38, 2) NOT NULL,
    consumo_antes_kwh NUMERIC(38, 2) NOT NULL,
    consumo_atual_kwh NUMERIC(38, 2) NOT NULL,
    CONSTRAINT fk_contrato_cliente
        FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE economia (
    id UUID PRIMARY KEY,
    contrato_id UUID NOT NULL,
    mes_referencia VARCHAR(255) NOT NULL,
    custo_antes NUMERIC(38, 2) NOT NULL,
    custo_depois NUMERIC(38, 2) NOT NULL,
    economia_gerada NUMERIC(38, 2),
    CONSTRAINT fk_economia_contrato
        FOREIGN KEY (contrato_id) REFERENCES contrato(id)
);

CREATE TABLE consumo_mensal (
    id UUID PRIMARY KEY,
    contrato_id UUID NOT NULL,
    mes_referencia VARCHAR(255) NOT NULL,
    kwh_consumido NUMERIC(38, 2) NOT NULL,
    custo_total NUMERIC(38, 2) NOT NULL,
    CONSTRAINT fk_consumo_contrato
        FOREIGN KEY (contrato_id) REFERENCES contrato(id)
);

CREATE TABLE mensalidade (
    id UUID PRIMARY KEY,
    contrato_id UUID NOT NULL,
    mes_referencia VARCHAR(255) NOT NULL,
    valor NUMERIC(38, 2) NOT NULL,
    vencimento DATE NOT NULL,
    data_pagamento DATE,
    status VARCHAR(255) NOT NULL,
    CONSTRAINT fk_mensalidade_contrato
        FOREIGN KEY (contrato_id) REFERENCES contrato(id)
);

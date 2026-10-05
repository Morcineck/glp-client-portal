DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_usuario_cliente'
    ) THEN
        ALTER TABLE usuario
            ADD CONSTRAINT uk_usuario_cliente UNIQUE (cliente_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_economia_contrato_mes'
    ) THEN
        ALTER TABLE economia
            ADD CONSTRAINT uk_economia_contrato_mes
            UNIQUE (contrato_id, mes_referencia);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_consumo_contrato_mes'
    ) THEN
        ALTER TABLE consumo_mensal
            ADD CONSTRAINT uk_consumo_contrato_mes
            UNIQUE (contrato_id, mes_referencia);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_mensalidade_contrato_mes'
    ) THEN
        ALTER TABLE mensalidade
            ADD CONSTRAINT uk_mensalidade_contrato_mes
            UNIQUE (contrato_id, mes_referencia);
    END IF;
END
$$;

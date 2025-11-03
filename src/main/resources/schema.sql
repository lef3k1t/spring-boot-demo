-- ===== DROP (на dev) =====
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS cards CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS customers CASCADE;

-- ===== CUSTOMERS =====
CREATE TABLE customers (
                           id           BIGSERIAL PRIMARY KEY,
                           first_name   VARCHAR(100),
                           last_name    VARCHAR(100),
                           email        VARCHAR(255) UNIQUE,
                           created_at   TIMESTAMP DEFAULT NOW()
);

-- ===== ACCOUNTS =====
-- ВАЖНО: колонка number существует (под индекс ниже)
CREATE TABLE accounts (
                          id           BIGSERIAL PRIMARY KEY,
                          number       VARCHAR(64) NOT NULL UNIQUE,
                          balance      NUMERIC(19,2) NOT NULL DEFAULT 0,
                          currency     VARCHAR(16) NOT NULL DEFAULT 'RUB',
                          customer_id  BIGINT REFERENCES customers(id) ON DELETE SET NULL,
                          created_at   TIMESTAMP DEFAULT NOW()
);

-- Индекс по номеру счёта (тот самый, что раньше падал в data.sql)
CREATE INDEX IF NOT EXISTS idx_account_number ON accounts(number);

-- ===== CARDS =====
CREATE TABLE cards (
                       id           BIGSERIAL PRIMARY KEY,
                       number       VARCHAR(32) NOT NULL UNIQUE,
                       cvv          VARCHAR(4),
                       exp_month    INT,
                       exp_year     INT,
                       is_locked    BOOLEAN NOT NULL DEFAULT FALSE,  -- колонка, из-за которой раньше падал Hibernate
                       account_id   BIGINT REFERENCES accounts(id) ON DELETE CASCADE,
                       created_at   TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_card_number ON cards(number);
CREATE INDEX IF NOT EXISTS idx_card_account_id ON cards(account_id);

-- ===== TRANSACTIONS =====
CREATE TABLE transactions (
                              id           BIGSERIAL PRIMARY KEY,
                              account_id   BIGINT REFERENCES accounts(id) ON DELETE CASCADE,
                              amount       NUMERIC(19,2) NOT NULL,
                              type         VARCHAR(32) NOT NULL,   -- например: DEBIT/CREDIT
                              description  VARCHAR(500),
                              created_at   TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_tx_account_id ON transactions(account_id);
CREATE INDEX IF NOT EXISTS idx_tx_created_at ON transactions(created_at);

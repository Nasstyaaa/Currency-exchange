CREATE TABLE currencies
(
    id        INTEGER     NOT NULL PRIMARY KEY AUTOINCREMENT,
    code      VARCHAR(3)  NOT NULL,
    full_name VARCHAR(64) NOT NULL,
    sign      VARCHAR(5)  NOT NULL
);
CREATE UNIQUE INDEX unique_index
    ON currencies (code);

CREATE TABLE exchange_rates
(
    id                 INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    base_currency_id   INTEGER NOT NULL,
    target_currency_id INTEGER NOT NULL,
    rate               DECIMAL(6),

    CONSTRAINT base_currency_fk FOREIGN KEY (base_currency_id) REFERENCES currencies (id),
    CONSTRAINT target_currency_fk FOREIGN KEY (target_currency_id) REFERENCES currencies (id)
);
CREATE UNIQUE INDEX unique_exchange_rate_pair
ON exchange_rates (base_currency_id, target_currency_id);
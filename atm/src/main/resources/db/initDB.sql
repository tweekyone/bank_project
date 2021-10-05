DROP INDEX IF EXISTS users_unique_email_idx;
DROP INDEX IF EXISTS accounts_unique_number_idx;
DROP INDEX IF EXISTS cards_unique_number_idx;
DROP INDEX IF EXISTS transaction_unique_acc_source_idx;
DROP INDEX IF EXISTS transaction_unique_acc_dest_idx;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS card;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS "user";

CREATE TABLE "user"
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY,
    name         VARCHAR     NOT NULL,
    surname      VARCHAR     NOT NULL,
    email        VARCHAR     NOT NULL,
    password     VARCHAR     NOT NULL,
    phone_number VARCHAR(16) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT user_constraint UNIQUE (email)
);
CREATE UNIQUE INDEX users_unique_email_idx ON "user" (email);

CREATE TABLE role
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    CONSTRAINT roles_constraint UNIQUE (name)
);

CREATE TABLE user_role
(
    user_id BIGINT  NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT user_roles_constraint UNIQUE (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
);

CREATE TABLE account
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY,
    number     NUMERIC(20, 0)              NOT NULL,
    is_default BOOL           DEFAULT TRUE NOT NULL,
    plan       VARCHAR                     NOT NULL,
    amount     NUMERIC(13, 2) DEFAULT 0.00 NOT NULL,
    user_id    BIGINT                      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT account_constraint UNIQUE (number),
    FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX accounts_unique_number_idx ON account (user_id, number);

CREATE TABLE card
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY,
    number           BIGINT        NOT NULL,
    pin_code         NUMERIC(4, 0) NOT NULL,
    plan             VARCHAR       NOT NULL,
    explication_date TIMESTAMP     NOT NULL,
    account_id       INTEGER       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT card_constraint UNIQUE (number),
    FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX cards_unique_number_idx ON card (account_id, number);

CREATE TABLE transaction
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY,
    source_account_id      BIGINT,
    destination_account_id BIGINT,
    amount                 NUMERIC(13, 2) DEFAULT 0.00 NOT NULL,
    operation_type         VARCHAR(20)                 NOT NULL,
    date_time              TIMESTAMP                   NOT NULL,
    state                  VARCHAR(20)                 NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (source_account_id) REFERENCES account (id) ON DELETE CASCADE,
    FOREIGN KEY (destination_account_id) REFERENCES account (id) ON DELETE CASCADE
);
CREATE INDEX transaction_unique_acc_source_idx ON transaction (source_account_id) WHERE source_account_id IS NOT NULL;
CREATE INDEX transaction_unique_acc_dest_idx ON transaction (destination_account_id) WHERE destination_account_id IS NOT NULL;
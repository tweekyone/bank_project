DELETE FROM transaction;
DELETE FROM card;
DELETE FROM account;
DELETE FROM user_role;
DELETE FROM role;
DELETE FROM "user";

INSERT INTO "user" (name, surname, email, password, phone_number)
VALUES ('User', 'Userovich', 'user@mail.com', 'pass', '+79996669966');

INSERT INTO "user" (name, surname, email, password, phone_number)
VALUES ('Roman', 'R.', 'some@mail.com', '123', '+5423'),
       ('Dina', 'K.', 'dina@mail.com', '567', '+1234');

INSERT INTO account(number, is_default, plan, amount, user_id)
VALUES ('40702810123456789125', true, 'plan', 5678.58, '1');

INSERT INTO account(number, is_default, plan, amount, user_id)
VALUES ('13', 'f', 'some_plan',	298.08, 1), ('9', 't', 'some_plan', 998.00, 2);

INSERT INTO card(number, pin_code, plan, explication_date, account_id)
VALUES ('4070281012345678', '1234', 'TESTPLAN', '2025-10-07T00:00:00+00:00', 1);

INSERT INTO transaction(source_account_id, destination_account_id, amount, operation_type, date_time, state)
VALUES (3, 5, 101, 'CASH', '2021-09-11 12:05:23', 'IN_PROCESS'),
       (3, 3,	500.00, 'WITHDRAWAL', '2021-10-11 22:10:23', 'DONE');


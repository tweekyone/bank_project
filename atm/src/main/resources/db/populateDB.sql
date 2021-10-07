DELETE FROM transaction;
DELETE FROM card;
DELETE FROM account;
DELETE FROM user_role;
DELETE FROM role;
DELETE FROM "user";

INSERT INTO "user" (name, surname, email, password, phone_number)
VALUES ('User', 'Userovich', 'user@mail.com', 'pass', '+79996669966');

INSERT INTO account(number, is_default, plan, amount, user_id)
VALUES ('40702810123456789125', true, 'plan', 5678.58, '1');
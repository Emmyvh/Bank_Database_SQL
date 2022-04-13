/*
This file provides a basic dataset for the client, wallet, and account tables in the database itself.
Additional entries, as well as transactions and loans should be created via the Java methods.
*/

INSERT INTO "Client"
    (client_number, given_name, last_name, street_name, house_number, zip_code, town)
VALUES
    ('1', 'Ann', 'Wright', 'Boulognalaan', '13', '7564', 'Overheuvel')

INSERT INTO "Client"
    (client_number, given_name, prefix, last_name, street_name, house_number, zip_code, town)
VALUES
    ('2', 'Daniel', 'van der', 'Leusd', 'Halleweg', '248', '4532', 'Wadder'),
    ('3', 'Niels', 'de', 'Graaf', 'Foureur', '2', '8475', 'Vlissewaard')

INSERT INTO "Client"
    (client_number, given_name, last_name, street_name, house_number, zip_code, town)
VALUES
    ('4', 'Fatima', 'Nadien', 'Flaviade', '35', '3894', 'Hiverdam'),
    ('5', 'Samuel', 'Remour', 'Grotebeek', '15', '7563', 'Overheuvel')

INSERT INTO "Client"
    (client_number, given_name, prefix, last_name, street_name, house_number, zip_code, town)
VALUES
    ('6', 'Nienke', 'von', 'Breugel', 'Hixmade', '98', '9872', 'Neverdorp'),
    ('7', 'Omar', 'al', 'Mamout', 'Juliuskade', '45', '8975', 'Ouddam'),
    ('8', 'Sterre', 'de', 'Groot-Havermake', 'Geradsweg', '23', '3673', 'Frivoor')

INSERT INTO "Client"
    (client_number, given_name, last_name, street_name, house_number, zip_code, town)
VALUES
    ('9', 'Hugo', 'Meuler', 'Parallaan', '45', '2378', 'Millewaard')

INSERT INTO "Client"
    (client_number, given_name, last_name, street_name, house_number, zip_code, town)
VALUES
    ('10', 'Mireille', 'van de', 'Weerd', 'Fixade', '59', '3345', 'Niemadorp')

INSERT INTO "Wallet"
    (wallet_number)
VALUES('1'),
    ('2'),
    ('3'),
    ('4'),
    ('5'),
    ('6'),
    ('7'),
    ('8'),
    ('9'),
    ('10')

UPDATE "Client"
SET wallet_number = 1
WHERE (client_number = 1)

UPDATE "Client"
SET wallet_number = 2
WHERE (client_number = 2)

UPDATE "Client"
SET wallet_number = 3
WHERE (client_number = 3)

UPDATE "Client"
SET wallet_number = 4
WHERE (client_number = 4)

UPDATE "Client"
SET wallet_number = 5
WHERE (client_number = 5)

UPDATE "Client"
SET wallet_number = 6
WHERE (client_number = 6)

UPDATE "Client"
SET wallet_number = 7
WHERE (client_number = 7)

UPDATE "Client"
SET wallet_number = 8
WHERE (client_number = 8)

UPDATE "Client"
SET wallet_number = 9
WHERE (client_number = 9)

UPDATE "Client"
SET wallet_number = 10
WHERE (client_number = 10)

INSERT INTO "Account"
    (account_number, amount, account_type, opening_date, client_number)
VALUES
    ('1', '100', 'current', '06-04-2022'),
    ('2', '5000', 'savings', '06-04-2022'),
    ('3', '450', 'current', '06-04-2022'),
    ('4', '2365', 'savings', '06-04-2022'),
    ('5', '857', 'current', '06-04-2022'),
    ('6', '9845', 'savings', '06-04-2022'),
    ('7', '834', 'current', '06-04-2022'),
    ('8', '2384', 'savings', '06-04-2022'),
    ('9', '384', 'current', '06-04-2022'),
    ('10', '38745', 'savings', '06-04-2022'),
    ('11', '393', 'currents', '06-04-2022'),
    ('12', '2874', 'savings', '06-04-2022'),
    ('13', '546', 'current', '06-04-2022'),
    ('14', '3874', 'savings', '06-04-2022'),
    ('15', '874', 'current', '06-04-2022'),
    ('16', '83479', 'savings', '06-04-2022'),
    ('17', '932', 'current', '06-04-2022'),
    ('18', '8237', 'savings', '06-04-2022'),
    ('19', '747', 'current', '06-04-2022'),
    ('20', '1562', 'savings', '06-04-2022'),
    ('21', '13820', 'invest', '06-04-2022'),
    ('22', '3784', 'invest', '06-04-2022'),
    ('23', '38472', 'invest', '06-04-2022')

UPDATE "Wallet"
SET account_number_current = 1
WHERE (wallet_number = 1)

UPDATE "Wallet"
SET account_number_current = 3
WHERE (wallet_number=2)

UPDATE "Wallet"
SET account_number_current = 5
WHERE (wallet_number=3)

UPDATE "Wallet"
SET account_number_current = 7
WHERE (wallet_number=4)

UPDATE "Wallet"
SET account_number_current = 9
WHERE (wallet_number=5)

UPDATE "Wallet"
SET account_number_current = 11
WHERE (wallet_number=6)

UPDATE "Wallet"
SET account_number_current = 13
WHERE (wallet_number=7)

UPDATE "Wallet"
SET account_number_current = 15
WHERE (wallet_number=8)

UPDATE "Wallet"
SET account_number_current = 17
WHERE (wallet_number=9)

UPDATE "Wallet"
SET account_number_current = 19
WHERE (wallet_number=10)

UPDATE "Wallet"
SET account_number_savings = 2
WHERE (wallet_number=1)

UPDATE "Wallet"
SET account_number_savings = 4
WHERE (wallet_number=2)

UPDATE "Wallet"
SET account_number_savings = 6
WHERE (wallet_number=3)

UPDATE "Wallet"
SET account_number_savings = 8
WHERE (wallet_number=4)

UPDATE "Wallet"
SET account_number_savings = 10
WHERE (wallet_number=5)

UPDATE "Wallet"
SET account_number_savings = 12
WHERE (wallet_number=6)

UPDATE "Wallet"
SET account_number_savings = 14
WHERE (wallet_number=7)

UPDATE "Wallet"
SET account_number_savings = 16
WHERE (wallet_number=8)

UPDATE "Wallet"
SET account_number_savings = 18
WHERE (wallet_number=9)

UPDATE "Wallet"
SET account_number_savings = 20
WHERE (wallet_number=10)

UPDATE "Wallet"
SET account_number_invest = 21
WHERE (wallet_number=5)

UPDATE "Wallet"
SET account_number_invest = 22
WHERE (wallet_number=7)

UPDATE "Wallet"
SET account_number_invest = 23
WHERE (wallet_number=8)
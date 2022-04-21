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
    ('1', '100', 'current', '06-04-2022', '1'),
    ('2', '5000', 'savings', '06-04-2022', '1'),
    ('3', '450', 'current', '06-04-2022', '2'),
    ('4', '2365', 'savings', '06-04-2022', '2'),
    ('5', '857', 'current', '06-04-2022', '3'),
    ('6', '9845', 'savings', '06-04-2022', '3'),
    ('7', '834', 'current', '06-04-2022', '4'),
    ('8', '2384', 'savings', '06-04-2022', '4'),
    ('9', '384', 'current', '06-04-2022', '5'),
    ('10', '38745', 'savings', '06-04-2022', '5'),
    ('11', '393', 'currents', '06-04-2022', '6'),
    ('12', '2874', 'savings', '06-04-2022', '6'),
    ('13', '546', 'current', '06-04-2022', '7'),
    ('14', '3874', 'savings', '06-04-2022', '7'),
    ('15', '874', 'current', '06-04-2022', '8'),
    ('16', '83479', 'savings', '06-04-2022', '8'),
    ('17', '932', 'current', '06-04-2022', '9'),
    ('18', '8237', 'savings', '06-04-2022', '9'),
    ('19', '747', 'current', '06-04-2022', '10'),
    ('20', '1562', 'savings', '06-04-2022', '10'),
    ('21', '13820', 'invest', '06-04-2022', '7'),
    ('22', '3784', 'invest', '06-04-2022', '8'),
    ('23', '38472', 'invest', '06-04-2022', '9')

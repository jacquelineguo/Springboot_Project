DROP SCHEMA if EXISTS easypay;
CREATE SCHEMA easypay;

USE easypay;

DROP TABLE IF EXISTS users;

CREATE TABLE `users` (
    `id` INT AUTO_INCREMENT,
    `uuid` VARCHAR(255) NOT NULL,
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `createdAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    `balance` DOUBLE NOT NULL DEFAULT 0
);

DESCRIBE users;
SELECT * FROM users;

--INSERT INTO users
--VALUES (1, '123', 'a', 'a@gmail.com', 'JacksonA', '1234567', 20221-1-1, 5000);
--INSERT INTO users
--VALUES (2, '132', 'b', 'b@gmail.com', 'JacksonB', '1234567', 20221-1-1, 5000);
--INSERT INTO users
--VALUES (3, '231', 'c', 'c@gmail.com', 'JacksonC', '1234567', 20221-1-1, 5000);
--INSERT INTO users
--VALUES (4, '456', 'c', 'c@gmail.com', 'JacksonC', '1234567', 20221-1-1, 5000);

DROP TABLE IF EXISTS PaymentDetails;

CREATE TABLE PaymentDetails
(
    transactionId            varchar(255) PRIMARY KEY,
    transactionTime          Datetime     NOT NULL,
    transactionType          varchar(255) NOT NULL,
    transactionStatus        varchar(255) NOT NULL,
    transactionStatusMessage varchar(255),

    payerId                  numeric      NOT NULL,
    payerName                varchar(255) NOT NULL,
    payerZipcode             varchar(255) NOT NULL,

    cardNumber               varchar(255) NOT NULL,
    cardType                 varchar(255) NOT NULL,
    cardExpDate              DATE         NOT NULL,
    cardCvv                  varchar(255) NOT NULL,

    paymentAmount            double       NOT NULL,
    refundAmount             double       NOT NULL,
    userId                   numeric      NOT NULL
);

DESCRIBE PaymentDetails;
SELECT * FROM PaymentDetails;